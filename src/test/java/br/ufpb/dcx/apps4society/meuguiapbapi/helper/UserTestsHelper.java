package br.ufpb.dcx.apps4society.meuguiapbapi.helper;

import br.ufpb.dcx.apps4society.meuguiapbapi.authentication.dto.AuthenticationRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.authentication.dto.AuthenticationResponseData;
import br.ufpb.dcx.apps4society.meuguiapbapi.authentication.service.AuthenticationService;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.domain.User;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.dto.RegisterUserRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.dto.UpdateUserRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.dto.UserDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.repository.UserRepository;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

@Component
public class UserTestsHelper {
    public static final String PATH_USER_REGISTER = "/auth/register";
    public static final String PATH_USER_AUTHENTICATE = "/auth/authenticate";
    public static final String PATH_USER = "/users";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User lastUserCreated;

    public static UserDTO register(RegisterUserRequestData bodyRequest) {

        return given()
                .contentType(ContentType.JSON)
                .body(bodyRequest)
                .when()
                .post(PATH_USER_REGISTER)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(UserDTO.class);
    }

    public static AuthenticationResponseData authenticate(User user) {
        return given()
                .contentType(ContentType.JSON)
                .body(
                        AuthenticationRequestData.builder()
                                .email(user.getEmail())
                                .password(user.getPassword())
                                .build()
                )
                .when()
                .post(PATH_USER_AUTHENTICATE)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(AuthenticationResponseData.class);
    }

    public static AuthenticationResponseData registerAndAuthenticate(RegisterUserRequestData bodyRequest) {
        register(bodyRequest);
        return authenticate(User.builder()
                .email(bodyRequest.getEmail())
                .password(bodyRequest.getPassword())
                .build());
    }

    public static void delete(UserDTO userDTO) {
        String token = authenticate(User.builder()
                .email(userDTO.getEmail())
                .password("12345678")
                .build()).getToken();

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(PATH_USER)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    public static void delete(String token) {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(PATH_USER)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    public static UpdateUserRequestData createUpdateUserRequestData() {
        return UpdateUserRequestData.builder()
                .email("test@test.com")
                .firstName("Test")
                .lastName("User")
                .build();
    }

    public static UpdateUserRequestData createUpdateUserRequestData(String email) {
        return UpdateUserRequestData.builder()
                .email(email)
                .firstName("Test")
                .lastName("User")
                .build();
    }

    public static User createUser(Integer id) {
        return User.builder()
                .id(id.longValue())
                .email("test@test.com")
                .firstName("Test")
                .lastName("User")
                .password("123456")
                .build();
    }

    public static RegisterUserRequestData createRegisterRequestData(Integer num) {
        return RegisterUserRequestData.builder()
                .firstName("mock User" + num)
                .lastName("last name")
                .email(num + "test@test.com")
                .password("12345678")
                .build();
    }

    public static AuthenticationRequestData createAuthenticationRequestData(Integer num) {
        return AuthenticationRequestData.builder()
                .email(num + "test@test.com")
                .password("12345678")
                .build();
    }

    public static AuthenticationRequestData createAuthenticationRequestData() {
        return createAuthenticationRequestData(1);
    }

    // TODO: Adicionar permissões padrões

    /**
     * Create a User with default permissions.
     * firstName: user;
     * lastName: default;
     * email: user@default;
     * password: password;
     *
     * @return A user with default permissions
     */
    public User createUserWithDefaultPermissions() {
        return userRepository.save(
                User.builder()
                        .firstName("user")
                        .lastName("default")
                        .email("user@default")
                        .password(passwordEncoder.encode("password"))
                        .build()
        );
    }

    // TODO: Adicionar permissões quando forem implementadas

    /**
     * Create a User with default permissions.
     * user firstName: admin
     * user lastName: default
     * user email: admin@default
     * user password: password
     *
     * @return A user with admin permissions
     */
    public User createUserWithAdminPermissions() {
        return userRepository.save(
                User.builder()
                        .firstName("admin")
                        .lastName("default")
                        .email("admin@default")
                        .password(passwordEncoder.encode("password"))
                        .build()
        );
    }

    /**
     * Create a User with the given parameters.
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @return A user
     */
    public User createUser(String firstName, String lastName, String email, String password) {
        return userRepository.save(
                User.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .build()
        );
    }

    /**
     * Create and authenticate a user with default permissions.
     *
     * @return A JWT string token
     */
    public String createAndAuthenticateUserWithDefaultPermissions() {
        this.lastUserCreated = this.createUserWithDefaultPermissions();
        return authenticationService.authenticate(new AuthenticationRequestData("user@default", "password")).getToken();
    }

    /**
     * Create and authenticate a user with admin permissions.
     *
     * @return A JWT string token
     */
    public String createAndAuthenticateUserWithAdminPermission() {
        this.lastUserCreated = this.createUserWithAdminPermissions();
        return authenticationService.authenticate(new AuthenticationRequestData("admin@default", "password")).getToken();
    }

    /**
     * Delete the last user created if it exists.
     */
    public void deleteLastUserCreated() {
        if (this.lastUserCreated != null) {
            userRepository.delete(this.lastUserCreated);
            this.lastUserCreated = null;
        }
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
