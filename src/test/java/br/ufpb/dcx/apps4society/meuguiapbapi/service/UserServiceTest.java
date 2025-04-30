package br.ufpb.dcx.apps4society.meuguiapbapi.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.domain.User;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.dto.UpdateUserRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.dto.UserDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.repository.UserRepository;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.UserTestsHelper.createUpdateUserRequestData;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.UserTestsHelper.createUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void updateUserTest() {
        User user = createUser(1);
        UpdateUserRequestData requestData = createUpdateUserRequestData();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserDTO result = userService.update(requestData, user);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(requestData.getEmail(), result.getEmail());
        assertEquals(requestData.getFirstName(), result.getFirstName());
        assertEquals(requestData.getLastName(), result.getLastName());
    }

    @Test
    void updateUser_NotExistTest() {
        User user = createUser(1);
        UpdateUserRequestData requestData = createUpdateUserRequestData();
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        var thrown = assertThrows(ObjectNotFoundException.class, () -> userService.update(requestData, user));

        assertEquals("User with id " + user.getId() + " not found", thrown.getMessage());
    }

    @Test
    void deleteUserTest() {
        User user = createUser(1);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.delete(user);

        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_NotExistTest() {
        User user = createUser(1);
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        var thrown = assertThrows(ObjectNotFoundException.class, () -> userService.delete(user));

        assertEquals("User with id " + user.getId() + " not found", thrown.getMessage());
    }
}
