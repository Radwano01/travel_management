package com.hackathon.backend.utilities.amazonServices;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String BUCKET_NAME;

    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try(FileOutputStream fos = new FileOutputStream(convertedFile)){
            fos.write(file.getBytes());
        }
        return convertedFile;
    }

    public String uploadFile(MultipartFile file) {
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

    public void deleteFile(String fileName){
        try{
            amazonS3.deleteObject(new DeleteObjectRequest(BUCKET_NAME, fileName));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteFiles(String[] fileNames){
        try{
            for(String fileName:fileNames){
                amazonS3.deleteObject(new DeleteObjectRequest(BUCKET_NAME, fileName));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
