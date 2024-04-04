package com.marek_kawalski.clinic_system.user.dto;

import com.marek_kawalski.clinic_system.user.Address;
import com.marek_kawalski.clinic_system.user.UserRole;
import com.marek_kawalski.clinic_system.user.doctor.dto.DoctorDetailsDTO;
import lombok.Builder;

@Builder
public record UserDTO(
        String id,
        String name,
        String surname,
        String email,
        UserRole role,
        String phoneNumber,
        String pesel,
        Address address,
        DoctorDetailsDTO doctorDetails

) {
}
