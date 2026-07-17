package com.belenits.leadmanagementapi.controller;

import com.belenits.leadmanagementapi.dto.CounsellorsDTO;
import com.belenits.leadmanagementapi.dto.CoursesDTO;
import com.belenits.leadmanagementapi.dto.EnquiresDTO;
import com.belenits.leadmanagementapi.dto.LoginDTO;
import com.belenits.leadmanagementapi.response.ApiResponse;
import com.belenits.leadmanagementapi.response.CounsellorResponse;
import com.belenits.leadmanagementapi.response.CourseResponse;
import com.belenits.leadmanagementapi.response.EnquiryResponse;
import com.belenits.leadmanagementapi.service.JwtService;
import com.belenits.leadmanagementapi.service.LeadmanagmentService;
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
    public static String counId = null;


    @PostMapping("/courses")
    public ResponseEntity<ApiResponse<CourseResponse>> addCourse(@Valid @RequestBody CoursesDTO coursesDTO) {
        CourseResponse addCourseRes = leadmanagmentService.addCourse(coursesDTO);
        ApiResponse<CourseResponse> response = new ApiResponse<>(addCourseRes, "Course added successfully", 201);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourseById(@PathVariable Integer id) {
        CourseResponse courseRes = leadmanagmentService.getCourseById(id);
        ApiResponse<CourseResponse> response = new ApiResponse<>(courseRes, "Course retrieved successfully", 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getCourses() {
        List<CourseResponse> coursesRes = leadmanagmentService.getCourses();
        ApiResponse<List<CourseResponse>> response = new ApiResponse<>(coursesRes, "Courses retrieved successfully", 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/counsellors")
    public ResponseEntity<ApiResponse<CounsellorResponse>> addCounsellor(@Valid @RequestBody CounsellorsDTO counsellorsDTO) {
        CounsellorResponse addCounsellorRes = leadmanagmentService.addCounsellor(counsellorsDTO);
        ApiResponse<CounsellorResponse> response = new ApiResponse<>(addCounsellorRes, "Counsellor added successfully", 201);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/counsellors/{id}")
    public ResponseEntity<ApiResponse<CounsellorResponse>> getCounsellorById(@PathVariable String id) {
        CounsellorResponse counsellorRes = leadmanagmentService.getCounsellorById(id);
        ApiResponse<CounsellorResponse> response = new ApiResponse<>(counsellorRes, "Counsellor retrieved successfully", 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/counsellors")
    public ResponseEntity<ApiResponse<List<CounsellorResponse>>> getCounsellors() {
        List<CounsellorResponse> counsellorsRes = leadmanagmentService.getCounsellors();
        ApiResponse<List<CounsellorResponse>> response = new ApiResponse<>(counsellorsRes, "Counsellors retrieved successfully", 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/counsellors/login")
    public ResponseEntity<ApiResponse<String>> counsellorlogin(@Valid @RequestBody LoginDTO loginDTO) {
        String counsellorId = leadmanagmentService.counsellorlogin(loginDTO);
        ApiResponse<String> response = new ApiResponse<>(counsellorId,jwtService.generateKey(counsellorId) , 200);
        counId = counsellorId;
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/counsellors/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        String result = leadmanagmentService.logout(counId);
        counId = null;
        ApiResponse<String> response = new ApiResponse<>(result, "", 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/counsellors/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> dashboard() {
        Map<String, Integer> dashboardData = leadmanagmentService.dashboard(counId);

        ApiResponse<Map<String, Integer>> response = new ApiResponse<>(dashboardData, "", 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/enquiries")
    public ResponseEntity<ApiResponse<EnquiryResponse>> addEnquiry(@Valid @RequestBody EnquiresDTO enquiresDTO) {
        enquiresDTO.setCourseModes(enquiresDTO.getCourseModes().toUpperCase());
        enquiresDTO.setStatus(enquiresDTO.getStatus().toUpperCase());
        EnquiryResponse resp = leadmanagmentService.addEnquiry(enquiresDTO, counId);
        ApiResponse<EnquiryResponse> response = new ApiResponse<>(resp, "Enquires added successfully", 200);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/enquires/{id}")
    public ResponseEntity<ApiResponse<EnquiryResponse>> getEnquiresById(@PathVariable Integer id) {
        EnquiryResponse enquiryResponse = leadmanagmentService.getEnquiresById(id, counId);
        ApiResponse<EnquiryResponse> response = new ApiResponse<>(enquiryResponse, "Fetching the enquiry Successfully By ID: " + id, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/enquiries/{pageno}")
    public ResponseEntity<ApiResponse<List<EnquiryResponse>>> getEnquires(@PathVariable Integer pageno) {
        List<EnquiryResponse> enquiryResponses = leadmanagmentService.getEnquires(pageno, counId);
        ApiResponse<List<EnquiryResponse>> response = new ApiResponse<>(enquiryResponses, "Fetching the enquiries Successfully", 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/enquires/filter")
    public ResponseEntity<ApiResponse<List<EnquiryResponse>>> getAllEnquiresByFilter(@RequestParam(required = false) String courseMode,
                                                                                     @RequestParam(required = false) String enquiryStatus,
                                                                                     @RequestParam(required = false) Integer courseId) {
        List<EnquiryResponse> enquiryResponses = leadmanagmentService.getAllEnquiresByFilter(counId, courseId, courseMode, enquiryStatus);
        ApiResponse<List<EnquiryResponse>> response = new ApiResponse<>(enquiryResponses, "Fetching the enquiries Successfully", 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/enquires/{id}")
    public ResponseEntity<ApiResponse<EnquiryResponse>> updateEnquires(@PathVariable Integer id,@RequestBody EnquiresDTO enquiresDTO) {
        EnquiryResponse enquiryResponse =leadmanagmentService.updateEnquires(id,enquiresDTO,counId);
        ApiResponse<EnquiryResponse> response = new ApiResponse<>(enquiryResponse, "Fetching the enquiries Successfully", 200);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}

