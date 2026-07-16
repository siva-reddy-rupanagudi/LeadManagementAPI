package com.belenits.leadmanagementapi.response;

import com.belenits.leadmanagementapi.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CounsellorResponse {
    private String id;
    private String name;
    private String email;
    private String phone;
    private Status status;
}
