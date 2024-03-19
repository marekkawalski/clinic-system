package com.marek_kawalski.clinic_system.user.doctor.schedule;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DailySchedule {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
