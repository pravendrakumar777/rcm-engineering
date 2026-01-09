package com.rcm.engineering.resource.rest;

import com.rcm.engineering.domain.dto.SettingDTO;
import com.rcm.engineering.service.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/settings")
public class SettingController {
    private static final Logger log = LoggerFactory.getLogger(SettingController.class);
    private final SettingService settingService;
    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping("/{key}")
    public ResponseEntity<SettingDTO> get(@PathVariable String key) {
        log.info("Settings | Fetch | INIT | key: {}",
                key);
        try {
            SettingDTO dto = settingService.getByKey(key);
            log.info("Settings | Fetch | SUCCESS | key: {} | category: {} | active: {}",
                    key, dto.getCategory(), dto.isActive());
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException nf) {
            log.warn("Settings | Fetch | NOT_FOUND | key: {} | message: {}",
                    key, nf.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            log.error("Settings | Fetch | ERROR | key: {} | message: {}",
                    key, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<SettingDTO>> list(@RequestParam(required = false) String category) {
        log.info("Settings | List | INIT | category: {}",
                category);
        try {
            List<SettingDTO> list = settingService.list(category);
            log.info("Settings | List | SUCCESS | category: {} | count: {}",
                    category, list.size());
            return ResponseEntity.ok(list);
        } catch (Exception ex) {
            log.error("Settings | List | ERROR | category: {} | message: {}",
                    category, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{key}")
    public ResponseEntity<Void> upsert(@PathVariable String key, @RequestBody SettingDTO dto) {
        log.info("Settings | Upsert | INIT | key: {}",
                key);
        try {
            dto.setKey(key);
            settingService.upsert(dto);
            log.info("Settings | Upsert | SUCCESS | key: {}",
                    key);
            return ResponseEntity.accepted().build();
        } catch (Exception ex) {
            log.error("Settings | Upsert | ERROR | key: {} | message: {}",
                    key, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> deactivate(@PathVariable String key) {
        log.info("Settings | Deactivate | INIT | key: {}", key);
        try {
            settingService.deactivate(key);
            log.info("Settings | Deactivate | SUCCESS | key: {}", key);
            return ResponseEntity.accepted().build();
        } catch (EntityNotFoundException nf) {
            log.warn("Settings | Deactivate | NOT_FOUND | key: {} | message: {}",
                    key, nf.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            log.error("Settings | Deactivate | ERROR | key: {} | message: {}",
                    key, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
