package com.divyanshu.youtube.hospitalManagement.repository;

import com.divyanshu.youtube.hospitalManagement.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}