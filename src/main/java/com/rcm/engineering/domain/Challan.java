package com.rcm.engineering.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    private String customerName;
    @OneToMany(mappedBy = "challan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JsonIgnore
    private List<ChallanItem> items = new ArrayList<>();

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Challan challan = (Challan) o;
        return Objects.equals(id, challan.id) && Objects.equals(challanNo, challan.challanNo) && Objects.equals(date, challan.date) && Objects.equals(customerName, challan.customerName) && Objects.equals(items, challan.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, challanNo, date, customerName, items);
    }

    @Override
    public String toString() {
        return "Challan{" +
                "id=" + id +
                ", challanNo='" + challanNo + '\'' +
                ", date=" + date +
                ", customerName='" + customerName + '\'' +
                ", items=" + items +
                '}';
    }
}
