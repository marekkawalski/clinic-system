package com.marek_kawalski.clinic_system.seeder;

import com.marek_kawalski.clinic_system.admin.Admin;
import com.marek_kawalski.clinic_system.admin.AdminRepository;
import com.marek_kawalski.clinic_system.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Seeder {
    private final AdminRepository adminRepository;
    private final String genericPassword = "Password1234!";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @EventListener
    public void seedTables(ContextRefreshedEvent ignoredEvent) {
        this.seedUsers();
    }

    private void seedUsers() {
        if (!adminRepository.findAll().isEmpty()) return;
        adminRepository.save(Admin.builder()
                .name("admin")
                .surname("admin")
                .password(bCryptPasswordEncoder.encode(genericPassword))
                .email("admin@admin.com")
                .userRole(UserRole.ADMIN)
                .isEnabled(true)
                .build());
    }
}
