package br.ufpb.dcx.apps4society.meuguiapbapi.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.User;
import br.ufpb.dcx.apps4society.meuguiapbapi.dtos.RequestUserDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.dtos.ResponseUserDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

// TODO: Update user, delete user, get user information
// TODO: Update user, delete user, get user information for admin

    @GetMapping
    public ResponseEntity<ResponseUserDTO> getAuthenticatedUser(Authentication authentication) {
        User logedUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(ResponseUserDTO.builder()
                .id(logedUser.getId())
                .email(logedUser.getEmail())
                .firstName(logedUser.getFirstName())
                .lastName(logedUser.getLastName())
                .build());
    }

    @PutMapping
    public ResponseEntity<ResponseUserDTO> updateAuthenticatedUser(@RequestBody RequestUserDTO requestUserDTO, Authentication authentication) {
        User logedUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(userService.update(requestUserDTO, logedUser));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public void deleteAuthenticatedUser(Authentication authentication) {
        User logedUser = (User) authentication.getPrincipal();
        userService.delete(logedUser);
    }

}
