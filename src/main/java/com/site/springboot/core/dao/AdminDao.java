package com.site.springboot.core.dao;

import com.site.springboot.core.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AdminDao extends JpaRepository<Admin, Long>, QuerydslPredicateExecutor<Admin> {

    Admin findAdminByAdminId(Long adminId);

    Admin findAdminByLoginNameAndLoginPassword(String userName, String password);

    Admin findAdminByLoginName(String userName);
}