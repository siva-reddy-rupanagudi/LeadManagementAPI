package com.belenits.leadmanagementapi.mappings;

import com.belenits.leadmanagementapi.dto.CounsellorsDTO;
import com.belenits.leadmanagementapi.dto.CoursesDTO;
import com.belenits.leadmanagementapi.dto.EnquiresDTO;
import com.belenits.leadmanagementapi.entity.Counsellors;
import com.belenits.leadmanagementapi.entity.Courses;
import com.belenits.leadmanagementapi.entity.Enquires;
import com.belenits.leadmanagementapi.response.CounsellorResponse;
import com.belenits.leadmanagementapi.response.CourseResponse;
import com.belenits.leadmanagementapi.response.EnquiryResponse;
import org.modelmapper.ModelMapper;

public class ModelMappers {
    private static final ModelMapper modelMapper = new ModelMapper();


    public static CourseResponse toCoursesResponse(Courses courses) {
        return modelMapper.map(courses, CourseResponse.class);
    }

    public static Courses toCoursesEntity(CoursesDTO coursesDTO) {
        return modelMapper.map(coursesDTO, Courses.class);
    }

    public static EnquiryResponse toEnquiryResponse(Enquires enquires) {
        EnquiryResponse enquiryResponse = modelMapper.map(enquires, EnquiryResponse.class);
        enquiryResponse.setCounId(enquires.getCounsellors().getId());
        enquiryResponse.setCourseId(enquires.getCourse().getId());
        return enquiryResponse ;
    }

    public static Enquires toEnquiresEntity(EnquiresDTO enquiresDTO) {
        return modelMapper.map(enquiresDTO, Enquires.class);
    }

    public static CounsellorResponse toCounsellorResponse(Counsellors counsellors) {
        return modelMapper.map(counsellors, CounsellorResponse.class);
    }

    public static Counsellors toCounsellorsEntity(CounsellorsDTO counsellorsDTO) {
        return modelMapper.map(counsellorsDTO, Counsellors.class);
    }
}