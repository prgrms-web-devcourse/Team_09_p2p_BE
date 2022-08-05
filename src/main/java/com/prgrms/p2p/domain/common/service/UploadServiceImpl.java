package com.prgrms.p2p.domain.common.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

  private final AmazonS3 amazonS3;
  @Value("${cloud.aws.s3.bucket.name}")
  public String bucket;
  @Value("${cloud.aws.s3.bucket.url}")
  private String defaultUrl;

  private static String getUuid() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }

  @Override
  public String uploadImg(MultipartFile multipartFile) {
    String origName = multipartFile.getOriginalFilename();
    final String ext = origName.substring(origName.lastIndexOf('.'));
    String saveFileName = getUuid() + ext;
    BufferedImage image = null;
    try {
      image = ImageIO.read(multipartFile.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return uploadImgToS3(image, saveFileName, ext);
  }

  @Override
  public String uploadImgToS3(BufferedImage image, String Filename, String ext) {
    String url;
    try {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      ImageIO.write(image, ext.substring(1), os);
      byte[] bytes = os.toByteArray();
      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentLength(bytes.length);
      objectMetadata.setContentType(ext.substring(1));
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
      final TransferManager transferManager = new TransferManager(this.amazonS3);
      final PutObjectRequest request = new PutObjectRequest(bucket, Filename, byteArrayInputStream,
          objectMetadata);
      final Upload upload = transferManager.upload(request);
      upload.waitForCompletion();
    } catch (AmazonServiceException | InterruptedException | IOException e) {
      e.printStackTrace();
    }
    url = defaultUrl + Filename;
    return url;
  }

  @Override
  public void delete(String path) {
    try {
      amazonS3.deleteObject(bucket, path.split("/")[3]);
    } catch (AmazonServiceException e) {
      e.printStackTrace();
    }
  }
}