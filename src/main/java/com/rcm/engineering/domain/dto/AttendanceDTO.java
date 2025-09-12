package com.rcm.engineering.domain.dto;

import com.rcm.engineering.domain.Attendance;

public class AttendanceDTO {

    private Attendance attendance;
    private String totalHours;

    public AttendanceDTO(Attendance attendance, String totalHours) {
        this.attendance = attendance;
        this.totalHours = totalHours;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public String getTotalHours() {
        return totalHours;
    }
}
