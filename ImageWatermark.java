package com.imagewatermark;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class ImageWatermark implements RequestHandler<S3Event, String> {

	BasicAWSCredentials creds = new BasicAWSCredentials("AKIAWQWX53INR6YRZTTA",
			"Xl73/IfhVIvcHVqedBdr80HY+7VJAWCuNPiQK+IC");
	private AmazonS3 s3 = AmazonS3Client.builder().withRegion("ap-south-1")
			.withCredentials(new AWSStaticCredentialsProvider(creds)).build();

	public ImageWatermark() {
	}

	// Test purpose only.
	ImageWatermark(AmazonS3 s3) {
		this.s3 = s3;
	}

	@Override
	public String handleRequest(S3Event event, Context context) {
		context.getLogger().log("Received event: " + event);

		// Get the object from the event and show its content type
		String bucket = event.getRecords().get(0).getS3().getBucket().getName();
		String key = event.getRecords().get(0).getS3().getObject().getKey();
		System.out.println("bucketname=>" + bucket);
		System.out.println("key=>" + key);

		try {
			S3Object f1 = s3.getObject(new GetObjectRequest(bucket, key));
			S3ObjectInputStream inputStream1 = f1.getObjectContent();

			System.out.println("1111");
			S3Object f2 = s3.getObject("imagewatermark", "watermark/Neel1.jpg");
			S3ObjectInputStream inputStream2 = f2.getObjectContent();
			System.out.println("2222");
			System.out.println(inputStream1+"  DSDADADSa  "+inputStream2);
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			OutputStream addImageWatermark = AddWatermark.addImageWatermark(inputStream1, inputStream2,
					byteArrayOutputStream);
			byte[] data = ((ByteArrayOutputStream) addImageWatermark).toByteArray();
			System.out.println("3333333333");
			s3.putObject("imagewithwatermark",f1.getKey(), new ByteArrayInputStream(data), null);
			
			String contentType = f1.getObjectMetadata().getContentType();
			context.getLogger().log("CONTENT TYPE: " + contentType);
			return contentType;
		} catch (Exception e) {
			e.printStackTrace();
			context.getLogger().log(String.format("Error getting object %s from bucket %s. Make sure they exist and"
					+ " your bucket is in the same region as this function.", key, bucket));
		}
		return null;
	}
}