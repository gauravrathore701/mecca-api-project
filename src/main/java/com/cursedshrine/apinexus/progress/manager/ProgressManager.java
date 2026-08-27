package com.cursedshrine.apinexus.progress.manager;

import com.cursedshrine.apinexus.progress.adapter.ProgressAdapter;
import com.cursedshrine.apinexus.progress.entity.ProgressEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProgressManager {

    private final ProgressAdapter adapter;

    public Map<String, Object> save(ProgressEntity entity, String authorization) {
        return adapter.save(entity, authorization);
    }

    public Map<String, Object> list(String show, String authorization) {
        return adapter.list(show, authorization);
    }
}
