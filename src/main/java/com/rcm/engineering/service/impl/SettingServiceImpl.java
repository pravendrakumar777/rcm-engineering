package com.rcm.engineering.service.impl;

import com.rcm.engineering.domain.Setting;
import com.rcm.engineering.domain.dto.SettingDTO;
import com.rcm.engineering.mapper.SettingMapper;
import com.rcm.engineering.repository.SettingRepository;
import com.rcm.engineering.service.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SettingServiceImpl implements SettingService {
    private static final Logger log = LoggerFactory.getLogger(SettingServiceImpl.class);
    private final SettingRepository settingRepository;

    public SettingServiceImpl(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @Override public SettingDTO getByKey(String key) {
        log.info("Settings | GetByKey | key: {}", key);
        Setting s = settingRepository.findByKey(key) .orElseThrow(() -> new EntityNotFoundException("Setting not found for key: " + key));
        return SettingMapper.toDto(s);
    }

    @Override
    public List<SettingDTO> list(String category) {
        log.info("Settings | List | category: {}", category);

        List<Setting> settings = (category == null || category.trim().isEmpty())
                ? settingRepository.findByActiveTrue()
                : settingRepository.findByCategoryAndActiveTrue(category);
        return settings.stream()
                .map(SettingMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override public void upsert(SettingDTO dto) {
        log.info("Settings | Upsert | key: {} | category: {} | active: {}", dto.getKey(), dto.getCategory(), dto.isActive());
        Setting s = settingRepository.findByKey(dto.getKey()).orElseGet(Setting::new);
        SettingMapper.toEntity(dto, s);
        s.setKey(dto.getKey()); // ensure key is set for new entity
        settingRepository.save(s); log.info("Settings | Upsert | key:{} | status:SUCCESS", dto.getKey());
    }

    @Override
    public void deactivate(String key) {
        log.info("Settings | Deactivate | key: {}", key);
        Setting s = settingRepository.findByKey(key)
                .orElseThrow(() -> new EntityNotFoundException("Setting not found for key: " + key));
        s.setActive(false);
        settingRepository.save(s);
        log.info("Settings | Deactivate | key: {} | status: SUCCESS", key);
    }
}
