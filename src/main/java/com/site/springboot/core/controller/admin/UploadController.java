package com.site.springboot.core.controller.admin;

import com.site.springboot.core.config.Constants;
import com.site.springboot.core.util.Result;
import com.site.springboot.core.util.ResultGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin")
public class UploadController {

    @PostMapping({"/upload/file"})
    @ResponseBody
    public Result upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return ResultGenerator.genFailResult("请选择文件");
        }
        String fileName = file.getOriginalFilename();
        String suffixName = null;
        if (fileName != null) {
            suffixName = fileName.substring(fileName.lastIndexOf("."));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        String newFileName = sdf.format(new Date()) + r.nextInt(100) + suffixName;
        try {
            String filePath = ResourceUtils.getURL(Constants.FILE_UPLOAD_PATH).getPath();
            File fileDirectory = new File(filePath, newFileName);
            if (!fileDirectory.getParentFile().exists()) {
                fileDirectory.getParentFile().mkdirs();
            }
            file.transferTo(fileDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Result result = ResultGenerator.genSuccessResult();
        result.setData("/files/" + newFileName);
        return result;
    }

}
