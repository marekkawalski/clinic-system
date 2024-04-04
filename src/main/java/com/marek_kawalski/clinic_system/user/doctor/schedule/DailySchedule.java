package com.marek_kawalski.clinic_system.user.doctor.schedule;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Builder
public class DailySchedule {
    private LocalTime startTime;
    private LocalTime endTime;
}
