package com.belenits.leadmanagementapi.service;

import com.belenits.leadmanagementapi.dto.CounsellorsDTO;
import com.belenits.leadmanagementapi.dto.CoursesDTO;
import com.belenits.leadmanagementapi.dto.EnquiresDTO;
import com.belenits.leadmanagementapi.dto.LoginDTO;
import com.belenits.leadmanagementapi.entity.Counsellors;
import com.belenits.leadmanagementapi.entity.Courses;
import com.belenits.leadmanagementapi.entity.Enquires;
import com.belenits.leadmanagementapi.enums.CourseModes;
import com.belenits.leadmanagementapi.enums.EnquiryStatus;
import com.belenits.leadmanagementapi.exception.*;
import com.belenits.leadmanagementapi.mappings.ModelMappers;
import com.belenits.leadmanagementapi.repo.CounsellorRepo;
import com.belenits.leadmanagementapi.repo.Coursesrepo;
import com.belenits.leadmanagementapi.repo.EnquiresRepo;
import com.belenits.leadmanagementapi.response.CounsellorResponse;
import com.belenits.leadmanagementapi.response.CourseResponse;
import com.belenits.leadmanagementapi.response.EnquiryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LeadmanagmentServiceImpl implements LeadmanagmentService {
    private final Coursesrepo coursesrepo;
    private final CounsellorRepo counsellorRepo;
    private final EnquiresRepo enquiresRepo;
    private final CounsellorsDetaileService counsellorsDetaileService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationProvider provider;



    @Override
    public CourseResponse addCourse(CoursesDTO coursesDTO) {
        Courses courses = ModelMappers.toCoursesEntity(coursesDTO);
        Courses savedCourse = coursesrepo.save(courses);
        return ModelMappers.toCoursesResponse(savedCourse);
    }

    @Override
    public CourseResponse getCourseById(Integer id) {
        Courses course = coursesrepo.findById(id).orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));
        return ModelMappers.toCoursesResponse(course);
    }

    @Override
    public List<CourseResponse> getCourses() {
        return coursesrepo.findAll().stream().map(ModelMappers::toCoursesResponse).collect(Collectors.toList());
    }

    @Override
    public CounsellorResponse addCounsellor(CounsellorsDTO counsellorsDTO) {
        Counsellors counsellors = ModelMappers.toCounsellorsEntity(counsellorsDTO);
        String pass=passwordEncoder.encode(counsellors.getPassword());
        counsellors.setPassword(pass);
        Counsellors savedCounsellors = counsellorRepo.save(counsellors);
        return ModelMappers.toCounsellorResponse(savedCounsellors);
    }

    @Override
    public CounsellorResponse getCounsellorById(String id) {
        Counsellors counsellor = counsellorRepo.findById(id).orElseThrow(() -> new CourseNotFoundException("Counsellor not found with id: " + id));
        return ModelMappers.toCounsellorResponse(counsellor);
    }

    @Override
    public List<CounsellorResponse> getCounsellors() {
        return counsellorRepo.findAll().stream().map(ModelMappers::toCounsellorResponse).collect(Collectors.toList());
    }

    @Override
    public String counsellorlogin(LoginDTO loginDTO) {
        Counsellors counsellors = counsellorRepo.findByEmail(loginDTO.getEmail());
        if (counsellors == null) {
            throw new EmailNotFoundException("Counsellor not found with email: " + loginDTO.getEmail());
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        Authentication authentication=provider.authenticate(token);
        if (!authentication.isAuthenticated()) {
            throw new InvaliCredentialsException("Invalid credentials");
        }
        return counsellors.getId();
    }

    @Override
    public String logout(String id) {
        if (id == null) {
            throw new LogoutFaildException("not yet logged in please login");
        }
        return "Successfully logged out";
    }

    @Override
    public EnquiryResponse addEnquiry(EnquiresDTO enquiresDTO, String counId) {
        if (counId == null) {
            throw new IllegalOperationException("not yet logged in please login");
        }
        Counsellors counsellor = counsellorRepo.findById(counId).orElseThrow(() -> new CousellorNotFoundException("Counsellor not found with id: " + counId));
        Courses courses = coursesrepo.findById(enquiresDTO.getCourseId()).orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + enquiresDTO.getCourseId()));
        Enquires enquires = ModelMappers.toEnquiresEntity(enquiresDTO);
        enquires.setId(null);
        enquires.setCounsellors(counsellor);
        enquires.setCourse(courses);
        Enquires savedEnquiry = enquiresRepo.save(enquires);
        EnquiryResponse enquiryResponse = ModelMappers.toEnquiryResponse(savedEnquiry);
        enquiryResponse.setCounId(savedEnquiry.getCounsellors().getId());
        enquiryResponse.setCourseId(savedEnquiry.getCourse().getId());
        return enquiryResponse;
    }

    @Override
    public EnquiryResponse getEnquiresById(Integer id, String counId) {
        if (counId == null) {
            throw new IllegalOperationException("not yet logged in please login");
        }
        Enquires enquires = enquiresRepo.findByIdAndCounsellorsId(id, counId).orElseThrow(() -> new EnquiryNotFoundException("Enquiry not found with counsellorid: " + counId));
        EnquiryResponse enquiryResponse = ModelMappers.toEnquiryResponse(enquires);
        enquiryResponse.setCounId(enquires.getCounsellors().getId());
        enquiryResponse.setCourseId(enquires.getCourse().getId());
        return enquiryResponse;
    }

    @Override
    public List<EnquiryResponse> getEnquires(Integer pageno, String counId) {
        if (counId == null) {
            throw new IllegalOperationException("not yet logged in please login");
        }
        if (pageno <= 0) {
            throw new IllegalOperationException("pageno should be greater than 0");
        }
        return enquiresRepo.findAllByCounsellorsId(counId, PageRequest.of(pageno - 1, 5)).stream().map(ModelMappers::toEnquiryResponse).collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> dashboard(String counId) {
        if (counId == null) {
            throw new IllegalOperationException("not yet logged in please login");
        }
        Integer totalEntries = enquiresRepo.findAllByCounsellorsId(counId).size();
        Integer totalOpenentries = enquiresRepo.findAllByStatusAndCounsellorsId(EnquiryStatus.OPEN, counId).size();
        Integer totalEnrolled = enquiresRepo.findAllByStatusAndCounsellorsId(EnquiryStatus.ENROLLED, counId).size();
        Integer totalLost = enquiresRepo.findAllByStatusAndCounsellorsId(EnquiryStatus.LOST, counId).size();
        Map<String, Integer> dashboard = new LinkedHashMap<>();
        dashboard.put("totalEntries", totalEntries);
        dashboard.put("totalOpenentries", totalOpenentries);
        dashboard.put("totalEnrolled", totalEnrolled);
        dashboard.put("totalLost", totalLost);
        return dashboard;
    }

    public List<EnquiryResponse> getAllEnquiresByFilter(String couId, Integer courseId, String courseMode, String enquireStatus) {
        if (couId == null) {
            throw new IllegalOperationException("Not yet logged in, please login");
        }
        Enquires filterTemplate = new Enquires();
        filterTemplate.setStatus(null);

        Counsellors dummyCounsellor = new Counsellors();
        dummyCounsellor.setId(couId);
        filterTemplate.setCounsellors(dummyCounsellor);

        if (courseId != null) {
            Courses dummyCourse = new Courses();
            dummyCourse.setId(courseId);
            filterTemplate.setCourse(dummyCourse);
        }
        if (!Objects.equals(courseMode, null)) {
            filterTemplate.setCourseModes(CourseModes.valueOf(courseMode.toUpperCase()));
        }

        if (!Objects.equals(enquireStatus, null)) {
            filterTemplate.setStatus(EnquiryStatus.valueOf(enquireStatus.toUpperCase()));
        }
        Example<Enquires> enquiresExample = Example.of(filterTemplate);
        List<Enquires> enquiresList = enquiresRepo.findAll(enquiresExample);
        return enquiresList.stream()
                .map(ModelMappers::toEnquiryResponse)
                .toList();
    }

    @Override
    public EnquiryResponse updateEnquires(Integer id, EnquiresDTO enquiresDTO, String counId) {
        if (counId == null) {
            throw new IllegalOperationException("Not yet logged in, please login");
        }
        Enquires enquires = enquiresRepo.findByIdAndCounsellorsId(id, counId)
                .orElseThrow(() -> new EnquiryNotFoundException("Enquiry not found with ID: " + id + " for Counsellor ID: " + counId));

        if (enquiresDTO.getName() != null) {
            enquires.setName(enquiresDTO.getName());
        }
        if (enquiresDTO.getPhone() != null) {
            enquires.setPhone(enquiresDTO.getPhone());
        }
        if (enquiresDTO.getCourseModes() != null) {
            CourseModes mode = enquiresDTO.getCourseModes().equalsIgnoreCase("online") ? CourseModes.ONLINE : CourseModes.OFFLINE;
            enquires.setCourseModes(mode);
        }
        if (enquiresDTO.getCourseId() != null) {
            Courses courses = coursesrepo.findById(enquiresDTO.getCourseId())
                    .orElseThrow(() -> new CourseNotFoundException("Course not found with Id: " + enquiresDTO.getCourseId()));
            enquires.setCourse(courses);
        }
        if (enquiresDTO.getStatus() != null) {
            EnquiryStatus status=enquiresDTO.getStatus().equalsIgnoreCase("open")?EnquiryStatus.OPEN:(enquiresDTO.getStatus().equalsIgnoreCase("lost")?EnquiryStatus.LOST:EnquiryStatus.ENROLLED);
            enquires.setStatus(status);
        }
        Enquires savedEnqueries = enquiresRepo.save(enquires);
        return ModelMappers.toEnquiryResponse(savedEnqueries);
    }
}
