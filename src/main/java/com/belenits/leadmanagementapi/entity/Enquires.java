package com.belenits.leadmanagementapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Students {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String studentId;
    private String studentName;
    private String studentPhone;
    @ManyToOne
    @JoinColumn(name = "counsellor_id")
    private Counsellors counsellors;

}
