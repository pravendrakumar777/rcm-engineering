package com.rcm.engineering.mapper;

import com.rcm.engineering.domain.Setting;
import com.rcm.engineering.domain.dto.SettingDTO;
public class SettingMapper {
    public static SettingDTO toDto(Setting s) {
        SettingDTO dto = new SettingDTO();
        dto.setId(s.getId());
        dto.setKey(s.getKey());
        dto.setValue(s.getValue());
        dto.setCategory(s.getCategory());
        dto.setDescription(s.getDescription());
        dto.setActive(s.isActive());
        dto.setVersion(s.getVersion());
        return dto;
    }
    public static Setting toEntity(SettingDTO dto, Setting target) {
        target.setKey(dto.getKey());
        target.setValue(dto.getValue());
        target.setCategory(dto.getCategory());
        target.setDescription(dto.getDescription());
        target.setActive(dto.isActive());
        return target;
    }
}
