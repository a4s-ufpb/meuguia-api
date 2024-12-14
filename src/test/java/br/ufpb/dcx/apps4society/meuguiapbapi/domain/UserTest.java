package br.ufpb.dcx.apps4society.meuguiapbapi.domain;

import br.ufpb.dcx.apps4society.meuguiapbapi.mock.UserTestHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private final UserTestHelper userTestHelper = UserTestHelper.getInstance();

    @Test
    void testEquals() {
        User user1 = userTestHelper.createUser(1);
        User user2 = userTestHelper.createUser(1);

        assertTrue(user1.equals(user2) && user2.equals(user1));
    }

    @Test
    void testEqualsNull() {
        User user1 = userTestHelper.createUser(1);

        assertNotEquals(null, user1);
    }

    @Test
    void testEqualsDifferentClass() {
        User user1 = userTestHelper.createUser(1);

        assertNotEquals(user1, new Object());
    }

    @Test
    void testEqualsDifferent() {
        User user1 = userTestHelper.createUser(1);
        User user2 = userTestHelper.createUser(2);

        assertFalse(user1.equals(user2) || user2.equals(user1));
    }

    @Test
    void testBuilder() {
        User result = User.builder()
                .id(1L)
                .firstName("test")
                .lastName("test")
                .email("test@test.com")
                .password("123test")
                .build();

        assertEquals(1L, result.getId());
        assertEquals("test", result.getFirstName());
        assertEquals("test", result.getLastName());
        assertEquals("test@test.com", result.getEmail());
        assertEquals("123test", result.getPassword());
    }

    @Test
    void testHashCode() {
        User user1 = userTestHelper.createUser(1);
        User user2 = userTestHelper.createUser(1);

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testToString() {
        User user = userTestHelper.createUser(1);
        String expected = "User(id=1, firstName=Test, lastName=User, email=test@test.com, password=123456)";
        assertEquals(expected, user.toString());
    }
}
