package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.authentication.dto.AuthenticationRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.dto.RegisterUserRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.domain.User;

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

    public RegisterUserRequestData getRegisterRequestData(Integer num) {
        return RegisterUserRequestData.builder()
                .firstName("mock User"+num)
                .lastName("last name")
                .email(num+"test@test.com")
                .password("12345678")
                .build();
    }

    public AuthenticationRequestData createAuthenticationRequestData(Integer num) {
        return AuthenticationRequestData.builder()
                .email(num+"test@test.com")
                .password("12345678")
                .build();
    }

    public AuthenticationRequestData createAuthenticationRequestData() {
        return createAuthenticationRequestData(1);
    }
}
