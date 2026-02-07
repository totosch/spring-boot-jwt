package com.rsch.repository;

import com.rsch.dto.FileResponse;
import com.rsch.model.User;
import com.rsch.model.UserFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

public interface UserFileRepository extends JpaRepository<UserFile, Integer> {
    Page<UserFile> findAllByUser(User user, Pageable pageable);
}
