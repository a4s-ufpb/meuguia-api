package br.ufpb.dcx.apps4society.meuguiapbapi.user.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.user.domain.User;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.dto.UpdateUserRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.dto.UserDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserDTO> getAuthenticatedUser(Authentication authentication) {
        log.info("Trying to get a logged user");
        User logedUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(logedUser.toDto());
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateAuthenticatedUser(@RequestBody UpdateUserRequestData updateUserRequestData, Authentication authentication) {
        log.debug("Trying to update a logged user with data: {}", updateUserRequestData);
        log.info("Trying to update a logged user");

        User logedUser = (User) authentication.getPrincipal();
        UserDTO userDTO = userService.update(updateUserRequestData, logedUser).toDto();

        log.debug("User updated: {}", userDTO);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteAuthenticatedUser(Authentication authentication) {
        log.info("Trying to delete a user");
        User logedUser = (User) authentication.getPrincipal();
        userService.delete(logedUser);
        log.info("User deleted");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        log.debug("Trying to get User with id {}", id);

        UserDTO userDTO = userService.findUserById(id).toDto();

        log.debug("User found: {}", userDTO);
        return ResponseEntity.ok(userDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable Long id) {
        log.debug("Trying to delete User with id {}", id);

        userService.deleteUserById(id);

        log.info("User with id {} deleted", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable Long id, @RequestBody UpdateUserRequestData updateUserRequestData) {
        log.debug("Trying to update User with id {} with data: {}", id, updateUserRequestData);

        UserDTO userDTO = userService.updateUserById(id, updateUserRequestData).toDto();

        log.debug("User updated: {}", userDTO);
        return ResponseEntity.ok(userDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        log.debug("Trying to get all users");

        Page<UserDTO> users = userService.findAllUsers(pageable).map(User::toDto);

        log.debug("Users found: {}", users.getTotalElements());
        return ResponseEntity.ok(users);
    }
}