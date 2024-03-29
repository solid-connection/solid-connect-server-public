package com.example.solidconnection.entity;

import com.example.solidconnection.type.SemesterAvailableForDispatch;
import com.example.solidconnection.type.TuitionFeeType;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Entity
@Getter
public class UniversityInfoForApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String term;

    @Column(nullable = false)
    private Integer studentCapacity;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private TuitionFeeType tuitionFeeType;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private SemesterAvailableForDispatch semesterAvailableForDispatch;

    @Column(length = 10)
    private String semesterRequirement;

    @Column(length = 1000)
    private String detailsForLanguage;

    @Column(length = 5)
    private String gpaRequirement;

    @Column(length = 5)
    private String gpaRequirementCriteria;

    @Column(length = 1000)
    private String detailsForApply;

    @Column(length = 1000)
    private String detailsForMajor;

    @Column(length = 1000)
    private String detailsForAccommodation;

    @Column(length = 1000)
    private String detailsForEnglishCourse;

    @Column(length = 500)
    private String details;

    // 연관 관계
    @OneToMany(mappedBy = "universityInfoForApply", fetch = FetchType.LAZY)
    private Set<LanguageRequirement> languageRequirements;

    @OneToOne
    @JoinColumn(name = "university_id")
    private University university;
}
