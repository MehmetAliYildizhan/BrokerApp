package com.example.brokerr.controller;
import com.example.brokerr.model.Employee;
import com.example.brokerr.model.LoginRequest;
import com.example.brokerr.model.LoginResponse;
import com.example.brokerr.model.ReturnHeader;
import com.example.brokerr.service.EnrollmentService;
import com.example.brokerr.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    ReturnMessageHandler RMHelper = new ReturnMessageHandler();
    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/addEmployee")
    public ResponseEntity<String> addEmployee(@RequestBody Employee employee) {

        long startTime = System.currentTimeMillis();
        LogUtil.writeServiceLog(Thread.currentThread().getStackTrace()[1].getMethodName(),Constants.LogType.REQUEST,null,employee.toString());

        Employee addedEmployee = enrollmentService.addEmployee(employee);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        LogUtil.writeServiceLog(Thread.currentThread().getStackTrace()[1].getMethodName(),Constants.LogType.RESPONSE,duration,"");

        return ResponseEntity.ok("Employee added successfully with ID: " + addedEmployee.getId());
    }

    @GetMapping("/listEmployee")
    public ResponseEntity<List<Employee>> getEmployeeList() {
        long startTime = System.currentTimeMillis();
        LogUtil.writeServiceLog(Thread.currentThread().getStackTrace()[1].getMethodName(),Constants.LogType.REQUEST,null,"");

        List<Employee> employees = enrollmentService.getAllEmployees();
        LogUtil.writeLog(Constants.LogType.INFO, ErrorConstants.SUCSESSFULL, "Employee listed. Employee count: " + employees.size());
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        LogUtil.writeServiceLog(Thread.currentThread().getStackTrace()[1].getMethodName(),Constants.LogType.RESPONSE,duration,employees.toString());

        return ResponseEntity.ok(employees);
    }

    @GetMapping("/listEmployeeDecoded")
    public ResponseEntity<List<Employee>> getAllEmployeesDecoded() {
        long startTime = System.currentTimeMillis();
        LogUtil.writeServiceLog(Thread.currentThread().getStackTrace()[1].getMethodName(),Constants.LogType.REQUEST,null,"");

        List<Employee> employees = enrollmentService.getAllEmployeesDecoded();
        LogUtil.writeLog(Constants.LogType.INFO, ErrorConstants.SUCSESSFULL, "Decoded employee listed. Employee count: " + employees.size());
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        LogUtil.writeServiceLog(Thread.currentThread().getStackTrace()[1].getMethodName(),Constants.LogType.RESPONSE,duration,employees.toString());

        return ResponseEntity.ok(employees);
    }

    @GetMapping("/getEmployeeById/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = enrollmentService.getById(id);
        if (employee != null) {
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build(); // Bulunamazsa 404 döner
        }
    }

    @PostMapping("/getEmployeeByUsername")
    public ResponseEntity<Employee> getEmployeeByUsername(@RequestBody Employee employee) {
        employee = enrollmentService.getByUsername(employee.getUsername());
        if (employee != null) {
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build(); // Bulunamazsa 404 döner
        }
    }

    @DeleteMapping("/deleteEmployeeById/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        enrollmentService.deleteUser(id);
        return ResponseEntity.ok("Employee with ID " + id + " has been deleted.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        LoginResponse res = enrollmentService.login(req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/customerLogin")
    public ResponseEntity<LoginResponse> customerLogin(@RequestBody LoginRequest req) {
        LoginResponse res = enrollmentService.customerLogin(req);
        return ResponseEntity.ok(res);
    }
}
