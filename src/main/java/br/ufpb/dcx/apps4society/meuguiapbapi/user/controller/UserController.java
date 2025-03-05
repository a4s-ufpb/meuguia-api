package br.ufpb.dcx.apps4society.meuguiapbapi.user.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.user.domain.User;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.dto.UpdateUserRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.dto.UserDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        User logedUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(UserDTO.builder()
                .id(logedUser.getId())
                .email(logedUser.getEmail())
                .firstName(logedUser.getFirstName())
                .lastName(logedUser.getLastName())
                .build());
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateAuthenticatedUser(@RequestBody UpdateUserRequestData updateUserRequestData, Authentication authentication) {
        User logedUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userService.update(updateUserRequestData, logedUser));
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteAuthenticatedUser(Authentication authentication) {
        log.info("Trying to delete a user");
        User logedUser = (User) authentication.getPrincipal();
        userService.delete(logedUser);
        log.info("User deleted");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}