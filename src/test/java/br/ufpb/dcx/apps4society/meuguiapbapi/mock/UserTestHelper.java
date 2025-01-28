package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.User;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.UpdateUserRequestData;

public class UserTestHelper {
    private static UserTestHelper instance;

    public static UserTestHelper getInstance() {
        if (instance == null) {
            instance = new UserTestHelper();
        }
        return instance;
    }

    public UpdateUserRequestData getUpdateUserRequestData() {
        return UpdateUserRequestData.builder()
                .email("test@test.com")
                .firstName("Test")
                .lastName("User")
                .build();
    }

    public UpdateUserRequestData getUpdateUserRequestData(String email) {
        return UpdateUserRequestData.builder()
                .email(email)
                .firstName("Test")
                .lastName("User")
                .build();
    }

    public User createUser(Integer id) {
        return User.builder()
                .id(id.longValue())
                .email("test@test.com")
                .firstName("Test")
                .lastName("User")
                .password("123456")
                .build();
    }
}
