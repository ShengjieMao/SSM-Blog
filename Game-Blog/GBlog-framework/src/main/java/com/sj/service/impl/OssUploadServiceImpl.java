package com.sj.service.impl;

import com.google.gson.Gson;
import com.sj.domain.ResponseResult;
import com.sj.enums.AppHttpCodeEnum;
import com.sj.exception.SystemException;
import com.sj.service.impl.OssUploadService;
import com.sj.service.UploadService;
import com.sj.utils.PathUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@Data
@ConfigurationProperties(prefix = "oss")
public class OssUploadServiceImpl implements OssUploadService {

    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        String originalFilename = img.getOriginalFilename();
        long fileSize = img.getSize();

        if (fileSize > 2 * 1024 * 1024) {
            throw new SystemException(AppHttpCodeEnum.FILE_SIZE_ERROR);
        }

        if(!originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }

        String filePath = PathUtils.generateFilePath(originalFilename);

        String url = uploadOss(img,filePath);
        return ResponseResult.okResult(url);
    }


    private String accessKey;
    private String secretKey;
    private String bucket;

    private String uploadOss(MultipartFile imgFile, String filePath){
        Configuration cfg = new Configuration(Region.regionNa0());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        UploadManager uploadManager = new UploadManager(cfg);

        String key = filePath;

        try {
            InputStream inputStream = imgFile.getInputStream();

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println("Successfully uploaded with the key " + putRet.key);
                System.out.println("Successfully uploaded with the hash " + putRet.hash);
                // Set default Qiniu address
                return "http://ryhsvluq3.hn-bkt.clouddn.com/" + key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        }catch (Exception e) {
            //ignore
        }
        return "Fail to upload";
    }
}