package br.ufpb.dcx.apps4society.meuguiapbapi.unit.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.dto.TourismSegmentationDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TourismSegmentationDTOTest {

    @Test
    void builder() {
        var result = TourismSegmentationDTO.builder()
                .id(1L)
                .name("name")
                .description("description")
                .build();

        assertEquals(1L, result.getId());
        assertEquals("name", result.getName());
        assertEquals("description", result.getDescription());
    }

    @Test
    void testEquals() {
        var requestData1 = new TourismSegmentationDTO(1L, "name", "description");
        var requestData2 = new TourismSegmentationDTO(1L, "name", "description");

        assertTrue(requestData1.equals(requestData2) && requestData2.equals(requestData1));
    }

    @Test
    void testEqualsNull() {
        var requestData1 = new TourismSegmentationDTO(1L, "name", "description");
        assertNotEquals(null, requestData1);
    }

    @Test
    void testEqualsDifferentClass() {
        var requestData1 = new TourismSegmentationDTO(1L, "name", "description");
        assertNotEquals(new Object(), requestData1);
    }

    @Test
    void testEqualsDifferent() {
        var requestData1 = new TourismSegmentationDTO(1L, "name", "description");
        var requestData2 = new TourismSegmentationDTO(2L, "name1", "description");

        assertFalse(requestData1.equals(requestData2) || requestData2.equals(requestData1));
    }

    @Test
    void testHashCode() {
        var requestData1 = new TourismSegmentationDTO(1L, "name", "description");
        var requestData2 = new TourismSegmentationDTO(1L, "name", "description");

        assertEquals(requestData1.hashCode(), requestData2.hashCode());
    }

    @Test
    void testToString() {
        var requestData = new TourismSegmentationDTO(1L, "name", "description");
        String expected = "TourismSegmentationDTO(id=1, name=name, description=description)";
        assertEquals(expected, requestData.toString());
    }
}