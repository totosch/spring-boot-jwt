package com.rsch.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitingService {

    private final ProxyManager<byte[]> proxyManager;

    public RateLimitingService(ProxyManager<byte[]> proxyManager) {
        this.proxyManager = proxyManager;
    }

    public Bucket resolveBucket(String userEmail) {
        byte[] key = userEmail.getBytes();

        BucketConfiguration configuration = BucketConfiguration.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(5)
                        .refillIntervally(5, Duration.ofMinutes(1))
                        .build())
                .build();

        return proxyManager.builder().build(key, configuration);
    }
}