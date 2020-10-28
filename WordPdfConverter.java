package com.wordpdfconverter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class WordPdfConverter implements RequestHandler<S3Event, String> {

    private AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();

    public WordPdfConverter() {}

    // Test purpose only.
    WordPdfConverter(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public String handleRequest(S3Event event, Context context) {
        context.getLogger().log("Received event: " + event);

        // Get the object from the event and show its content type
        String bucket = event.getRecords().get(0).getS3().getBucket().getName();
        String key = event.getRecords().get(0).getS3().getObject().getKey();
        try {
			S3Object f1 = s3.getObject(new GetObjectRequest(bucket, key));
			S3ObjectInputStream inputStream1 = f1.getObjectContent();
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			OutputStream outputStream = null;
			String folderName = key.split("/")[0];
			String key1 = key.split("/")[1];
			String rkey;
			System.out.println(folderName);
			if (folderName.equals("jpegTojpg")) {
				System.out.println(key1);
//				outputStream = convertFormat(inputStream1, byteArrayOutputStream, "JPG");
				rkey=key1.replaceAll("jpeg","jpg");
				System.out.println(rkey);
			}else{
				System.out.println(key1);
//				outputStream = convertFormat(inputStream1, byteArrayOutputStream, "JPEG");
				rkey=key1.replaceAll("jpg","jpeg");
				System.out.println(rkey);
			}
			String key2 = "decompress/"+rkey;
			System.out.println(key2);
			byte[] data = ((ByteArrayOutputStream) outputStream).toByteArray();
			PutObjectRequest objectRequest = new PutObjectRequest("com.jntalks.compressimage",key2,  new ByteArrayInputStream(data), null);
			objectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
			s3.putObject("com.jntalks.conveter",folderName+"/"+rkey , new ByteArrayInputStream(data), null);
			System.out.println("3333333333");
			
			return "doneee";
        } catch (Exception e) {
            e.printStackTrace();
            context.getLogger().log(String.format(
                "Error getting object %s from bucket %s. Make sure they exist and"
                + " your bucket is in the same region as this function.", key, bucket));
            throw e;
        }
    }
}