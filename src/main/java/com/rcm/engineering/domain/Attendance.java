package com.rcm.engineering.domain;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendances", uniqueConstraints = @UniqueConstraint(columnNames = {"employee_ohr", "date"}))
public class Attendance implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Status { PRESENT, ABSENT }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_ohr", referencedColumnName = "ohr")
    private Employee employee;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm a")
    @Column(name = "check_in_datetime")
    private LocalDateTime checkInDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm a")
    @Column(name = "check_out_datetime")
    private LocalDateTime checkOutDateTime;

    public Attendance() { }

    public Attendance(Employee employee, LocalDate date, Status status, LocalDateTime checkInDateTime, LocalDateTime checkOutDateTime) {
        this.employee = employee;
        this.date = date;
        this.status = status;
        this.checkInDateTime = checkInDateTime;
        this.checkOutDateTime = checkOutDateTime;
    }

    @Transient
    public Duration getTotalDuration() {
        if (checkInDateTime != null && checkOutDateTime != null) {
            return Duration.between(checkInDateTime, checkOutDateTime);
        }
        return null;
    }

    public String getFormattedTotalHours() {
        if (checkInDateTime == null || checkOutDateTime == null) {
            return "0 Hrs 0 Mins";
        }
        long minutes = Duration.between(checkInDateTime, checkOutDateTime).toMinutes();
        long hrs = minutes / 60;
        long mins = minutes % 60;
        return hrs + " Hrs " + mins + " Mins";
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCheckInDateTime() {
        return checkInDateTime;
    }

    public void setCheckInDateTime(LocalDateTime checkInDateTime) {
        this.checkInDateTime = checkInDateTime;
    }

    public LocalDateTime getCheckOutDateTime() {
        return checkOutDateTime;
    }

    public void setCheckOutDateTime(LocalDateTime checkOutDateTime) {
        this.checkOutDateTime = checkOutDateTime;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", employee=" + employee +
                ", date=" + date +
                ", status=" + status +
                ", checkInDateTime=" + checkInDateTime +
                ", checkOutDateTime=" + checkOutDateTime +
                '}';
    }
}
