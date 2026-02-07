package com.rsch.dto;

public record FileResponse(
        Integer id,
        String filename,
        String fileType,
        Long size,
        String path) {
}