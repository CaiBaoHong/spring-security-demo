package com.abc.web.controller;


import com.abc.model.FileInfo;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

@RestController
@RequestMapping("/file")
public class FileController {

    private String folder = "D:\\demo";

    @PostMapping
    public FileInfo upload(MultipartFile file) throws IOException {
        System.out.println("File upload, file.name: " + file.getName());
        System.out.println("File upload, file.originalFilename: " + file.getOriginalFilename());
        System.out.println("File upload, file.size: " + file.getSize());

        File localFile = new File(folder, new Date().getTime() + ".txt");
        file.transferTo(localFile);
        return new FileInfo(localFile.getAbsolutePath());
    }

    @GetMapping("/{id}")
    public void download(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String folder = "D:\\demo";
        try (InputStream is = new FileInputStream(new File(folder, id + ".txt"));
             OutputStream os = response.getOutputStream()) {

            response.setContentType("applocation/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=download-test.txt");
            IOUtils.copy(is, os);
            os.flush();
        } catch (Exception e) {

        }

    }

}
