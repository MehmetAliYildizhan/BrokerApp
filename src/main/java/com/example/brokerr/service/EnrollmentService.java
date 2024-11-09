package com.example.brokerr.service;
import com.example.brokerr.model.Employee;
import com.example.brokerr.model.Customer;
import com.example.brokerr.model.LoginRequest;
import com.example.brokerr.model.LoginResponse;
import com.example.brokerr.model.ReturnHeader;
import com.example.brokerr.repository.CustomerRepository;
import com.example.brokerr.repository.EnrollmentRepository;
import com.example.brokerr.util.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnrollmentService {

    ReturnMessageHandler RMHelper = new ReturnMessageHandler();
    JWTUtil jwtUtil = new JWTUtil();

    private final EnrollmentRepository enrollmentRepository;
    private final CustomerRepository customerRepository;
    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, CustomerRepository customerRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.customerRepository = customerRepository;
    }
    
    @Transactional
    public Employee addEmployee(Employee emp) {

        Long maxId = enrollmentRepository.findMaxId();
        Long newId = (maxId == -1) ? 0 : maxId + 1;

        Employee employee = new Employee(newId, emp.getUsername(), Base64Util.encode(emp.getPassword()));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        employee.setDateAdded(dtf.format(now));

        if(emp.getEmploymentType()== null){
            employee.setEmploymentType(Constants.EmploymentType.BROKER);
            employee.setAdmin(Constants.NO_FLAG);
        }else{
            employee.setEmploymentType(Constants.EmploymentType.ADMIN);
            employee.setAdmin(Constants.YES_FLAG);
        }
        employee.setFailedTryCount(0);
        Employee cemployee = enrollmentRepository.save(employee);
        if(cemployee!=null) {
            LogUtil.writeLog(Constants.LogType.INFO, ErrorConstants.SUCSESSFULL, "Employee created. EmployeeId: " + cemployee.getId());
            return cemployee;
        }
        return null;
    }

    @Transactional  // Bu işlem sadece okuma amaçlı
    public List<Employee> getAllEmployeesDecoded() {
        List<Employee> empList = new ArrayList<>();
        List<Employee> toReturn = new ArrayList<>();
        empList = enrollmentRepository.getAllEmployees();
        for(int i=0;i<empList.size();i++){
           String decodedPassword = Base64Util.decode(empList.get(i).getPassword());
           Employee toSend = new Employee(empList.get(i).getId(),empList.get(i).getUsername(),decodedPassword);
           toSend.setEmploymentType(empList.get(i).getEmploymentType());
           toSend.setAdmin(empList.get(i).getAdmin());
           toReturn.add(toSend);
        }
        LogUtil.writeLog(Constants.LogType.INFO, ErrorConstants.SUCSESSFULL, "Decoded employee listed. Employee count: " + toReturn.size());
        return toReturn;
    }
    @Transactional  // Bu işlem sadece okuma amaçlı
    public List<Employee>getAllEmployees(){
        return enrollmentRepository.getAllEmployees();
    }

    @Transactional
    public Employee getById(Long id) {
        return enrollmentRepository.getById(id);
    }

    @Transactional
    public Employee getByUsername(String username) {
        return enrollmentRepository.getByUsername(username);
    }

    @Transactional
    public Employee getCustomerByUsername(String username) {
        return enrollmentRepository.getByUsername(username);
    }

    @Transactional
    public void deleteUser(Long id) {
        enrollmentRepository.deleteById(id);
    }

    public LoginResponse login(LoginRequest req){

        Employee employee = new Employee();
        employee.setUsername(req.getUsername());
        employee.setPassword(req.getPassword());

        ReturnHeader rh = new ReturnHeader();
        LoginResponse res = new LoginResponse();
        if (employee.getUsername().isEmpty() || employee.getPassword().isEmpty()){
            rh = RMHelper.getReturnHeader(ErrorConstants.MISSING_LOGIN_INFO);
        }
        Employee emp = getByUsername(employee.getUsername());
        if(emp.getFailedTryCount() > Constants.MAX_FAILED_LOGIN_TRY){
            rh = RMHelper.getReturnHeader(ErrorConstants.BLOCKED_ACCOUND);
        }
        emp.setPassword(Base64Util.decode(emp.getPassword()));
        if(emp.getPassword().equals(employee.getPassword()) ||
                emp.getEmploymentType().equals(Constants.EmploymentType.ADMIN)){
            rh = RMHelper.getReturnHeader(ErrorConstants.SUCSESSFULL);
        }else{
            rh = RMHelper.getReturnHeader(ErrorConstants.WRONG_LOGIN_INFO);
        }
        res.setHeader(rh);
        if(rh.getReturnCode()== ErrorConstants.SUCSESSFULL)
            res.setEmpToken(jwtUtil.generateToken(emp.getId().toString()));
        else
            res.setEmpToken("");

        return res;
    }

    public LoginResponse customerLogin(LoginRequest req){

        Customer customer = new Customer();
        customer.setName(req.getUsername());
        customer.setPassword(req.getPassword());

        ReturnHeader rh = new ReturnHeader();
        LoginResponse res = new LoginResponse();
        if (customer.getName().isEmpty() || customer.getPassword().isEmpty()){
            rh = RMHelper.getReturnHeader(ErrorConstants.MISSING_LOGIN_INFO);
        }

        Customer cus = customerRepository.findByName(customer.getName());
        if(cus.getPassword() == customer.getPassword()){
            rh.setReturnCode(ErrorConstants.SUCSESSFULL);
        }else
            rh.setReturnCode(ErrorConstants.WRONG_LOGIN_INFO);
        res.setHeader(rh);
        if(rh.getReturnCode()== ErrorConstants.SUCSESSFULL)
            res.setEmpToken(jwtUtil.generateToken(cus.getId().toString()));
        else
            res.setEmpToken("");

        return res;
    }
}
