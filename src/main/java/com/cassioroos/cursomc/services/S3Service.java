package com.cassioroos.cursomc.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cassioroos.cursomc.services.exceptions.FileException;

@Service
public class S3Service {

	private Logger log = LoggerFactory.getLogger(S3Service.class);

	@Autowired
	private AmazonS3 s3Client;

	@Value("${s3.bucket}")
	private String bucketName;

	public URI uploadFile(MultipartFile multipartfile) {
		try {
			String filename = multipartfile.getOriginalFilename();
			InputStream is = multipartfile.getInputStream();
			String contentType = multipartfile.getContentType();

			return uploadFile(is, contentType, filename);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileException("IOException " + e.getMessage());
		}
	}

	public URI uploadFile(InputStream is, String contentType, String filename) {
		try {
			log.info("Upload iniciado");
			ObjectMetadata om = new ObjectMetadata();
			om.setContentType(contentType);
			s3Client.putObject(new PutObjectRequest(bucketName, filename, is, om));
			log.info("Upload finalizado");

			return s3Client.getUrl(bucketName, filename).toURI();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new FileException("URISyntaxException " + e.getMessage());
		}
	}
}
