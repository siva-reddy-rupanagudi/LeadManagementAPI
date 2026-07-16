package com.belenits.leadmanagementapi.entity;

import com.belenits.leadmanagementapi.enums.CourseModes;
import com.belenits.leadmanagementapi.enums.EnquiryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enquires {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String phone;
    @ManyToOne
    @JoinColumn(name = "counsellor_id")
    private Counsellors counsellors;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Courses course;
    @Enumerated(EnumType.STRING)
    private CourseModes courseModes;
    @Enumerated(EnumType.STRING)
    private EnquiryStatus status = EnquiryStatus.OPEN;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
