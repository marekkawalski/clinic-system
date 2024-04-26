package com.marek_kawalski.clinic_system.user;

import com.marek_kawalski.clinic_system.user.doctor.dto.DoctorDetailsDTO;
import com.marek_kawalski.clinic_system.user.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO mapUserToUserDTO(final User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .role(user.getUserRole())
                .phoneNumber(user.getPhoneNumber())
                .pesel(user.getPesel())
                .address(user.getAddress())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .updatedAt(user.getUpdatedAt())
                .doctorDetails(
                        user.getDoctorDetails() == null ? null :
                                DoctorDetailsDTO.builder()
                                        .specialization(user.getDoctorDetails().getSpecialization())
                                        .education(user.getDoctorDetails().getEducation())
                                        .description(user.getDoctorDetails().getDescription())
                                        .build()
                )
                .build();
    }
}
