package br.ufpb.dcx.apps4society.meuguiapbapi.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.authentication.dto.AuthenticationResponseData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationResponseDataTest {

    @Test
    void builder() {
        var result = AuthenticationResponseData.builder()
                .token("token.test")
                .build();

        assertEquals("token.test", result.getToken());
    }

    @Test
    void testEquals() {
        var authenticationResponseData1 = new AuthenticationResponseData("token.test");
        var authenticationResponseData2 = new AuthenticationResponseData("token.test");

        assertTrue(authenticationResponseData1.equals(authenticationResponseData2) && authenticationResponseData2.equals(authenticationResponseData1));
    }

    @Test
    void testEqualsNull() {
        var authenticationResponseData1 = new AuthenticationResponseData("token.test");

        assertNotEquals(null, authenticationResponseData1);
    }

    @Test
    void testEqualsDifferentClass() {
        var authenticationResponseData1 = new AuthenticationResponseData("token.test");

        assertNotEquals(new Object(), authenticationResponseData1);
    }

    @Test
    void testEqualsDifferent() {
        var authenticationResponseData1 = new AuthenticationResponseData("token.test");
        var authenticationResponseData2 = new AuthenticationResponseData("token.test2");

        assertFalse(authenticationResponseData1.equals(authenticationResponseData2) || authenticationResponseData2.equals(authenticationResponseData1));
    }

    @Test
    void testHashCode() {
        var authenticationResponseData1 = new AuthenticationResponseData("token.test");
        var authenticationResponseData2 = new AuthenticationResponseData("token.test");

        assertEquals(authenticationResponseData1.hashCode(), authenticationResponseData2.hashCode());
    }

    @Test
    void testToString() {
        var authenticationResponseData = new AuthenticationResponseData("token.test");
        assertEquals("AuthenticationResponseData(token=token.test)", authenticationResponseData.toString());
    }
}