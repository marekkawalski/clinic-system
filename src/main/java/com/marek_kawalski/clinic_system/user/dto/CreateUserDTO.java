package com.marek_kawalski.clinic_system.user.dto;

import com.marek_kawalski.clinic_system.user.UserRole;
import com.marek_kawalski.clinic_system.utils.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(
        String id,
        @Size(min = Constants.MIN_NAME_LENGTH, max = Constants.MAX_NAME_LENGTH, message = "Name must be between " + Constants.MIN_NAME_LENGTH + " and " + Constants.MAX_NAME_LENGTH + " characters")
        String name,
        @Size(min = Constants.MIN_SURNAME_LENGTH, max = Constants.MAX_SURNAME_LENGTH, message = "Surname must be between " + Constants.MIN_SURNAME_LENGTH + " and " + Constants.MAX_SURNAME_LENGTH + " characters")
        String surname,
        @Email(message = "Invalid email format")
        String email,
        String password,
        UserRole userRole
) {
}
