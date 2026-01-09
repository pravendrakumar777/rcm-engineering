package com.rcm.engineering.domain;

import javax.persistence.*;

@Entity
@Table(name = "settings")
public class Setting extends BaseAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key", nullable = false, unique = true, length = 128)
    private String key;

    @Column(name = "value", nullable = false, length = 1024)
    private String value;

    @Column(name = "category", nullable = false, length = 64)
    private String category;

    @Column(name = "description", length = 256)
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active = true;
    @Version
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
