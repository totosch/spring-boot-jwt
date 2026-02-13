package com.rsch.service;

import com.rsch.dto.FileResponse;
import com.rsch.exception.FileEntityNotFoundException;
import com.rsch.model.User;
import com.rsch.model.UserFile;
import com.rsch.repository.UserFileRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    private final UserFileRepository userFileRepository;
    private final String FOLDER_PATH = "/home/schillaci/Desktop/FilesPath/";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public FileService(UserFileRepository userFileRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.userFileRepository = userFileRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public FileResponse uploadFile(MultipartFile file, User user) throws IOException {
        Files.createDirectories(Paths.get(FOLDER_PATH));
        String nameOfFileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        Long fileSize = file.getSize();


        String filePath = FOLDER_PATH + nameOfFileName;
        file.transferTo(new File(filePath));

        UserFile fileToSave = new UserFile(
                nameOfFileName,
                fileType,
                fileSize,
                filePath,
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

    public UserFile getFileEntity(Integer id) {
        return userFileRepository.findById(id)
                .orElseThrow(() -> new FileEntityNotFoundException("file not found with id: " + id));
    }

    public Resource loadFileAsResource(String filePath) throws MalformedURLException, FileNotFoundException {
        Path path = Paths.get(filePath);
        Resource resource = new UrlResource(path.toUri());

        if(resource.exists()) {
            return resource;
        } else {
            throw new FileNotFoundException("file not found: " + filePath);
        }
    }

    public void deleteFile(Integer id, String filePath) throws IOException {
        Files.deleteIfExists(Paths.get(filePath));

        userFileRepository.deleteById(id);
    }

}
