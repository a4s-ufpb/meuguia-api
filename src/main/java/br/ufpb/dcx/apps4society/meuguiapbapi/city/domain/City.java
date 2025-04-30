package br.ufpb.dcx.apps4society.meuguiapbapi.city.domain;

import br.ufpb.dcx.apps4society.meuguiapbapi.city.dto.CityDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.domain.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "city")
public class City {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_id_seq")
    @SequenceGenerator(name = "city_id_seq", sequenceName = "city_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "ibge_id", unique = true)
    private Long ibgeId;

    @Column(name = "name")
    private String name;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "state_abbreviation", nullable = false)
    private String stateAbbreviation;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by")
    private User lastModifiedBy;

    public City() {
        this("", "", "", "");
    }

    public City(String name, String state, String stateAbbreviation, String country) {
        this.name = name;
        this.state = state;
        this.stateAbbreviation = stateAbbreviation;
        this.country = country;
    }

    public static CityBuilder builder() {
        return new CityBuilder();
    }

    public CityDTO toDto() {
        return CityDTO.CityDTOBuilder.aCityDTO()
                .id(id)
                .name(name)
                .state(state)
                .stateAbbreviation(stateAbbreviation)
                .country(country)
                .build();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Long getIbgeId() {
        return ibgeId;
    }

    public void setIbgeId(Long ibgeId) {
        this.ibgeId = ibgeId;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + state.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;
        return name.equalsIgnoreCase(city.name) && state.equalsIgnoreCase(city.state);
    }

    @Override
    public String toString() {
        return "City(" +
                "id=" + id +
                ", country=" + country +
                ", stateAbbreviation=" + stateAbbreviation +
                ", state=" + state +
                ", name='" + name +
                ')';
    }

    public static final class CityBuilder {
        private Long id;
        private String name;
        private String state;
        private String stateAbbreviation;
        private String country;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private CityBuilder() {
        }

        public static CityBuilder aCity() {
            return new CityBuilder();
        }

        public CityBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CityBuilder state(String state) {
            this.state = state;
            return this;
        }

        public CityBuilder stateAbbreviation(String stateAbbreviation) {
            this.stateAbbreviation = stateAbbreviation;
            return this;
        }

        public CityBuilder country(String country) {
            this.country = country;
            return this;
        }

        public CityBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public CityBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public City build() {
            City city = new City();
            city.setId(id);
            city.setName(name);
            city.setState(state);
            city.setStateAbbreviation(stateAbbreviation);
            city.setCountry(country);
            city.setCreatedAt(createdAt);
            city.setUpdatedAt(updatedAt);
            return city;
        }
    }
}