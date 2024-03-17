package com.marek_kawalski.clinic_system.admin.dto;

import com.marek_kawalski.clinic_system.user.UserRole;
import lombok.Builder;

@Builder
public record AdminDTO(
        String name,
        String surname,
        String email,
        UserRole userRole
) {
}
