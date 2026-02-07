package com.rsch.controller;

import com.rsch.dto.FileResponse;
import com.rsch.dto.UserResponse;
import com.rsch.model.Role;
import com.rsch.model.User;
import com.rsch.model.UserFile;
import com.rsch.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class UserFileController {

    private final FileService fileService;

    public UserFileController(FileService fileService) { this.fileService = fileService; }

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> uploadFile(@RequestParam("file") MultipartFile file, Authentication authentication) throws IOException {

        User user = (User) authentication.getPrincipal();

        FileResponse response = fileService.uploadFile(file, user);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<FileResponse>> getUploadedFiles(Authentication authentication, @PageableDefault(size = 10, page = 0, sort = "id") Pageable pageable) {

        User user = (User) authentication.getPrincipal();

        Page<FileResponse> page = fileService.getAllUserFiles(user, pageable);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer id, Authentication authentication) throws IOException {

        User user = (User) authentication.getPrincipal();

        UserFile userFile = fileService.getFileEntity(id);

        boolean isOwner = userFile.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new SecurityException("user not allowed to download this file");
        }

        Resource resource = fileService.loadFileAsResource(userFile.getPath());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(userFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + userFile.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Integer id, Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();

        UserFile userFile = fileService.getFileEntity(id);

        boolean isOwner = userFile.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new SecurityException("user not allowed to delete the file");
        }

        fileService.deleteFile(id, userFile.getPath());

        return ResponseEntity.noContent().build();
    }

}
