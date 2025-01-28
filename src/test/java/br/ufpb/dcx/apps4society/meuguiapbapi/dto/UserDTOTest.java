package br.ufpb.dcx.apps4society.meuguiapbapi.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    @Test
    void builder() {
        var result = UserDTO.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("test")
                .lastName("case")
                .build();

        assertEquals(1L, result.getId());
        assertEquals("test@test.com", result.getEmail());
        assertEquals("test", result.getFirstName());
        assertEquals("case", result.getLastName());
    }

    @Test
    void testEquals() {
        var userDTO1 = new UserDTO(1L, "test@test.com", "test", "case");
        var userDTO2 = new UserDTO(1L, "test@test.com", "test", "case");

        assertTrue(userDTO1.equals(userDTO2) && userDTO2.equals(userDTO1));
    }

    @Test
    void testEqualsNull() {
        var userDTO1 = new UserDTO(1L, "test@test.com", "test", "case");
        assertNotEquals(null, userDTO1);
    }

    @Test
    void testEqualsDifferentClass() {
        var userDTO1 = new UserDTO(1L, "test@test.com", "test", "case");
        assertNotEquals(new Object(), userDTO1);
    }

    @Test
    void testEqualsDifferent() {
        var userDTO1 = new UserDTO(1L, "test@test.com", "test", "case");
        var userDTO2 = new UserDTO(2L, "test@test.com", "test", "case");

        assertFalse(userDTO1.equals(userDTO2) || userDTO2.equals(userDTO1));
    }

    @Test
    void testHashCode() {
        var userDTO1 = new UserDTO(1L, "test@test.com", "test", "case");
        var userDTO2 = new UserDTO(1L, "test@test.com", "test", "case");
        assertEquals(userDTO1.hashCode(), userDTO2.hashCode());
    }

    @Test
    void testToString() {
        var userDTO = new UserDTO(1L, "test@test.com", "test", "case");
        var expected = "UserDTO(id=1, email=test@test.com, firstName=test, lastName=case)";

        assertEquals(expected, userDTO.toString());
    }
}