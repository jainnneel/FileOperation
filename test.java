package com.amazonaws.lambda.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class test {


	private static BasicAWSCredentials creds = new BasicAWSCredentials("XXXXXX",
			"XXXXX");
	private static AmazonS3 s3 = AmazonS3Client.builder().withRegion("ap-south-1")
			.withCredentials(new AWSStaticCredentialsProvider(creds)).build();

	
	public static void main(String[] args) throws IOException {
		S3Object f1 = s3.getObject("com.jntalks.conveter", "wordTopdf/task111.doc");
		S3ObjectInputStream inputStream1 = f1.getObjectContent();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		OutputStream addImageWatermark = convert.wordToPdf(inputStream1, byteArrayOutputStream);
		byte[] data = ((ByteArrayOutputStream) addImageWatermark).toByteArray();
		s3.putObject("com.jntalks.conveter","wordTopdf/task111.pdf", new ByteArrayInputStream(data), null);
	};
}
