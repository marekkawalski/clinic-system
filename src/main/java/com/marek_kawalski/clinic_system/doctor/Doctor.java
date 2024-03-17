package com.marek_kawalski.clinic_system.doctor;

import com.marek_kawalski.clinic_system.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Document(collection = "users")
@TypeAlias("doctor")
public class Doctor extends User {
    private String specialization;

}
