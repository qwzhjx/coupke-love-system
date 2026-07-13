package com.love.couplelovesystem.module.admin.controller;

import com.love.couplelovesystem.common.utils.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传控制器 - 支持语音、图片上传
 */
@RestController
@RequestMapping("/api/admin")
public class FileUploadController {

    @Value("${file.upload.voices}")
    private String voicesPath;

    @Value("${file.upload.materials}")
    private String materialsPath;

    /**
     * 上传语音文件（MP3/M4A/WAV）
     */
    @PostMapping("/upload/voice")
    public R uploadVoice(@RequestParam("file") MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            if (originalName == null) return R.error("文件名不能为空");
            String ext = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
            if (!ext.matches("\\.(mp3|m4a|wav|ogg|aac|mp4|webm|mov|avi)")) {
                return R.error("文件格式不支持，支持音频：MP3/M4A/WAV 或视频：MP4/WEBM/MOV");
            }
            if (file.getSize() > 50 * 1024 * 1024) {
                return R.error("文件不能超过50MB");
            }

            File dir = new File(voicesPath);
            if (!dir.exists()) dir.mkdirs();

            String fileName = UUID.randomUUID().toString().replace("-", "") + ext;
            File dest = new File(dir, fileName);
            file.transferTo(dest);

            Map<String, String> result = new HashMap<>();
            result.put("url", "/upload/voices/" + fileName);
            result.put("name", originalName);
            return R.ok("上传成功", result);
        } catch (Exception e) {
            return R.error("上传失败: " + e.getMessage());
        }
    }

    /**
     * 上传素材图片
     */
    @PostMapping("/upload/material")
    public R uploadMaterial(@RequestParam("file") MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            if (originalName == null) return R.error("文件名不能为空");
            String ext = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
            if (!ext.matches("\\.(jpg|jpeg|png|gif|webp)")) {
                return R.error("图片格式不支持，支持：JPG/PNG/GIF/WEBP");
            }

            File dir = new File(materialsPath);
            if (!dir.exists()) dir.mkdirs();

            String fileName = UUID.randomUUID().toString().replace("-", "") + ext;
            File dest = new File(dir, fileName);
            file.transferTo(dest);

            Map<String, String> result = new HashMap<>();
            result.put("url", "/upload/materials/" + fileName);
            result.put("name", originalName);
            return R.ok("上传成功", result);
        } catch (Exception e) {
            return R.error("上传失败: " + e.getMessage());
        }
    }
}
