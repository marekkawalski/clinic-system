package com.marek_kawalski.clinic_system.seeder;

import com.github.javafaker.Faker;
import com.marek_kawalski.clinic_system.appointment.Appointment;
import com.marek_kawalski.clinic_system.appointment.AppointmentRepository;
import com.marek_kawalski.clinic_system.appointment.AppointmentStatus;
import com.marek_kawalski.clinic_system.examination.Examination;
import com.marek_kawalski.clinic_system.examination.ExaminationRepository;
import com.marek_kawalski.clinic_system.examination.ExaminationStatus;
import com.marek_kawalski.clinic_system.user.Address;
import com.marek_kawalski.clinic_system.user.User;
import com.marek_kawalski.clinic_system.user.UserRepository;
import com.marek_kawalski.clinic_system.user.UserRole;
import com.marek_kawalski.clinic_system.user.doctor.DoctorDetails;
import com.marek_kawalski.clinic_system.user.doctor.schedule.DailySchedule;
import com.marek_kawalski.clinic_system.user.doctor.schedule.Schedule;
import lombok.AllArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
public class Seeder {
    private final UserRepository userRepository;
    private final ExaminationRepository examinationRepository;
    private final AppointmentRepository appointmentRepository;
    private final String genericPassword = "Password1234!";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Faker faker = new Faker();

    @EventListener
    public void seedTables(ContextRefreshedEvent ignoredEvent) {
        this.seedUsers();
        this.seedExaminations();
        this.seedAppointments();
    }

    private void seedUsers() {
        if (userRepository.count() > 0) return;
        this.seedDoctors();
        this.seedAdmins();
        this.seedPatients();
        this.seedRegistrars();
    }

    private void seedAppointments() {
        if (appointmentRepository.count() > 0) return;
        final User doctor = userRepository.findAllByUserRole(UserRole.ROLE_DOCTOR).get(faker.random().nextInt(0, userRepository.findAllByUserRole(UserRole.ROLE_DOCTOR).size() - 1));
        final User patient = userRepository.findAllByUserRole(UserRole.ROLE_PATIENT).get(faker.random().nextInt(0, userRepository.findAllByUserRole(UserRole.ROLE_PATIENT).size() - 1));
        final Optional<Examination> examination = examinationRepository.findById(doctor.getDoctorDetails().getExaminations().get(faker.random().nextInt(0, doctor.getDoctorDetails().getExaminations().size() - 1)).getId());
        if (examination.isEmpty()) return;
        appointmentRepository.save(Appointment.builder()
                .date(LocalDateTime.now())
                .status(AppointmentStatus.BOOKED)
                .description(faker.lorem().paragraph())
                .doctor(doctor)
                .patient(patient)
                .examination(examination.get())
                .build());
    }

    private User.UserBuilder seedCommonUserData() {
        final String name = faker.name().firstName();
        final String surname = faker.name().lastName();
        return User.builder()
                .name(name)
                .surname(surname)
                .email(name.toLowerCase(Locale.ROOT) + "." + surname.toLowerCase(Locale.ROOT) + "@doctor.com")
                .password(bCryptPasswordEncoder.encode(genericPassword))
                .isEnabled(true)
                .pesel("12345678901")
                .phoneNumber("123456789")
                .address(Address.builder()
                        .apartmentNumber(String.valueOf(faker.random().nextInt(1, 100)))
                        .city(faker.address().city())
                        .houseNumber(String.valueOf(faker.random().nextInt(1, 100)))
                        .postalCode(faker.address().zipCode())
                        .street(faker.address().streetName())
                        .country("Poland")
                        .build());
    }

    private void seedAdmins() {
        userRepository.save(
                seedCommonUserData()
                        .name("Admin")
                        .surname("Admin")
                        .email("admin@admin.com")
                        .userRole(UserRole.ROLE_ADMIN)
                        .build());
        IntStream.range(0, 10).forEach(i -> userRepository.save(
                seedCommonUserData()
                        .userRole(UserRole.ROLE_ADMIN)
                        .build()));
    }

    private void seedPatients() {
        userRepository.save(
                seedCommonUserData()
                        .name("Patient")
                        .surname("Patient")
                        .email("patient@patient.com")
                        .userRole(UserRole.ROLE_PATIENT)
                        .build());
        IntStream.range(0, 100).forEach(i -> userRepository.save(
                seedCommonUserData()
                        .userRole(UserRole.ROLE_PATIENT)
                        .build()));
    }

