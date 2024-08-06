package com.sj.service.impl;

import com.sj.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface OssUploadService {
    ResponseResult uploadImg(MultipartFile img);
}