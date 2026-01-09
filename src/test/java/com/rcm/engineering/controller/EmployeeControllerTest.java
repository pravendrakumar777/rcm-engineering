package com.rcm.engineering.controller;

import com.rcm.engineering.domain.Employee;
import com.rcm.engineering.repository.EmployeeRepository;
import com.rcm.engineering.service.AttendanceService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AttendanceService attendanceService;

    //@Test
    void shouldReturnEmployeeListView() throws Exception {
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-list"))
                .andExpect(model().attributeExists("employees"));
    }

    //@Test
    void shouldShowCreateForm() throws Exception {
        mockMvc.perform(get("/employees/form"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-form"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attribute("formTitle", "Create New Employee"));
    }

    //@Test
    public void shouldDeleteEmployee() throws Exception {
        Employee emp = new Employee();
        emp.setOhr("RCMEM12092025074409");

        Mockito.when(employeeRepository.findByOhr("RCMEM12092025074409"))
                .thenReturn(Optional.of(emp));

        mockMvc.perform(get("/employees/delete/RCMEM12092025074409"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"));
    }

    //@Test
    void shouldReturnRedirectAfterSaveEmployee() throws Exception {
        Employee emp = new Employee();
        emp.setName("New Employee");

        mockMvc.perform(post("/employees/save")
                        .flashAttr("employee", emp))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"));
    }

    //@Test
    public void shouldGetAllEmployeesViaApi() throws Exception {
        Mockito.when(employeeRepository.findAll()).thenReturn(Collections.singletonList(new Employee()));

        mockMvc.perform(get("/employees/api"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    //@Test
    public void shouldReturnSalaryCalculationView() throws Exception {
        String empCode = "RCMEM12092025074409";
        String start = "2023-09-01";
        String end = "2023-09-30";

        Employee emp = new Employee();
        emp.setOhr(empCode);
        emp.setSalary(30000.00);

        Mockito.when(employeeRepository.findByOhr(empCode)).thenReturn(Optional.of(emp));
        Mockito.when(attendanceService.getAttendance(eq(empCode), any(), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/employees/calculate-salary")
                        .param("empCode", empCode)
                        .param("start", start)
                        .param("end", end))
                .andExpect(status().isOk())
                .andExpect(view().name("calculate-salary"));
    }
}
