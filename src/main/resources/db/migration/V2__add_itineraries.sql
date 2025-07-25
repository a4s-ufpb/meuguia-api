CREATE TABLE itineraries
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(255),
    description VARCHAR(255),
    is_active   BOOLEAN                                 NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    is_public   BOOLEAN                                 NOT NULL,
    is_ranked   BOOLEAN                                 NOT NULL,
    created_by  BIGINT                                  NOT NULL,
    CONSTRAINT pk_itineraries PRIMARY KEY (id)
);

CREATE TABLE itinerary_item
(
    position      INTEGER,
    rating        INTEGER,
    attraction_id BIGINT NOT NULL,
    itinerary_id  BIGINT NOT NULL,
    CONSTRAINT pk_itineraryitem PRIMARY KEY (attraction_id, itinerary_id)
);

ALTER TABLE itineraries
    ADD CONSTRAINT FK_ITINERARIES_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES _user (id);

ALTER TABLE itinerary_item
    ADD CONSTRAINT FK_ITINERARYITEM_ON_ATTRACTION FOREIGN KEY (attraction_id) REFERENCES attraction (id);

ALTER TABLE itinerary_item
    ADD CONSTRAINT FK_ITINERARYITEM_ON_ITINERARY FOREIGN KEY (itinerary_id) REFERENCES itineraries (id);