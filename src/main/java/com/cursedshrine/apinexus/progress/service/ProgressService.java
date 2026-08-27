package com.cursedshrine.apinexus.progress.service;

import com.cursedshrine.apinexus.progress.dto.ProgressRequestDto;
import com.cursedshrine.apinexus.progress.entity.ProgressEntity;
import com.cursedshrine.apinexus.progress.manager.ProgressManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ProgressManager manager;

    public Map<String, Object> save(ProgressRequestDto dto, String authorization) {
        ProgressEntity entity = ProgressEntity.builder()
                .show(dto.getShow())
                .path(dto.getPath())
                .position(dto.getPosition())
                .duration(dto.getDuration())
                .finished(dto.getFinished())
                .build();
        return manager.save(entity, authorization);
    }

    public Map<String, Object> list(String show, String authorization) {
        return manager.list(show, authorization);
    }
}
