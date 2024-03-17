package com.marek_kawalski.clinic_system.admin;

import com.marek_kawalski.clinic_system.admin.dto.AdminDTO;
import com.marek_kawalski.clinic_system.admin.dto.CreateAdminDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/admins")
@Validated
public class AdminController {
    private final AdminService adminService;
    private final AdminMapper adminMapper;

    @PostMapping("/create")
    public ResponseEntity<AdminDTO> createAdmin(@RequestBody @Valid CreateAdminDTO createAdminDTO) {
        return adminService.createAdmin(createAdminDTO)
                .map(admin -> ResponseEntity.status(HttpStatus.CREATED).body(adminMapper.mapAdminToAdminDTO(admin)))
                .orElseGet(() -> ResponseEntity.badRequest().build());

    }

}
