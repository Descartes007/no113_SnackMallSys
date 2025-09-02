package com.wen.controller;

import com.wen.entity.InfFileDO;
import com.wen.service.impl.UploadLoadSerivceImpl;
import com.wen.util.general.CommonResult;
import com.wen.util.oss.AliyunOssUtil;
import com.wen.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 *图片上传
 */
@Slf4j
@CrossOrigin
@RestController
public class OssController {

    @Autowired
    private AliyunOssUtil ossUtil;


    @Resource
    UploadLoadSerivceImpl uploadLoadSerivce;

    @RequestMapping("/uploadImage")
    public CommonResult upload(@RequestParam("name") String folderName,
                               @RequestParam("file") MultipartFile file) throws IOException {
        //本地上传
        if (file != null) {
            String fileName = folderName+file.getOriginalFilename();
            if (StringUtils.isNotBlank(fileName)) {
                try  {
                    Result<InfFileDO> infFileDOResult = uploadLoadSerivce.uploadFile(file);
                    String url = infFileDOResult.getData().getUrl();
                    return new CommonResult(200, "上传成功", url);
                } catch (Exception e) {
                    log.error("文件上传失败", e);
                    return CommonResult.error("上传失败");
                } finally {
                }
            }
        }

        //OSS上传
//        if (file != null) {
//            try {
//                // 将 MultipartFile 转换为 File
//                File tempFile = File.createTempFile("temp", file.getOriginalFilename());
//                file.transferTo(tempFile);
//
//                // 使用 OSS 工具类上传文件
//                String url = ossUtil.upload(folderName, tempFile);
//
//                // 删除临时文件
//                tempFile.delete();
//
//                if (url != null) {
//                    return new CommonResult(200, "上传成功", url);
//                } else {
//                    return CommonResult.error("上传失败");
//                }
//            } catch (Exception e) {
//                log.error("文件上传失败", e);
//                return CommonResult.error("上传失败: " + e.getMessage());
//            }
//        }
        return CommonResult.error("文件不存在");
    }
}
