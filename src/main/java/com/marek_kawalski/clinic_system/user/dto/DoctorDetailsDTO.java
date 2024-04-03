package com.marek_kawalski.clinic_system.user.dto;

import lombok.Builder;

@Builder
public record DoctorDetailsDTO(
        String specialization,
        String education,
        String description
) {
}
