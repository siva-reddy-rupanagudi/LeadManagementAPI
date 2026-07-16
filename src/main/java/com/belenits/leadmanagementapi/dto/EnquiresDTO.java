package com.belenits.leadmanagementapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnquiresDTO {
    @Valid

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotBlank(message = "Phone cannot be blank")
    @NotNull(message = "Phone is required")
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits")
    private String phone;
    @NotBlank(message = "Course mode cannot be blank")
    @NotNull(message = "Course mode is required")
    private String courseModes;
    @NotNull(message = "Course ID is required")
    private Integer courseId;
    private String status;
}
