package com.marek_kawalski.clinic_system.appointment.dto;

import com.marek_kawalski.clinic_system.appointment.AppointmentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateUpdateAppointmentDTO(
        LocalDateTime date,
        AppointmentStatus status,

        String description,

        String doctorId,

        String patientId,

        String examinationId
) {
}
