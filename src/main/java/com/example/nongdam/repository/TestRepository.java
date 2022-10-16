package com.example.nongdam.repository;

import com.example.nongdam.entity.Hello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Hello, Long> {
}
