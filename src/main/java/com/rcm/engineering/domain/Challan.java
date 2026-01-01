package com.rcm.engineering.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSetter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Entity
@Table(name = "challan")
public class Challan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String challanNo;
    @Column(name = "ref_ch_no")
    private String refChNo;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    private String customerName;
    @OneToMany(mappedBy = "challan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JsonIgnore
    private List<ChallanItem> items = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.date = LocalDateTime.now();
        this.modifiedDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedDate = LocalDateTime.now();
    }

    @Transient
    private String formattedDate;
    @Transient
    private String modifiedFormatted;

    public String getFormattedDate() {
        return formattedDate;
    }
    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }
    public String getModifiedFormatted() {
        return modifiedFormatted;
    }
    public void setModifiedFormatted(String modifiedFormatted) {
        this.modifiedFormatted = modifiedFormatted;
    }

    @JsonSetter("date")
    public void setDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            this.date = null;
            return;
        }
        try {
            if (dateStr.length() == 10) {
                LocalDate localDate = LocalDate.parse(dateStr);
                this.date = localDate.atTime(LocalDateTime.now().toLocalTime());
            } else {
                this.date = LocalDateTime.parse(dateStr,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr, e);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChallanNo() {
        return challanNo;
    }

    public void setChallanNo(String challanNo) {
        this.challanNo = challanNo;
    }

    public String getRefChNo() {
        return refChNo;
    }

    public void setRefChNo(String refChNo) {
        this.refChNo = refChNo;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<ChallanItem> getItems() {
        return items;
    }

    public void setItems(List<ChallanItem> items) {
        this.items = items;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Challan challan = (Challan) o;
        return Objects.equals(id, challan.id) && Objects.equals(challanNo, challan.challanNo) && Objects.equals(refChNo, challan.refChNo) && Objects.equals(date, challan.date) && Objects.equals(customerName, challan.customerName) && Objects.equals(items, challan.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, challanNo, refChNo, date, customerName, items);
    }

    @Override
    public String toString() {
        return "Challan{" +
                "id=" + id +
                ", challanNo='" + challanNo + '\'' +
                ", refChNo='" + refChNo + '\'' +
                ", date=" + date +
                ", customerName='" + customerName + '\'' +
                ", items=" + items +
                '}';
    }
}
