package com.niiran.software.solutions.repository;

import com.niiran.software.solutions.domain.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
    Optional<Setting> findByKey(String key);
    List<Setting> findByCategoryAndActiveTrue(String category);
    List<Setting> findByActiveTrue();
}
