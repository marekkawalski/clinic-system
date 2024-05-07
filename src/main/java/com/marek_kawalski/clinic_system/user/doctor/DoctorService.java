package com.marek_kawalski.clinic_system.user.doctor;

import com.marek_kawalski.clinic_system.appointment.exception.AppointmentIllegalStateException;
import com.marek_kawalski.clinic_system.examination.exception.ExaminationNotFoundException;
import com.marek_kawalski.clinic_system.user.User;
import com.marek_kawalski.clinic_system.user.UserRequestParams;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface DoctorService {
    List<LocalDateTime> getAvailableAppointments(final String doctorId, final String examinationId, final LocalDateTime date) throws UserNotFoundException, ExaminationNotFoundException, AppointmentIllegalStateException;

    Page<User> getPagedDoctors(final UserRequestParams doctorRequestParams);
}
