package com.marek_kawalski.clinic_system.user.doctor;

import com.marek_kawalski.clinic_system.appointment.exception.AppointmentIllegalStateException;
import com.marek_kawalski.clinic_system.examination.exception.ExaminationNotFoundException;
import com.marek_kawalski.clinic_system.user.User;
import com.marek_kawalski.clinic_system.user.UserRequestParams;
import com.marek_kawalski.clinic_system.user.doctor.dto.AvailableAppointmentsDTO;
import com.marek_kawalski.clinic_system.user.doctor.dto.DoctorDTO;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import com.marek_kawalski.clinic_system.utils.CustomDateTimeFormat;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/doctors")
@AllArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
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

    @GetMapping("/paged")
    public ResponseEntity<Page<DoctorDTO>> getPagedDoctors(@RequestParam(value = "sort", defaultValue = "name") final String sortField,
                                                           @RequestParam(value = "sort-dir", defaultValue = "ASC") final Sort.Direction sortDirection,
                                                           @RequestParam(value = "page-size", defaultValue = "10") final Integer pageSize,
                                                           @RequestParam(value = "page-num", defaultValue = "0") final Integer pageNum,
                                                           @RequestParam(value = "search", required = false) final String search) {
        final Page<User> pagedUsers = doctorService.getPagedDoctors(UserRequestParams.builder()
                .sortField(sortField)
                .sortDirection(sortDirection)
                .pageSize(pageSize)
                .pageNumber(pageNum)
                .search(search)
                .build());

        return pagedUsers.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null) :
                ResponseEntity.status(HttpStatus.OK).body(pagedUsers.map(doctorMapper::mapDoctorToDoctorDTO));
    }


}
