package org.example.service.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CacheService {
    private final CacheManager cacheManager;

    public void evict(String cacheName) {
        cacheManager.getCache(cacheName).clear();
    }

    public void evict(List<String> cacheNames) {
        cacheNames.stream()
                .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }

    public void evictAll() {
        cacheManager.getCacheNames().stream()
                .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }
}
