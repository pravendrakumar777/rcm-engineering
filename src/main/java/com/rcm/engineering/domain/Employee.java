package com.rcm.engineering.domain;

import com.rcm.engineering.domain.enumerations.EmployeeStatus;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "rcm_employee", uniqueConstraints = @UniqueConstraint(columnNames = "empCode"))
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String mobile;
    private String gender;
    private String email;
    private String manager;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    @Column(nullable = false, unique = true)
    private String empCode;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String department;
    private String designation;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfJoining;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfExit;
    private String panNumber;
    private String aadhaarNumber;
    private String bankName;
    private String bankAccountNumber;
    private String ifscCode;
    private Double salary;
    @Column(nullable = true, updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus status = EmployeeStatus.PENDING;

    @Column(name = "photo_url")
    private String photoUrl;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public LocalDate getDateOfExit() {
        return dateOfExit;
    }

    public void setDateOfExit(LocalDate dateOfExit) {
        this.dateOfExit = dateOfExit;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(name, employee.name) && Objects.equals(mobile, employee.mobile) && Objects.equals(gender, employee.gender) && Objects.equals(email, employee.email) && Objects.equals(manager, employee.manager) && Objects.equals(dateOfBirth, employee.dateOfBirth) && Objects.equals(empCode, employee.empCode) && Objects.equals(address, employee.address) && Objects.equals(city, employee.city) && Objects.equals(state, employee.state) && Objects.equals(postalCode, employee.postalCode) && Objects.equals(country, employee.country) && Objects.equals(department, employee.department) && Objects.equals(designation, employee.designation) && Objects.equals(dateOfJoining, employee.dateOfJoining) && Objects.equals(dateOfExit, employee.dateOfExit) && Objects.equals(panNumber, employee.panNumber) && Objects.equals(aadhaarNumber, employee.aadhaarNumber) && Objects.equals(bankName, employee.bankName) && Objects.equals(bankAccountNumber, employee.bankAccountNumber) && Objects.equals(ifscCode, employee.ifscCode) && Objects.equals(salary, employee.salary) && Objects.equals(createdAt, employee.createdAt) && status == employee.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, mobile, gender, email, manager, dateOfBirth, empCode, address, city, state, postalCode, country, department, designation, dateOfJoining, dateOfExit, panNumber, aadhaarNumber, bankName, bankAccountNumber, ifscCode, salary, createdAt, status);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", manager='" + manager + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", empCode='" + empCode + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                ", department='" + department + '\'' +
                ", designation='" + designation + '\'' +
                ", dateOfJoining=" + dateOfJoining +
                ", dateOfExit=" + dateOfExit +
                ", panNumber='" + panNumber + '\'' +
                ", aadhaarNumber='" + aadhaarNumber + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", ifscCode='" + ifscCode + '\'' +
                ", salary=" + salary +
                ", createdAt=" + createdAt +
                ", status=" + status +
                '}';
    }
}
