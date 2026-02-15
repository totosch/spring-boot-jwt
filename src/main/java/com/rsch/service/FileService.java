package com.rsch.service;

import com.rsch.dto.FileResponse;
import com.rsch.exception.FileEntityNotFoundException;
import com.rsch.model.User;
import com.rsch.model.UserFile;
import com.rsch.repository.UserFileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    private final UserFileRepository userFileRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public FileService(UserFileRepository userFileRepository, KafkaTemplate<String, String> kafkaTemplate, S3Client s3Client) {
        this.userFileRepository = userFileRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.s3Client = s3Client;
    }

    public FileResponse uploadFile(MultipartFile file, User user) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileType = file.getContentType();
        Long fileSize = file.getSize();

        String s3Key = UUID.randomUUID() + "_" + originalFilename;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType(fileType)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), fileSize));

        UserFile fileToSave = new UserFile(
                originalFilename,
                fileType,
                fileSize,
                s3Key,
                user
        );

        UserFile savedFile = userFileRepository.save(fileToSave);

        kafkaTemplate.send("upload-file-topic", user.getEmail());
        System.out.println("kafka sending email to: " + user.getEmail());

        return new FileResponse(
                savedFile.getId(),
                savedFile.getFilename(),
                savedFile.getFileType(),
                savedFile.getSize(),
                savedFile.getPath()
        );
    }

    public Page<FileResponse> getAllUserFiles(User user, Pageable pageable){
        return userFileRepository.findAllByUser(user, pageable)
                .map(file -> new FileResponse(
                        file.getId(),
                        file.getFilename(),
                        file.getFileType(),
                        file.getSize(),
                        file.getPath()
                ));
    }

    @Cacheable(value = "files", key = "#id")
    public UserFile getFileEntity(Integer id) {
        System.out.println("got the file from the db with id " + id);
        return userFileRepository.findById(id)
                .orElseThrow(() -> new FileEntityNotFoundException("file not found with id: " + id));
    }

    public Resource loadFileAsResource(String s3Key) throws FileNotFoundException {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            return new InputStreamResource(s3Client.getObject(getObjectRequest));

        } catch (NoSuchKeyException e) {
            throw new FileNotFoundException("file not found in S3 bucket: " + s3Key);
        }
    }

    public void deleteFile(Integer id, String s3Key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();
        s3Client.deleteObject(deleteObjectRequest);

        userFileRepository.deleteById(id);
    }
}