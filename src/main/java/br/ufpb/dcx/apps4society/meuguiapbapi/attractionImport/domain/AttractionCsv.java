package br.ufpb.dcx.apps4society.meuguiapbapi.attractionImport.domain;

import com.opencsv.bean.CsvBindAndJoinByPosition;
import com.opencsv.bean.CsvBindAndSplitByPosition;
import com.opencsv.bean.CsvBindByPosition;
import org.apache.commons.collections4.MultiValuedMap;

import java.util.List;

public class AttractionCsv {
    // Name of the attraction (column 0)
    @CsvBindByPosition(position = 0)
    private String name;

    // City of the attraction (column 1)
    @CsvBindByPosition(position = 1)
    private String city;

    // Type of the attraction (column 2)
    @CsvBindByPosition(position = 2)
    private String attractionType;

    // Segmentations of the attraction (column 3), is split by commas
    @CsvBindAndSplitByPosition(elementType = String.class, position = 3, splitOn = ",")
    private List<String> segmentations;

    // Website of the attraction (column 4)
    @CsvBindByPosition(position = 4)
    private String site;

    // Description of the attraction (column 5)
    @CsvBindByPosition(position = 5)
    private String description;

    // Image URL of the attraction (column 6)
    @CsvBindByPosition(position = 6)
    private String imageUrl;

    // Description of the image (column 7)
    @CsvBindByPosition(position = 7)
    private String imageDescription;

    // Map link for the attraction (column 8)
    @CsvBindByPosition(position = 8)
    private String mapLink;

    // Additional info links (columns 9-22)
    @CsvBindAndJoinByPosition(position = "9-22", elementType = String.class)
    private MultiValuedMap<Integer, String> moreInfoLinks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttractionType() {
        return attractionType;
    }

    public void setAttractionType(String attractionType) {
        this.attractionType = attractionType;
    }

    public List<String> getSegmentations() {
        return segmentations;
    }

    public void setSegmentations(List<String> segmentations) {
        this.segmentations = segmentations;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    public String getMapLink() {
        return mapLink;
    }

    public void setMapLink(String mapLink) {
        this.mapLink = mapLink;
    }

    public MultiValuedMap<Integer, String> getMoreInfoLinks() {
        return moreInfoLinks;
    }

    public void setMoreInfoLinks(MultiValuedMap<Integer, String> moreInfoLinks) {
        this.moreInfoLinks = moreInfoLinks;
    }

    @Override
    public String toString() {
        return "AttractionCsv(" +
                "name=" + name +
                ", city=" + city +
                ", description=" + description +
                ", attractionType=" + attractionType +
                ", segmentations=" + segmentations +
                ", site=" + site +
                ", imageUrl=" + imageUrl +
                ", imageDescription=" + imageDescription +
                ", mapLink=" + mapLink +
                ", moreInfoLinks=" + moreInfoLinks +
                ')';
    }
}
