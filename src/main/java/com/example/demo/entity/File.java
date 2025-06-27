package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Entity
@Getter
@Setter
@ToString
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contentType;

    private long size;

    @Column(nullable = false, unique = true)
    private String path;

    @Column(nullable = false)
    private Instant uploadedAt;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
}