    private void seedRegistrars() {
        userRepository.save(
                seedCommonUserData()
                        .name("Registrar")
                        .surname("Registrar")
                        .email("registrar@registrar.com")
                        .userRole(UserRole.ROLE_REGISTRAR)
                        .build());
    }

    private void seedDoctors() {
        userRepository.save(
                seedCommonUserData()
                        .name("Doctor")
                        .surname("Doctor")
                        .email("doctor@doctor.com")
                        .userRole(UserRole.ROLE_DOCTOR)
                        .doctorDetails(getDoctorDetails())
                        .build());
        IntStream.range(0, 10).forEach(i -> userRepository.save(
                seedCommonUserData()
                        .userRole(UserRole.ROLE_DOCTOR)
                        .doctorDetails(getDoctorDetails())
                        .build()));


    }

    private DoctorDetails getDoctorDetails() {
        return DoctorDetails.builder()
                .specialization(List.of(
                        "Anesthesiologist",
                        "Cardiologist",
                        "Dermatologist",
                        "Endocrinologist",
                        "Gastroenterologist",
                        "Hematologist",
                        "Infectious Disease Specialist",
                        "Internist",
                        "Medical Geneticist",
                        "Neurologist",
                        "Obstetrician/Gynecologist"
                ).get(faker.random().nextInt(0, 10)))
                .education(faker.educator().university())
                .description(faker.lorem().paragraph())
                .schedule(Schedule.builder()
                        .dailySchedules(this.initializeDailySchedules())
                        .build())
                .build();
    }

    private Map<DayOfWeek, DailySchedule> initializeDailySchedules() {
        Map<DayOfWeek, DailySchedule> dailySchedules = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            LocalTime startTime = LocalTime.of(faker.random().nextInt(8, 12), 0); // Only hour part, minute set to 0
            LocalTime endTime = LocalTime.of(faker.random().nextInt(12, 18), 0); // Only hour part, minute set to 0
            DailySchedule schedule = DailySchedule.builder()
                    .startTime(startTime)
                    .endTime(endTime)
                    .build();
            dailySchedules.put(day, schedule);
        }
        return dailySchedules;
    }

    private void seedExaminations() {
        if (examinationRepository.count() > 0) return;

        final List<Examination> examinations = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> examinations.add(Examination.builder()
                .name(faker.medical().diseaseName())
                .description(faker.medical().symptoms())
                .price(faker.number().numberBetween(50, 500))
                .duration(
                        switch (faker.random().nextInt(0, 3)) {
                            case 0 -> 15;
                            case 1 -> 30;
                            case 2 -> 45;
                            default -> 60;
                        }
                )
                .status(ExaminationStatus.AVAILABLE)
                .doctors(new ArrayList<>())
                .build()));
        examinationRepository.saveAll(examinations);

        final List<User> doctors = userRepository.findAllByUserRole(UserRole.ROLE_DOCTOR);

        doctors.forEach(doctor -> {
            final List<Examination> tempExaminations = new ArrayList<>();
            Collections.shuffle(examinations);
            IntStream.range(0, faker.random().nextInt(1, 5)).forEach(i -> tempExaminations.add(examinations.get(faker.random().nextInt(0, examinations.size() - 1))));

            doctor.getDoctorDetails().getExaminations().addAll(tempExaminations);
            userRepository.save(doctor);

            tempExaminations.forEach(examination -> examination.getDoctors().add(doctor));
            examinationRepository.saveAll(tempExaminations);
        });

        final Optional<User> doctor = userRepository.findByEmail("doctor@doctor.com");

        doctor.ifPresent(value -> {
            final List<Examination> tempExaminations = new ArrayList<>();
            IntStream.range(0, faker.random().nextInt(1, 5)).forEach(i -> tempExaminations.add(
                    Examination.builder()
                            .name("Examination " + i)
                            .description("Description " + i)
                            .price(100)
                            .duration(30)
                            .status(ExaminationStatus.AVAILABLE)
                            .doctors(new ArrayList<>())
                            .build()
            ));
            examinationRepository.saveAll(tempExaminations);

            value.getDoctorDetails().getExaminations().addAll(tempExaminations);
            userRepository.save(value);

            tempExaminations.forEach(examination -> examination.getDoctors().add(value));
            examinationRepository.saveAll(tempExaminations);
        });

    }
}
