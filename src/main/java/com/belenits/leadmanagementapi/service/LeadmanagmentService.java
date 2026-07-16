package com.belenits.leadmanagementapi.service;
import com.belenits.leadmanagementapi.dto.CounsellorsDTO;
import com.belenits.leadmanagementapi.dto.CoursesDTO;
import com.belenits.leadmanagementapi.dto.EnquiresDTO;
import com.belenits.leadmanagementapi.dto.LoginDTO;
import com.belenits.leadmanagementapi.response.CounsellorResponse;
import com.belenits.leadmanagementapi.response.CourseResponse;
import com.belenits.leadmanagementapi.response.EnquiryResponse;

import java.util.List;
import java.util.Map;

public interface LeadmanagmentService {
    public CourseResponse addCourse(CoursesDTO coursesDTO);
    public CourseResponse getCourseById(Integer id);
    public List<CourseResponse> getCourses();

    public CounsellorResponse addCounsellor(CounsellorsDTO counsellorsDTO);
    public CounsellorResponse getCounsellorById(String id);
    public List<CounsellorResponse> getCounsellors();

    public String counsellorlogin(LoginDTO loginDTO);
    public String logout(String id);

    public EnquiryResponse addEnquiry(EnquiresDTO enquiresDTO, String counId);
    public EnquiryResponse getEnquiresById(Integer id,String counId);
    public List<EnquiryResponse> getEnquires(Integer pageno,String counId);

    Map<String, Integer> dashboard(String counId);

    List<EnquiryResponse> getAllEnquiresByFilter(String couind,Integer courseId, String upperCase, String upperCase1);

    EnquiryResponse updateEnquires(Integer id, EnquiresDTO enquiresDTO, String counId);
}
