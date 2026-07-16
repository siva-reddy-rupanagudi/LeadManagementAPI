package com.belenits.leadmanagementapi.repo;

import com.belenits.leadmanagementapi.entity.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Coursesrepo extends JpaRepository<Courses, Integer> {
}
