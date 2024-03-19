package com.marek_kawalski.clinic_system.user;

import com.marek_kawalski.clinic_system.user.dto.CreateUpdateUserDTO;
import com.marek_kawalski.clinic_system.user.exception.UserExistsException;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> createUpdateUser(final String userId, final CreateUpdateUserDTO createUpdateUserDTO) throws UserNotFoundException, UserExistsException {
        User user = new User();
        if (userId != null) {
            final Optional<User> oUser = userRepository.findById(userId);
            if (oUser.isEmpty()) throw new UserNotFoundException(String.format("User with id %s not found", userId));
            user = oUser.get();
        }

        user.setName(createUpdateUserDTO.name());
        user.setSurname(createUpdateUserDTO.surname());
        user.setEmail(validateEmail(user, createUpdateUserDTO.email()));
        user.setPhoneNumber(createUpdateUserDTO.phoneNumber());
        user.setPesel(createUpdateUserDTO.pesel());
        user.setAddress(createUpdateUserDTO.address());
        user.setDoctorDetails(createUpdateUserDTO.doctorDetails());

        if (createUpdateUserDTO.password() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(createUpdateUserDTO.password()));
        }

        //Todo check privileges
        if (createUpdateUserDTO.role() == null) {
            user.setUserRole(UserRole.PATIENT);
        }
        if (createUpdateUserDTO.enabled() != null) {
            user.setEnabled(createUpdateUserDTO.enabled());
        }

        return Optional.of(userRepository.save(user));
    }

    private String validateEmail(final User user, final String email) throws UserExistsException {
        if (user.getEmail() != null && user.getEmail().equals(email)) {
            return email;
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserExistsException(String.format("User with email %s already exists", email));
        }

        return email;
    }
}
