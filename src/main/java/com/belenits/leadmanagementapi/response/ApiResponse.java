package com.belenits.leadmanagementapi.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse <T>{
    private T data;
    private String message;
    private Integer status;
}
