package com.belenits.leadmanagementapi.controller;

import com.belenits.leadmanagementapi.dto.*;
import com.belenits.leadmanagementapi.response.*;
import com.belenits.leadmanagementapi.service.JwtService;
import com.belenits.leadmanagementapi.service.LeadmanagmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class LeadmanagmentController {

    private final LeadmanagmentService leadmanagmentService;
    private final JwtService jwtService;

    private String getCounsellorId(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization Bearer token is required");
        }

        String token = authHeader.substring(7);

        return jwtService.extractCounsellorId(token);
    }

    @PostMapping("/courses")
    public ResponseEntity<ApiResponse<CourseResponse>> addCourse(
            @Valid @RequestBody CoursesDTO coursesDTO) {

        CourseResponse response = leadmanagmentService.addCourse(coursesDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(response, "Course added successfully", 201));
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourseById(
            @PathVariable Integer id) {

        CourseResponse response = leadmanagmentService.getCourseById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(response, "Course retrieved successfully", 200));
    }

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getCourses() {

        List<CourseResponse> response = leadmanagmentService.getCourses();

        return ResponseEntity.ok(
                new ApiResponse<>(response, "Courses retrieved successfully", 200));
    }

    @PostMapping("/counsellors")
    public ResponseEntity<ApiResponse<CounsellorResponse>> addCounsellor(
            @Valid @RequestBody CounsellorsDTO counsellorsDTO) {

        CounsellorResponse response =
                leadmanagmentService.addCounsellor(counsellorsDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(response,
                        "Counsellor added successfully",
                        201));
    }

    @GetMapping("/counsellors/{id}")
    public ResponseEntity<ApiResponse<CounsellorResponse>> getCounsellorById(
            @PathVariable String id) {

        CounsellorResponse response =
                leadmanagmentService.getCounsellorById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(response,
                        "Counsellor retrieved successfully",
                        200));
    }

    @GetMapping("/counsellors")
    public ResponseEntity<ApiResponse<List<CounsellorResponse>>> getCounsellors() {

        List<CounsellorResponse> response =
                leadmanagmentService.getCounsellors();

        return ResponseEntity.ok(
                new ApiResponse<>(response,
                        "Counsellors retrieved successfully",
                        200));
    }

    @PostMapping("/counsellors/login")
    public ResponseEntity<ApiResponse<String>> login(
            @Valid @RequestBody LoginDTO loginDTO) {

        String counsellorId =
                leadmanagmentService.counsellorlogin(loginDTO);

        String token = jwtService.generateKey(counsellorId);

        return ResponseEntity.ok(
                new ApiResponse<>(counsellorId, token, 200));
    }

    @GetMapping("/counsellors/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            HttpServletRequest request) {

        String counsellorId = getCounsellorId(request);

        String result =
                leadmanagmentService.logout(counsellorId);

        return ResponseEntity.ok(
                new ApiResponse<>(result, "Logout successful", 200));
    }

    @GetMapping("/counsellors/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> dashboard(
            HttpServletRequest request) {

        String counsellorId = getCounsellorId(request);

        Map<String, Integer> dashboard =
                leadmanagmentService.dashboard(counsellorId);

        return ResponseEntity.ok(
                new ApiResponse<>(dashboard,
                        "Dashboard fetched successfully",
                        200));
    }

    @PostMapping("/enquiries")
    public ResponseEntity<ApiResponse<EnquiryResponse>> addEnquiry(
            @Valid @RequestBody EnquiresDTO enquiresDTO,
            HttpServletRequest request) {

        enquiresDTO.setCourseModes(enquiresDTO.getCourseModes().toUpperCase());
        enquiresDTO.setStatus(enquiresDTO.getStatus().toUpperCase());

        String counsellorId = getCounsellorId(request);

        EnquiryResponse response =
                leadmanagmentService.addEnquiry(enquiresDTO, counsellorId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(response,
                        "Enquiry added successfully",
                        201));
    }

    @GetMapping("/enquires/{id}")
    public ResponseEntity<ApiResponse<EnquiryResponse>> getEnquiryById(
            @PathVariable Integer id,
            HttpServletRequest request) {

        String counsellorId = getCounsellorId(request);

        EnquiryResponse response =
                leadmanagmentService.getEnquiresById(id, counsellorId);

        return ResponseEntity.ok(
                new ApiResponse<>(response,
                        "Enquiry fetched successfully",
                        200));
    }

    @GetMapping("/enquiries/{pageNo}")
    public ResponseEntity<ApiResponse<List<EnquiryResponse>>> getEnquiries(
            @PathVariable Integer pageNo,
            HttpServletRequest request) {

        String counsellorId = getCounsellorId(request);

        List<EnquiryResponse> response =
                leadmanagmentService.getEnquires(pageNo, counsellorId);

        return ResponseEntity.ok(
                new ApiResponse<>(response,
                        "Enquiries fetched successfully",
                        200));
    }

    @GetMapping("/enquires/filter")
    public ResponseEntity<ApiResponse<List<EnquiryResponse>>> filterEnquiries(
            @RequestParam(required = false) String courseMode,
            @RequestParam(required = false) String enquiryStatus,
            @RequestParam(required = false) Integer courseId,
            HttpServletRequest request) {

        String counsellorId = getCounsellorId(request);

        List<EnquiryResponse> response =
                leadmanagmentService.getAllEnquiresByFilter(
                        counsellorId,
                        courseId,
                        courseMode,
                        enquiryStatus);

        return ResponseEntity.ok(
                new ApiResponse<>(response,
                        "Filtered enquiries fetched successfully",
                        200));
    }

    @PutMapping("/enquires/{id}")
    public ResponseEntity<ApiResponse<EnquiryResponse>> updateEnquiry(
            @PathVariable Integer id,
            @RequestBody EnquiresDTO enquiresDTO,
            HttpServletRequest request) {

        String counsellorId = getCounsellorId(request);

        EnquiryResponse response =
                leadmanagmentService.updateEnquires(
                        id,
                        enquiresDTO,
                        counsellorId);

        return ResponseEntity.ok(
                new ApiResponse<>(response,
                        "Enquiry updated successfully",
                        200));
    }
}