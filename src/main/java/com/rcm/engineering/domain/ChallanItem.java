package com.rcm.engineering.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "challan_item")
public class ChallanItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String refChNo;
    private String weight;
    private int quantity;

    private double piecesPerKg;
    private double ratePerPiece;
    private int totalPieces;
    private double totalAmount;
    @ManyToOne
    @JoinColumn(name = "challan_id")
    @JsonBackReference
    private Challan challan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRefChNo() {
        return refChNo;
    }

    public void setRefChNo(String refChNo) {
        this.refChNo = refChNo;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPiecesPerKg() {
        return piecesPerKg;
    }

    public void setPiecesPerKg(double piecesPerKg) {
        this.piecesPerKg = piecesPerKg;
    }

    public double getRatePerPiece() {
        return ratePerPiece;
    }

    public void setRatePerPiece(double ratePerPiece) {
        this.ratePerPiece = ratePerPiece;
    }

    public int getTotalPieces() {
        return totalPieces;
    }

    public void setTotalPieces(int totalPieces) {
        this.totalPieces = totalPieces;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Challan getChallan() {
        return challan;
    }

    public void setChallan(Challan challan) {
        this.challan = challan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChallanItem item = (ChallanItem) o;
        return quantity == item.quantity && Double.compare(piecesPerKg, item.piecesPerKg) == 0 && Double.compare(ratePerPiece, item.ratePerPiece) == 0 && totalPieces == item.totalPieces && Double.compare(totalAmount, item.totalAmount) == 0 && Objects.equals(id, item.id) && Objects.equals(description, item.description) && Objects.equals(refChNo, item.refChNo) && Objects.equals(weight, item.weight) && Objects.equals(challan, item.challan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, refChNo, weight, quantity, piecesPerKg, ratePerPiece, totalPieces, totalAmount, challan);
    }

    @Override
    public String toString() {
        return "ChallanItem{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", refChNo='" + refChNo + '\'' +
                ", weight='" + weight + '\'' +
                ", quantity=" + quantity +
                ", piecesPerKg=" + piecesPerKg +
                ", ratePerPiece=" + ratePerPiece +
                ", totalPieces=" + totalPieces +
                ", totalAmount=" + totalAmount +
                ", challan=" + challan +
                '}';
    }
}
