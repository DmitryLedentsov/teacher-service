package com.example.demo.repo;

import com.example.demo.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByNameAndSubject_IdAndUser_Username(String name, Long subjectId, String username);
    List<File> findAllBySubject_IdAndUser_Username(Long subjectId, String username);
}
