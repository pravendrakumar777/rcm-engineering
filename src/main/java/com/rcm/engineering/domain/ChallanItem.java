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
    private String weight;
    private int quantity;

    private double piecesPerKg;
    private double ratePerPiece;
    private int totalPieces;
    private double totalAmount;

    private String process;
    @Column(name = "hsn_code")
    private String hsnCode;
    private String unit;

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

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Challan getChallan() {
        return challan;
    }

    public void setChallan(Challan challan) {
        this.challan = challan;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChallanItem that = (ChallanItem) o;
        return quantity == that.quantity && Double.compare(piecesPerKg, that.piecesPerKg) == 0 && Double.compare(ratePerPiece, that.ratePerPiece) == 0 && totalPieces == that.totalPieces && Double.compare(totalAmount, that.totalAmount) == 0 && Objects.equals(id, that.id) && Objects.equals(description, that.description) && Objects.equals(weight, that.weight) && Objects.equals(process, that.process) && Objects.equals(hsnCode, that.hsnCode) && Objects.equals(unit, that.unit) && Objects.equals(challan, that.challan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, weight, quantity, piecesPerKg, ratePerPiece, totalPieces, totalAmount, process, hsnCode, unit, challan);
    }

    @Override
    public String toString() {
        return "ChallanItem{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", weight='" + weight + '\'' +
                ", quantity=" + quantity +
                ", piecesPerKg=" + piecesPerKg +
                ", ratePerPiece=" + ratePerPiece +
                ", totalPieces=" + totalPieces +
                ", totalAmount=" + totalAmount +
                ", process='" + process + '\'' +
                ", hsnCode='" + hsnCode + '\'' +
                ", unit='" + unit + '\'' +
                ", challan=" + challan +
                '}';
    }
}
