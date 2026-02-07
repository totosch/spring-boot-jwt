package com.rsch.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_file")
public class UserFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String filename;
    private String fileType;
    private Long size;
    private String path;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public UserFile() {
    }

    public UserFile(String filename, String fileType, Long size, String path, User user) {
        this.filename = filename;
        this.fileType = fileType;
        this.size = size;
        this.path = path;
        this.user = user;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() { return id; }
}
