package com.marek_kawalski.clinic_system.admin;

import com.marek_kawalski.clinic_system.admin.dto.AdminDTO;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {
    public AdminDTO mapAdminToAdminDTO(final Admin admin) {
        return AdminDTO.builder()
                .name(admin.getName())
                .surname(admin.getSurname())
                .email(admin.getEmail())
                .userRole(admin.getUserRole())
                .build();
    }
}
