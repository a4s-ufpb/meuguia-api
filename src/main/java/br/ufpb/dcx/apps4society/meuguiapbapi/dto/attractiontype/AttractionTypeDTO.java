package br.ufpb.dcx.apps4society.meuguiapbapi.dto.attractiontype;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;

public class AttractionTypeDTO {
    private final Long id;
    private final String name;
    private final String description;

    public AttractionTypeDTO() {
        this(0L, "", "");
    }

    public AttractionTypeDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public AttractionTypeDTO(AttractionType obj) {
        this.id = obj.getId();
        this.name = obj.getName();
        this.description = obj.getDescription();
    }

    public static AttractionTypeDTOBuilder builder() {
        return new AttractionTypeDTOBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }


    public static final class AttractionTypeDTOBuilder {
        private Long id;
        private String name;
        private String description;

        private AttractionTypeDTOBuilder() {
        }

        public static AttractionTypeDTOBuilder anAttractionTypeDTO() {
            return new AttractionTypeDTOBuilder();
        }

        public AttractionTypeDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AttractionTypeDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public AttractionTypeDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public AttractionTypeDTO build() {
            return new AttractionTypeDTO(id, name, description);
        }
    }
}
