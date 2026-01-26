package com.niiran.software.solutions.service;

import com.niiran.software.solutions.domain.dto.SettingDTO;

import java.util.List;

public interface SettingService {
    SettingDTO getByKey(String key);
    List<SettingDTO> list(String category);
    void upsert(SettingDTO dto); // create or update by key
    void deactivate(String key);
}
