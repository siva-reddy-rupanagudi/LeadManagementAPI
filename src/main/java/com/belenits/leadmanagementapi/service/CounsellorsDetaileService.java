package com.belenits.leadmanagementapi.service;

import com.belenits.leadmanagementapi.entity.Counsellors;
import com.belenits.leadmanagementapi.repo.CounsellorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CounsellorsDetaileService implements UserDetailsService {
    private final CounsellorRepo counsellorRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Counsellors counsellors = counsellorRepo.findByEmail(email);
        if (counsellors == null) {
            throw new UsernameNotFoundException(email);
        }
        return User.builder().username(counsellors.getEmail()).password(counsellors.getPassword()).build();
    }
}
