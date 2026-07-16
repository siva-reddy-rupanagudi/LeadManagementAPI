package com.belenits.leadmanagementapi.repo;

import com.belenits.leadmanagementapi.entity.Enquires;
import com.belenits.leadmanagementapi.enums.EnquiryStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnquiresRepo extends JpaRepository<Enquires, Integer> {
    public List<Enquires> findAllByCounsellorsId(String counId);
    public List<Enquires> findAllByCounsellorsId(String counId, PageRequest pageRequest);
    public Optional<Enquires> findByIdAndCounsellorsId(Integer id, String counsellorsId);
    public List<Enquires> findAllByStatusAndCounsellorsId(EnquiryStatus status, String id);
}
