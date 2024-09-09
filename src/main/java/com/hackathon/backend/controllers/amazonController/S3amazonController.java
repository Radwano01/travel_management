package com.hackathon.backend.controllers.amazonController;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping(path = "${BASE_API}")
public class S3amazonController {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String BUCKET_NAME;

    @GetMapping(path = "/index")
    public ResponseEntity<String> getIndexHealthy(){
        return ResponseEntity.ok("Application healthy");
    }

    @PostMapping("${UPLOAD_IMAGE_API}")
    public String uploadFileApi(@RequestParam("file") MultipartFile file) {
        String fileName = "";
        try {
            File convertedFile = convertMultiPartFileToFile(file);
            fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            amazonS3.putObject(new PutObjectRequest(BUCKET_NAME, fileName, convertedFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            convertedFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try(FileOutputStream fos = new FileOutputStream(convertedFile)){
            fos.write(file.getBytes());
        }
        return convertedFile;
    }
}