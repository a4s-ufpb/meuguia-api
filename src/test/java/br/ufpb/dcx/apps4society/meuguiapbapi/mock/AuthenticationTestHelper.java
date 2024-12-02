package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AuthenticationRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.RegisterRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.User;

import java.util.Random;

public class AuthenticationTestHelper {
    private static AuthenticationTestHelper instance;

    public static AuthenticationTestHelper getInstance() {
        if (instance == null) {
            instance = new AuthenticationTestHelper();
        }
        return instance;
    }

    public User mockEntity(Integer num) {
        return User.builder()
                .id(new Random().nextLong(1,100))
                .firstName("mock User"+num)
                .lastName("last name")
                .email("mock.email@email.com")
                .password("12345678")
                .build();
    }

    public RegisterRequestData mockRequest(Integer num) {
        return RegisterRequestData.builder()
                .firstName("mock User"+num)
                .lastName("last name")
                .email("mock.email@email.com")
                .password("12345678")
                .build();
    }

    public AuthenticationRequestData mockAuthentication() {
        return AuthenticationRequestData.builder()
                .email("mock.email@email.com")
                .password("12345678")
                .build();
    }
}
