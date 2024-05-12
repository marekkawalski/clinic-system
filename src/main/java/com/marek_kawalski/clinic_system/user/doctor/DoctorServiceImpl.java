package com.marek_kawalski.clinic_system.user.doctor;

import com.marek_kawalski.clinic_system.appointment.Appointment;
import com.marek_kawalski.clinic_system.appointment.AppointmentRepository;
import com.marek_kawalski.clinic_system.appointment.AppointmentStatus;
import com.marek_kawalski.clinic_system.appointment.exception.AppointmentIllegalStateException;
import com.marek_kawalski.clinic_system.examination.Examination;
import com.marek_kawalski.clinic_system.examination.ExaminationRepository;
import com.marek_kawalski.clinic_system.examination.exception.ExaminationNotFoundException;
import com.marek_kawalski.clinic_system.user.User;
import com.marek_kawalski.clinic_system.user.UserRepository;
import com.marek_kawalski.clinic_system.user.UserRequestParams;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final UserRepository userRepository;
    private final ExaminationRepository examinationRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public List<LocalDateTime> getAvailableAppointments(final String doctorId, final String examinationId, final LocalDateTime date) throws UserNotFoundException, ExaminationNotFoundException, AppointmentIllegalStateException {
        final Optional<User> optionalUser = userRepository.findById(doctorId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Doctor with id: " + doctorId + " not found");
        }
        final Optional<Examination> optionalExamination = examinationRepository.findById(examinationId);
        if (optionalExamination.isEmpty()) {
            throw new ExaminationNotFoundException("Examination with id: " + examinationId + " not found");
        }

        if (date.isBefore(LocalDateTime.now())) {
            throw new AppointmentIllegalStateException("Date cannot be in the past");
        }

        final User doctor = optionalUser.get();
        final Examination examination = optionalExamination.get();

        final DayOfWeek dayOfWeek = date.getDayOfWeek();
        final LocalTime startTime = doctor.getDoctorDetails().getSchedule().getDailySchedules().get(dayOfWeek).getStartTime();
        final LocalTime endTime = doctor.getDoctorDetails().getSchedule().getDailySchedules().get(dayOfWeek).getEndTime();

        final List<Appointment> bookedAppointments = appointmentRepository.findByDoctorAndDateBetweenAndStatus(doctor, date.with(LocalTime.MIN), date.with(LocalTime.MAX), AppointmentStatus.BOOKED);


        return getAvailableAppointmentsSlots(LocalDateTime.of(LocalDate.from(date), startTime), LocalDateTime.of(LocalDate.from(date), endTime), bookedAppointments, examination);
    }

    @Override
    public Page<User> getPagedDoctors(final UserRequestParams doctorRequestParams) {
        return userRepository.getPagedUsers(doctorRequestParams);
    }

    @Override
    public Optional<User> getDoctorByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    private List<LocalDateTime> getAvailableAppointmentsSlots(final LocalDateTime startTime, final LocalDateTime endTime, final List<Appointment> bookedAppointments, final Examination examination) {
        final List<LocalDateTime> availableAppointmentsSlots = new ArrayList<>();

        LocalDateTime currentTime = startTime;
        while (currentTime.plusMinutes(examination.getDuration()).isBefore(endTime)
               || currentTime.plusMinutes(examination.getDuration()).isEqual(endTime)) {
            boolean isAvailable = true;

            for (Appointment appointment : bookedAppointments) {
                if ((currentTime.isAfter(appointment.getDate())
                     || currentTime.isEqual(appointment.getDate()))
                    && (currentTime.isBefore(appointment.getDate())
                        || currentTime.isEqual(appointment.getDate()))
                ) {
                    currentTime = appointment.getDate().plusMinutes(appointment.getExamination().getDuration());
                    isAvailable = false;
                    break;
                }
            }
            if (isAvailable) {
                availableAppointmentsSlots.add(currentTime);
                currentTime = currentTime.plusMinutes(examination.getDuration());
            }
        }

        return availableAppointmentsSlots;
    }
}
