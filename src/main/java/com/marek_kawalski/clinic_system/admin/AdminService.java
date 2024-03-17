package com.marek_kawalski.clinic_system.admin;

import com.marek_kawalski.clinic_system.admin.dto.CreateAdminDTO;

import java.util.Optional;

public interface AdminService {

    Optional<Admin> createAdmin(final CreateAdminDTO createAdminDTO);
}
