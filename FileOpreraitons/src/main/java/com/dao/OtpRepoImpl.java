package com.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.model.OneTimePassword;
import com.model.UserEntity;

@Repository
public class OtpRepoImpl {

    @Autowired
    OtpRepository otpRepo;
    
    public OneTimePassword crateOtp(OneTimePassword oneTimePassword) {
        OneTimePassword oneTimePassword1 = null;
        
        try {
            oneTimePassword1= otpRepo.save(oneTimePassword);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return oneTimePassword1;
    }

    public OneTimePassword getOtpByOtpNumber(String otp) {
        OneTimePassword oneTimePassword1 = null;
        try {
            oneTimePassword1= otpRepo.findByOtp(otp);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return oneTimePassword1;
    }

    public void deleteOtp(OneTimePassword oneTimePassword) {
        try {
            otpRepo.delete(oneTimePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public OneTimePassword findByEntity(UserEntity entity) {
        OneTimePassword oneTimePassword = null;
        try {
             oneTimePassword = otpRepo.findByEntity(entity);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return oneTimePassword;
    }
    
    public void sendSms(String message,String number) {
            
        AWSCredentials awsCredentials = new BasicAWSCredentials("AKIAY4FTLO5BW27IMLHW", "YlqCw4H+bXhuiejU82ZYMsfSZTbQcUAT2IqstmUb");
        final AmazonSNSClient client = new AmazonSNSClient(awsCredentials);
        client.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
        Map<String, MessageAttributeValue> smsAttributes =
                new HashMap<String, MessageAttributeValue>();
        smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
                .withStringValue("789456456") //The sender ID shown on the device.
                .withDataType("String"));
        smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue()
                .withStringValue("0.50") //Sets the max price to 0.50 USD.
                .withDataType("Number"));
        smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
                .withStringValue("Transactional") //Sets the type to promotional.
                .withDataType("String"));
        
        AmazonSNSClient snsClient = new AmazonSNSClient(awsCredentials);
        PublishRequest request = new PublishRequest();
        request.withMessage("Otp for verification :"+message)
        .withPhoneNumber("+91"+number)
        .withMessageAttributes(smsAttributes);
        PublishResult result=snsClient.publish(request);
        System.out.println(result);
    }
    
}
