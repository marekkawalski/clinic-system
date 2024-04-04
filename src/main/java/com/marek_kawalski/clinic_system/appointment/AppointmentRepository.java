package com.marek_kawalski.clinic_system.appointment;

import com.marek_kawalski.clinic_system.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, String>, AppointmentRepositoryCustom {
    List<Appointment> findByDoctorAndDateBetweenAndStatus(User user, LocalDateTime startDate, LocalDateTime endDate, AppointmentStatus status);
}
