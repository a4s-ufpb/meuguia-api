package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationRequest;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.RegisterRequest;
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

    public RegisterRequest mockRequest(Integer num) {
        return RegisterRequest.builder()
                .firstName("mock User"+num)
                .lastName("last name")
                .email("mock.email@email.com")
                .password("12345678")
                .build();
    }

    public AuthenticationRequest mockAuthentication() {
        return AuthenticationRequest.builder()
                .email("mock.email@email.com")
                .password("12345678")
                .build();
    }
}
