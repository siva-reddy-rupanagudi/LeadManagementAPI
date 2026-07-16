package com.belenits.leadmanagementapi.entity;

import com.belenits.leadmanagementapi.customIdGenerators.IdGenerator;
import com.belenits.leadmanagementapi.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Counsellors {
    @Id
    @IdGenerator(prefix = "COU")
    private String id;
    private String name;
    private String email;
    private String phone;
    private String password;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;
    @OneToMany(mappedBy = "counsellors", cascade = CascadeType.ALL)
    List<Enquires> enquires;


}
