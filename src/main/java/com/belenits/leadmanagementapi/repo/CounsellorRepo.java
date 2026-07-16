package com.belenits.leadmanagementapi.repo;

import com.belenits.leadmanagementapi.entity.Counsellors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounsellorRepo extends JpaRepository<Counsellors, String> {
    public Counsellors findByEmail(String email);
    public Counsellors findByEmailAndPassword(String email, String password);
}
