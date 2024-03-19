package com.marek_kawalski.clinic_system.user.doctor;

import com.marek_kawalski.clinic_system.user.doctor.schedule.Schedule;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DoctorDetails {
    private String specialization;
    private String education;
    private String description;
    private Schedule schedule;
}
