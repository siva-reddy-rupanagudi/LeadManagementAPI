package com.belenits.leadmanagementapi.response;

import com.belenits.leadmanagementapi.enums.CourseModes;
import com.belenits.leadmanagementapi.enums.EnquiryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnquiryResponse {
    private Integer id;
    private String name;
    private String counId;
    private Integer courseId;
    private CourseModes courseModes;
    private EnquiryStatus status ;

}
