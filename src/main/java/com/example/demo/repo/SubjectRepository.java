package com.example.demo.repo;

import com.example.demo.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByNameAndUser_Username(String name, String username);
    Optional<Subject> findByIdAndUser_Username(Long id, String username);
    List<Subject> findAllByUser_Username(String username);
}
