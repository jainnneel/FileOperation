package com.amazonaws.lambda.demo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;


public class LambdaFunctionHandler implements RequestHandler<S3Event, String> {

	private static BasicAWSCredentials creds = new BasicAWSCredentials("XXXXXX",
			"XXXXXXXX");
	private static AmazonS3 s3 = AmazonS3Client.builder().withRegion("ap-south-1")
			.withCredentials(new AWSStaticCredentialsProvider(creds)).build();

	public LambdaFunctionHandler() {
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
			if (folderName.equals("jpg")) {
				System.out.println(key1);
				outputStream = convertFormat(inputStream1, byteArrayOutputStream, "JPG");
				String fileName= key1.split("\\.")[0];
				rkey = fileName.concat(".jpg");
				System.out.println(rkey);
			} else if (folderName.equals("png")) {
				System.out.println(key1);
				outputStream = convertFormat(inputStream1, byteArrayOutputStream, "PNG");
				String fileName= key1.split("\\.")[0];
				rkey = fileName.concat(".png");
				System.out.println(rkey);
			}else if (folderName.equals("jpeg")) {
				System.out.println(key1);
				outputStream = convertFormat(inputStream1, byteArrayOutputStream, "JPEG");
				String fileName= key1.split("\\.")[0];
				rkey = fileName.concat(".jpeg");
				System.out.println(rkey);
			} else if (folderName.equals("pdf")) {
				System.out.println(key1);
				outputStream = convert.wordToPdf(inputStream1, byteArrayOutputStream);
				String fileName= key1.split("\\.")[0];
				rkey = fileName.concat(".pdf");
				System.out.println(rkey);
			} else {
				System.out.println(key1);
				outputStream = convert.pdfToWord(inputStream1, byteArrayOutputStream);
				String fileName= key1.split("\\.")[0];
				rkey = fileName.concat(".doc");
				System.out.println(rkey);
			}
		
			byte[] data = ((ByteArrayOutputStream) outputStream).toByteArray();
			PutObjectRequest objectRequest = new PutObjectRequest("com.jntalks.convertedimage", folderName + "/" + rkey,
					new ByteArrayInputStream(data), null);
			objectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
			s3.putObject(objectRequest);
			System.out.println("3333333333");

			return "doneee";
		} catch (Exception e) {
			e.printStackTrace();
			context.getLogger().log(String.format("Error getting object %s from bucket %s. Make sure they exist and"
					+ " your bucket is in the same region as this function.", key, bucket));
		}
		return key;
	}

	public static OutputStream convertFormat(InputStream inputStream, OutputStream outputStream, String format)
			throws IOException {

		// reads input image from file
		BufferedImage inputImage = ImageIO.read(inputStream);

		// writes to the output image in specified format
		boolean result = ImageIO.write(inputImage, format, outputStream);

		System.out.println(result);

		// needs to close the streams
		outputStream.close();
		inputStream.close();

		return outputStream;
	}
}