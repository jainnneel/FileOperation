package com.imagecontroller;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dao.OtpAttemptService;
import com.dao.OtpRepoImpl;
import com.dao.RegUserImpl;
import com.dao.TokenImpl;
import com.dao.UserImpl;
import com.model.OneTimePassword;
import com.model.RegUser;
import com.model.Token;
import com.model.UserEntity;

@Controller
public class DahboardController {

    @Autowired
    private UserImpl userImpl;

    @Autowired
    private RegUserImpl regUserImpl;

    @Autowired
    private TokenImpl tokenImpl;

    @Autowired
    private OtpRepoImpl otpRepoImpl;

    @Autowired
    private OtpAttemptService otpAttemptService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "checkemobile", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ResponseEntity checkeMobile(@RequestBody Object mobile) {
        System.out.println(mobile.toString());
        ResponseEntity r = new ResponseEntity();
        if (regUserImpl.getUserByEmailOrMobile(mobile.toString().trim())!=null) {
            r.setStatus("nottt");
        } else {
            r.setStatus("yesss");
        }
        return r;
    }

    @RequestMapping(value = "checkeemail", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ResponseEntity checkeEmail(@RequestBody Object email) {
        ResponseEntity r = new ResponseEntity();
        if (regUserImpl.getUserByEmailOrMobile( email.toString().trim())!=null) {
            r.setStatus("nottt");
        } else {
            r.setStatus("yesss");
        }
        return r;
    }

    @RequestMapping("SignupAndLogin")
    public String signupwithEmail(Model m, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            m.addAttribute("UserData", new UserEntity());
            return "SignupAndLogin";
        } else {
            return "redirect:/home";
        }
    }

    @RequestMapping(value = "signupwithMobile", consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ResponseEntity signupwithMobile(@RequestBody UserEntity entity, Model m, BindingResult result,
            HttpServletRequest request) {
        ResponseEntity responseEntity = new ResponseEntity();
        System.out.println(entity);
        ValidationUtils.rejectIfEmptyOrWhitespace(result, "name", "Name can not be empty.");
        ValidationUtils.rejectIfEmptyOrWhitespace(result, "email", "Email can not be empty.");
        ValidationUtils.rejectIfEmptyOrWhitespace(result, "pass", "Pass can not be empty.");
        if (regUserImpl.findByEmailOrMobile(entity.getMobile(), entity.getEmail())) {
            responseEntity.setStatus("registed");
            return responseEntity;
        } else if (result.hasErrors()) {
            responseEntity.setStatus(result.hasErrors());
            return responseEntity;
        } else {
            UserEntity entity1 = userImpl.saveUser(entity);
            request.getSession().setAttribute("mobile",entity1.getMobile());
            request.getSession().setAttribute("email",entity1.getEmail());
            Token t = tokenImpl.crateToken(new Token(entity1));
            String url = request.getScheme() + "://" + request.getServerName() + ":8080/confirm-mail?token="
                    + t.getToken();
            System.out.println(url);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(entity.getEmail());
            mailMessage.setSubject("Email verification");
            mailMessage.setFrom("blankteam9933@gmail.com");
            mailMessage.setText("click on the link for verification:=>" + url);
//            emailSenderService.sendEmail(mailMessage);
            if (!entity.getMobile().equals("")) {
                OneTimePassword oneTimePassword = otpRepoImpl.crateOtp(new OneTimePassword(entity1));
                System.out.println(oneTimePassword.getOtp());
//              otpRepoImpl.sendSms(oneTimePassword.getOtp(), entity.getMobile());
                responseEntity.setStatus("otpdone");
                return responseEntity;
            } else {
                responseEntity.setStatus("emaildone");
                return responseEntity;
            }
        }
    }

    @RequestMapping("/confirm-mail")
    public String emailVerification(@RequestParam("token") String token, Model m) {
        Token t = tokenImpl.getTokenByTokenString(token);

        if (t == null) {
            m.addAttribute("link expired", "linkex");
            return "verification";
        } else {
            UserEntity entity = userImpl.findByEmailWithoutEnable(t.getEntity().getEmail());
            entity.setD(new Date(new java.util.Date().getTime()));
            entity.setFreeTierExpire();
            entity.setEnable(true);
            userImpl.saveUser(entity);
            ModelMapper mapper = new ModelMapper();
            RegUser regEntity = mapper.map(entity, RegUser.class);
            regUserImpl.createUser(regEntity);
            tokenImpl.deleteToken(t);
            return "redirect:/home";
        }
    }

    @RequestMapping("verification")
    public String otpPage() {
        return "verification";
    }

    @RequestMapping("emailverify")
    public String emailverify() {
        return "emailverify";
    }

    @RequestMapping(value = "otpverify", consumes = { MediaType.ALL_VALUE }, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity otpVerify(@RequestBody Object otp, Model m) {
        System.out.println(otp.toString());
        if (otpAttemptService.isOtpAttemptExceed(getClientIP())) {
            return new ResponseEntity("limit", "");
        }
        OneTimePassword oneTimePassword = otpRepoImpl.getOtpByOtpNumber(otp.toString());

        ResponseEntity res = new ResponseEntity();
        if (oneTimePassword == null) {
            m.addAttribute("invalid", "otp is invalid");
            m.addAttribute("UserData", new UserEntity());
            res.setStatus("not");
            otpAttemptService.otpFailed(getClientIP());
            return res;

        } else {
            otpAttemptService.otpSucceeded(getClientIP());
            UserEntity entity = userImpl.getUserByIdWithoutEnable(oneTimePassword.getEntity().getId());
            entity.setEnable(true);
            entity.setD(new Date(new java.util.Date().getTime()));
            entity.setFreeTierExpire();
            userImpl.saveUser(entity);
            ModelMapper mapper = new ModelMapper();
            RegUser regEntity = mapper.map(entity, RegUser.class);
            regUserImpl.createUser(regEntity);
            otpRepoImpl.deleteOtp(oneTimePassword);
            res.setStatus("done");
            return res;
        }
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    @RequestMapping(value = "resendotp", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public ResponseEntity resendotp() {
        ResponseEntity r = new ResponseEntity();
        String mobile = (String)request.getSession().getAttribute("mobile");
        UserEntity entity1 = userImpl.findByMobileWithoutEnable(mobile.trim());
        System.out.println(entity1.getMobile());
        OneTimePassword timePassword = otpRepoImpl.findByEntity(entity1);
        timePassword.setOtp((int) (Math.random() * 1000000 + 100001) + "");
        OneTimePassword oneTimePassword = otpRepoImpl.crateOtp(timePassword);
        System.out.println(oneTimePassword.getOtp());
//      otpRepoImpl.sendSms(oneTimePassword.getOtp(), entity.getMobile());
        otpAttemptService.otpSucceeded(getClientIP());
        r.setStatus("otpdone");
        return r;
    }

}
