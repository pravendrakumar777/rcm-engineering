package com.rcm.engineering.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDate;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String customerName;
    @OneToMany(mappedBy = "challan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ChallanItem> items = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (date == null) {
            this.date = LocalDate.now();
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
        if (this == o) return true;
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
