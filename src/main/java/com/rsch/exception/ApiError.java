package com.rsch.exception;

import java.time.LocalDateTime;

public record ApiError(
        String message,
        int statusCode,
        LocalDateTime timestamp
) {}