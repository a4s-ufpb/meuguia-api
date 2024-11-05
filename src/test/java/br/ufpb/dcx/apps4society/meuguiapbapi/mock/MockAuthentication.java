package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationForm;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.RegisterForm;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.User;

import java.util.Random;

public class MockAuthentication {

    public User mockEntity(Integer num) {
        return User.builder()
                .id(new Random().nextLong(1,100))
                .firstName("mock User"+num)
                .lastName("last name")
                .email("mock.email@email.com")
                .password("12345678")
                .build();
    }

    public RegisterForm mockRequest(Integer num) {
        return RegisterForm.builder()
                .firstName("mock User"+num)
                .lastName("last name")
                .email("mock.email@email.com")
                .password("12345678")
                .build();
    }

    public AuthenticationForm mockAuthentication() {
        return AuthenticationForm.builder()
                .email("mock.email@email.com")
                .password("12345678")
                .build();
    }
}
