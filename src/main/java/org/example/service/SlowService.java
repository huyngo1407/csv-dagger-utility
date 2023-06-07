package org.example.service;

import org.springframework.stereotype.Service;

@Service
public class SlowService {
    public void simulate(Long second) {
        try {
            Thread.sleep(second);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
