package com.marek_kawalski.clinic_system.admin.dto;

import com.marek_kawalski.clinic_system.user.dto.CreateUserDTO;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record CreateAdminDTO(
        CreateUserDTO createUserDTO
) implements Serializable {
}
