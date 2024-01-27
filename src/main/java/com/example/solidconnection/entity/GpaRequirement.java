package com.example.solidconnection.entity;

import jakarta.persistence.*;

@Entity
public class GpaRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 5)
    private String scale;

    @Column(nullable = false)
    private Float minGpa;

    // 연관 관계
    @ManyToOne
    @JoinColumn(name = "university_id")
    private University university;
}