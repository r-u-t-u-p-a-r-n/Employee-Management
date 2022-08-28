package site.operations.Repositories;

import site.operations.Models.EmployeeData;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepo extends JpaRepository<EmployeeData,String> {}