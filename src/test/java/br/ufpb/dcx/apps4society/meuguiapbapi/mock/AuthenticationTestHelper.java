package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AuthenticationRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.RegisterRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.User;

public class AuthenticationTestHelper {
    private static AuthenticationTestHelper instance;

    public static AuthenticationTestHelper getInstance() {
        if (instance == null) {
            instance = new AuthenticationTestHelper();
        }
        return instance;
    }

    public User getUser(Integer mockId) {
        return User.builder()
                .id(mockId.longValue())
                .firstName("test user"+mockId)
                .lastName("last name")
                .email("test@test.com")
                .password("12345678")
                .build();
    }

    public RegisterRequestData getRegisterRequestData(Integer num) {
        return RegisterRequestData.builder()
                .firstName("mock User"+num)
                .lastName("last name")
                .email("test@test.com")
                .password("12345678")
                .build();
    }

    public AuthenticationRequestData getAuthenticationRequestData() {
        return AuthenticationRequestData.builder()
                .email("test@test.com")
                .password("12345678")
                .build();
    }
}
