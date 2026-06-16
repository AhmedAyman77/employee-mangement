package com.example.employee_mangement.services;

import com.example.employee_mangement.abstracts.DepartmentService;
import com.example.employee_mangement.dtos.DepartmentCreate;
import com.example.employee_mangement.entities.Department;
import com.example.employee_mangement.repositories.DepartmentRepo;
import com.example.employee_mangement.share.CustomResponseException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class DepartmentServiceImp implements DepartmentService {

    @Autowired
    private DepartmentRepo departmentRepo;

    @Override
    public Department findOne(UUID id) {
        return departmentRepo.findById(id).orElseThrow(() -> CustomResponseException.resourceNotFound("Department not found with id: " + id));
    }

    @Override
    public List<Department> findAll() {
        return departmentRepo.findAll();
    }

    @Override
    public Department create(DepartmentCreate department) {
        Department departmentEntity = new Department();
        departmentEntity.setName(department.name());

        return departmentRepo.save(departmentEntity);
    }

    @Override
    public void delete(UUID id) {
        departmentRepo.deleteById(id);
    }

}
