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


}
