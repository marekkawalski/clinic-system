package com.marek_kawalski.clinic_system.appointment;

import com.marek_kawalski.clinic_system.appointment.dto.CreateUpdateAppointmentDTO;
import com.marek_kawalski.clinic_system.appointment.exception.AppointmentNotFoundException;
import com.marek_kawalski.clinic_system.examination.ExaminationRepository;
import com.marek_kawalski.clinic_system.examination.exception.ExaminationNotFoundException;
import com.marek_kawalski.clinic_system.user.UserRepository;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final ExaminationRepository examinationRepository;


    @Override
    public Optional<Appointment> createUpdateAppointment(final String id, final CreateUpdateAppointmentDTO createUpdateAppointmentDTO) throws UserNotFoundException, ExaminationNotFoundException, AppointmentNotFoundException {
        Appointment appointment;
        if (id != null) {
            appointment = appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException("Appointment not found!"));
            appointment.setLastUpdateDate(LocalDateTime.now());
        } else {
            appointment = new Appointment();
            appointment.setCreationDate(LocalDateTime.now());
        }

        updateAppointmentDetails(appointment, createUpdateAppointmentDTO);

        return Optional.of(appointmentRepository.save(appointment));

    }

    @Override
    public Optional<Appointment> findById(final String id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public Page<Appointment> findAllDoctorsAppointments(final String doctorId, final AppointmentRequestParams appointmentRequestParams) {
        return appointmentRepository.findAllDoctorsAppointments(doctorId, appointmentRequestParams);
    }

    @Override
    public Page<Appointment> findAllPatientsAppointments(final String patientId, final AppointmentRequestParams patientAppointmentRequestParams) {
        return appointmentRepository.findAllPatientsAppointments(patientId, patientAppointmentRequestParams);
    }

    private void updateAppointmentDetails(final Appointment appointment, final CreateUpdateAppointmentDTO createUpdateAppointmentDTO) throws UserNotFoundException, ExaminationNotFoundException {
        appointment.setDate(createUpdateAppointmentDTO.date());
        appointment.setDescription(createUpdateAppointmentDTO.description());
        appointment.setStatus(createUpdateAppointmentDTO.status());

        appointment.setDoctor(userRepository.findById(createUpdateAppointmentDTO.doctorId()).orElseThrow(() -> new UserNotFoundException("Doctor not found!")));
        appointment.setPatient(userRepository.findById(createUpdateAppointmentDTO.patientId()).orElseThrow(() -> new UserNotFoundException("Patient not found!")));
        appointment.setExamination(examinationRepository.findById(createUpdateAppointmentDTO.examinationId()).orElseThrow(() -> new ExaminationNotFoundException("Examination not found!")));
    }
}
