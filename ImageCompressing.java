package com.amazonaws.lambda.imagecompressing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

public class ImageCompressing implements RequestHandler<S3Event, String> {

	private static BasicAWSCredentials creds = new BasicAWSCredentials("XXXXXX",
			"XXXXXXX");
	private static AmazonS3 s3 = AmazonS3Client.builder().withRegion("ap-south-1")
			.withCredentials(new AWSStaticCredentialsProvider(creds)).build();

    public ImageCompressing() {}


    @Override
    public String handleRequest(S3Event o, Context context) {
        context.getLogger().log("Received event: " + o);
    	S3EventNotification.S3EventNotificationRecord record = o.getRecords().get(0);

		S3EventNotification.S3Entity s3Entity = record.getS3();
		String bucketName = s3Entity.getBucket().getName();
		String key = s3Entity.getObject().getKey();
        try {


    		try (S3Object object = s3.getObject(new GetObjectRequest(bucketName, key))) {
    			Map<String, String> metadata = object.getObjectMetadata().getUserMetadata();
    			if (metadata.get("compressed") == null) {
    				metadata.put("compressed", "true");
    			} else {
    				return "Already Compressed";
    			}

    			ObjectMetadata objectMetadata = new ObjectMetadata();
    			objectMetadata.setUserMetadata(metadata);
    			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    			compress(object.getObjectContent(), byteArrayOutputStream);

    			objectMetadata.setContentLength(byteArrayOutputStream.size());
    			PutObjectRequest objectRequest = new PutObjectRequest(bucketName,"compress/"+key.split("/")[1],
    					new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead);
    			s3.putObject(objectRequest);
    			
    			System.out.println("fsffsfsfdsfhjsbfhjshjfhjsbfhjb");
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            context.getLogger().log(String.format(
                "Error getting object %s from bucket %s. Make sure they exist and"
                + " your bucket is in the same region as this function.", key, bucketName));
           }
        }catch (Exception e) {
			// TODO: handle exception
		}
		return key;
    }
    
    public static void compress(InputStream inputStream, OutputStream outputStream) throws IOException {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			IOUtils.copy(inputStream, byteArrayOutputStream);
			byte[] bytes = byteArrayOutputStream.toByteArray();

			ImageType imageType = findInputtype.getImageType(new ByteArrayInputStream(bytes));
			System.out.println("Input is: " + imageType);

			switch (imageType) {
			case PNG:
			case GIF:
			case JPG:
				compress(imageType, new ByteArrayInputStream(bytes), outputStream);
				break;
			}
		}
	}

	public static void compress(ImageType imageType, InputStream inputStream, OutputStream outputStream)
			throws IOException {
		try {
			
			Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(imageType.getExtension());
			ImageWriter writer = writers.next();

			ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
			writer.setOutput(ios);

			ImageWriteParam param = writer.getDefaultWriteParam();
			// Check if canWriteCompressed is true
			if (param.canWriteCompressed()) {
				param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				param.setCompressionQuality(0.02f);
			}
			// End of check
			writer.write(null, new IIOImage(ImageIO.read(inputStream), null, null), param);
		} finally {
			inputStream.close();
		}
	}
}