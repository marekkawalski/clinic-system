package com.marek_kawalski.clinic_system.user.doctor;

import com.marek_kawalski.clinic_system.appointment.exception.AppointmentIllegalStateException;
import com.marek_kawalski.clinic_system.examination.exception.ExaminationNotFoundException;
import com.marek_kawalski.clinic_system.user.doctor.dto.AvailableAppointmentsDTO;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import com.marek_kawalski.clinic_system.utils.CustomDateTimeFormat;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/doctors")
@AllArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final AvailableAppointmentsMapper availableAppointmentsMapper;

    @GetMapping("/{id}/examinations/{examinationId}/available-appointments/date/{date}")
    public ResponseEntity<List<AvailableAppointmentsDTO>> getAvailableAppointments(@PathVariable final String id, @PathVariable final String examinationId,
                                                                                   @CustomDateTimeFormat @PathVariable final LocalDateTime date) {
        try {
            final List<LocalDateTime> availableAppointments = doctorService.getAvailableAppointments(id, examinationId, date);
            return availableAppointments.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
                    : ResponseEntity.status(HttpStatus.OK).body(
                    availableAppointmentsMapper.mapToAvailableAppointmentsDTOList(availableAppointments));
        } catch (UserNotFoundException | ExaminationNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e.getCause());
        } catch (AppointmentIllegalStateException e) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, e.getMessage(), e.getCause());
        }
    }

}
