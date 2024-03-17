package com.marek_kawalski.clinic_system.admin;

import com.marek_kawalski.clinic_system.admin.dto.CreateAdminDTO;
import com.marek_kawalski.clinic_system.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public Optional<Admin> createAdmin(final CreateAdminDTO createAdminDTO) {
        return Optional.of(adminRepository.save(Admin.builder()
                .email(createAdminDTO.createUserDTO().email())
                .password(bCryptPasswordEncoder.encode(createAdminDTO.createUserDTO().password()))
                .name(createAdminDTO.createUserDTO().name())
                .surname(createAdminDTO.createUserDTO().surname())
                .userRole(UserRole.ADMIN)
                .build()));
    }
}
