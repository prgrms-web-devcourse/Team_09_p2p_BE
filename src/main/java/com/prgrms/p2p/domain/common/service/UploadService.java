package com.prgrms.p2p.domain.common.service;

import java.awt.image.BufferedImage;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

  String uploadImg(MultipartFile multipartFile);

  String uploadImgToS3(BufferedImage image, String Filename, String ext);

  void delete(String path);

  private String getUUID() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }
}
