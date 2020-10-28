package com.imagecontroller;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.dao.RegUserImpl;
import com.dao.UserImpl;
import com.model.RegUser;
import com.model.UserEntity;

@Controller
public class Imagecontroller {

    @Autowired
    RegUserImpl userImpl;
    
    @RequestMapping("/home")
    public String home(@AuthenticationPrincipal UserDetails userDetails,Model model) {
        if(userDetails!=null) {
        RegUser entity = userImpl.getUserByEmailOrMobile(userDetails.getUsername());
        boolean userIsInFreeTrialOrNot = userIsInFreeTrialOrNot(entity);
        model.addAttribute("expire", userIsInFreeTrialOrNot);
        model.addAttribute("user", entity);
        }
        return "home";
    }

    @RequestMapping(value = "/proccessing/uploadForCompress", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity getImageDataForCompress(@RequestParam("image") MultipartFile file,@AuthenticationPrincipal UserDetails userDetails) throws IOException {
        ResponseEntity entity = new ResponseEntity();
        if(userDetails==null) {
            entity.setStatus("notdone");
            return entity;
        }else {
        String link = MainClass.uploadimage("com.jntalks.compressimages", "decompress/" + file.getOriginalFilename(),
                file.getBytes());
//        PublishResult pushNotificationToSns = pushNotificationToSns("image is compressed");
//        System.out.println(pushNotificationToSns);
        link = link.replaceAll("decompress", "compress");
        entity.setStatus("done");
        entity.setLink(link);
        Math.pow(4,5);
        return entity;
        }
    }

    private PublishResult pushNotificationToSns(String string) {
        AWSCredentials awsCredentials = new BasicAWSCredentials("AKIAY4FTLO5BW27IMLHW", "YlqCw4H+bXhuiejU82ZYMsfSZTbQcUAT2IqstmUb");
        Map<String, MessageAttributeValue> smsAttributes =
                new HashMap<String, MessageAttributeValue>();
        smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
                .withStringValue("789456456") //The sender ID shown on the device.
                .withDataType("String"));
        smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
                .withStringValue("Transactional") //Sets the type to promotional.
                .withDataType("String"));
        
        AmazonSNS amazonSNS = new AmazonSNSClient(awsCredentials);
        amazonSNS.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
        PublishResult publishResult = amazonSNS.publish(new PublishRequest()
                                        .withTargetArn("arn:aws:sns:ap-south-1:610261497667:FileOperation")
                                        .withMessage(string).withMessageAttributes(smsAttributes)
                                         );
        return publishResult;
    }

    @RequestMapping(value = "/proccessing/uploadForImageConvert", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity getImageDataForConvert(@RequestParam("image") MultipartFile file, @RequestParam("to") String to,@AuthenticationPrincipal UserDetails userDetails) throws IOException, InterruptedException {
        ResponseEntity entity = new ResponseEntity();
        if(userDetails==null) {
            entity.setStatus("notdone");
            return entity;
        }else {
        String key = to;
        String link = MainClass.uploadimage("com.jntalks.convertimage", key + "/" + file.getOriginalFilename(),
                file.getBytes());
        link = link.replaceAll("com.jntalks.convertimage", "com.jntalks.convertedimage");
        String linkArray[] = link.split("\\.");
        link = linkArray[0] + "." + linkArray[1] + "." + linkArray[2] + "." + linkArray[3] + "." + linkArray[4] + "."
                + linkArray[5] + "." + linkArray[6];
        link = link.concat("." + to);
        Thread.sleep(5000);
        entity.setStatus("done");
        entity.setLink(link);
        return entity;
        }
    }

    @RequestMapping(value = "/proccessing/uploadForFileConvert", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity getImageDataFileForConvert(@RequestParam("image") MultipartFile file,
            @RequestParam("to") String to,@AuthenticationPrincipal UserDetails userDetails) throws IOException, InterruptedException {
        ResponseEntity entity = new ResponseEntity();
        if(userDetails==null) {
            entity.setStatus("notdone");
            return entity;
        }else {
        String key = to;
        String link = MainClass.uploadimage("com.jntalks.convertimage", key + "/" + file.getOriginalFilename(),
                file.getBytes());
        link = link.replaceAll("com.jntalks.convertimage", "com.jntalks.convertedimage");
        String linkArray[] = link.split("\\.");
        link = linkArray[0] + "." + linkArray[1] + "." + linkArray[2] + "." + linkArray[3] + "." + linkArray[4] + "."
                + linkArray[5] + "." + linkArray[6];
        link = link.concat("." + to);
        Thread.sleep(10000);
        entity.setStatus("done");
        entity.setLink(link);
        System.out.println(entity.getLink());
        return entity;
        }
    }

    @RequestMapping(value = "/proccessing/uploadForAddWaterMark", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity uploadForAddWaterMark(@RequestParam("waterMark") MultipartFile waterMark,
            @RequestParam("mainImage") MultipartFile mainImage,@AuthenticationPrincipal UserDetails userDetails)
            throws IOException, InterruptedException {
        ResponseEntity entity = new ResponseEntity();
        if(userDetails==null) {
            entity.setStatus("notdone");
            return entity;
        }else {
        String mainLink = MainClass.uploadimage("com.jntalks.watermarkimage","main/"+mainImage.getOriginalFilename(), mainImage.getBytes());
        String waterLink = MainClass.uploadimage("com.jntalks.watermarkimage","watermark/"+mainImage.getOriginalFilename(), waterMark.getBytes());
        System.out.println(mainLink +"  "+waterLink);
        String conLink= "https://s3.ap-south-1.amazonaws.com/com.jntalks.watermarkimage/converted/"+mainImage.getOriginalFilename();
        Thread.sleep(5000);
        entity.setStatus("done");
        entity.setLink(conLink);
        return entity;
        }
    }
    
    private boolean userIsInFreeTrialOrNot(RegUser entity) {
        Date d = new Date(new java.util.Date().getTime());
        if(TimeUnit.MILLISECONDS.toDays(entity.getFreeTierExpire().getTime()-d.getTime())<0) {
            return false;
        }else {
            return true; 
        }
    }
    
    

}
