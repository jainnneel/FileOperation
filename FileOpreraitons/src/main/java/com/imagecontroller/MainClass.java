package com.imagecontroller;

import java.io.ByteArrayInputStream;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class MainClass {

	private static BasicAWSCredentials creds = new BasicAWSCredentials("XXXXXXXX",
			"XXXXXXXX");
	private static AmazonS3 s3 = AmazonS3Client.builder().withRegion("ap-south-1")
			.withCredentials(new AWSStaticCredentialsProvider(creds)).build();

	public static String uploadimage(String bucketname,String filename,byte[] data) {
	    PutObjectRequest objectRequest = new PutObjectRequest(bucketname, filename,  new ByteArrayInputStream(data), null);
	    objectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
		s3.putObject(objectRequest);
		String link = s3.getUrl(bucketname, filename).toString();
		System.out.println("fileuploaded S3");
		return link;
	}

}
