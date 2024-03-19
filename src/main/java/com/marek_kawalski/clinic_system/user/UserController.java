package com.marek_kawalski.clinic_system.user;

import com.marek_kawalski.clinic_system.user.dto.CreateUpdateUserDTO;
import com.marek_kawalski.clinic_system.user.dto.UserDTO;
import com.marek_kawalski.clinic_system.user.exception.UserExistsException;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        final List<User> users = userService.getAllUsers();
        return users.isEmpty() ?
                ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(null)
                : ResponseEntity.status(HttpStatus.OK)
                .body(users.stream().map(userMapper::mapUserToUserDTO).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") final String userId, @Valid @RequestBody final CreateUpdateUserDTO createUpdateUserDTO) {
        try {
            return userService.createUpdateUser(userId, createUpdateUserDTO)
                    .map(user -> ResponseEntity.status(HttpStatus.OK)
                            .body(userMapper.mapUserToUserDTO(user)))
                    .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e.getCause());
        } catch (UserExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e.getCause());
        }
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody final CreateUpdateUserDTO createUpdateUserDTO) {
        try {
            return userService.createUpdateUser(null, createUpdateUserDTO)
                    .map(user -> ResponseEntity.status(HttpStatus.CREATED)
                            .body(userMapper.mapUserToUserDTO(user)))
                    .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e.getCause());
        } catch (UserExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e.getCause());
        }
    }

}
