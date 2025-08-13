package br.ufpb.dcx.apps4society.meuguiapbapi.attractionImport.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.repository.AttractionRepository;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionImport.exceptions.ImportFileException;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionImport.domain.AttractionCsv;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionImport.domain.enums.ImportStatus;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionImport.dto.FieldDifference;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionImport.domain.enums.DiffType;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionImport.dto.AttractionDiffResponse;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractionImport.dto.ImportResponse;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.repository.AttractionTypeRepository;
import br.ufpb.dcx.apps4society.meuguiapbapi.city.domain.City;
import br.ufpb.dcx.apps4society.meuguiapbapi.city.repository.CityRepository;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.repository.TourismSegmentationRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AttractionCsvService {
    private static final Logger log = LoggerFactory.getLogger(AttractionCsvService.class);
    private final AttractionTypeRepository attractionTypeRepository;
    private final TourismSegmentationRepository tourismSegmentationRepository;
    private final CityRepository cityRepository;
    private final AttractionRepository attractionRepository;

    public AttractionCsvService(AttractionTypeRepository attractionTypeRepository, TourismSegmentationRepository tourismSegmentationRepository, CityRepository cityRepository, AttractionRepository attractionRepository) {
        this.attractionTypeRepository = attractionTypeRepository;
        this.tourismSegmentationRepository = tourismSegmentationRepository;
        this.cityRepository = cityRepository;
        this.attractionRepository = attractionRepository;
    }

    public List<ImportResponse> importAttractionsFromCsv(MultipartFile file) {
        validateFile(file);

        List<AttractionCsv> csvAttractions = readAndConvertFile(file);
        List<ImportResponse> importResponses = new ArrayList<>();

        csvAttractions.forEach(attraction -> {
            log.debug("Processing attraction: {}", attraction);
            Optional<Attraction> optionalAttraction = attractionRepository.findByNameAndCityName(attraction.getName(), attraction.getCity());
            var importResponse = new ImportResponse(attraction.getName(), ImportStatus.IN_PROGRESS, "Importing attraction from CSV");
            if (optionalAttraction.isPresent()) {
                log.debug("Attraction {} already exists in the database, skipping import.", attraction.getName());
                importResponse.setStatus(ImportStatus.SKIPPED);
                importResponse.setMessage("Attraction already exists in the database, skipping import.");
                importResponses.add(importResponse);
                return;
            }
            log.debug("Creating new attraction from CSV: {}", attraction.getName());
            try {
                importAttraction(attraction);
                importResponse.setStatus(ImportStatus.CREATED);
                importResponse.setMessage("Attraction imported successfully.");
            } catch (ObjectNotFoundException | DataIntegrityViolationException e) {
                log.error("Error importing attraction {}: {}", attraction.getName(), e.getMessage());
                importResponse.setStatus(ImportStatus.ERROR);
                importResponse.setMessage("Error importing attraction: " + e.getMessage());
            }

            importResponses.add(importResponse);
        });
        return importResponses;
    }

    public List<AttractionDiffResponse> compareAttractions(MultipartFile file) {
        validateFile(file);
        log.debug("compareAttractions called with file: {}", file.getOriginalFilename());
        List<AttractionCsv> csvAttractions = readAndConvertFile(file);
        return compareAttractions(csvAttractions);
    }

    public List<AttractionDiffResponse> compareAttractions(List<AttractionCsv> csvAttractions) {
        log.debug("compareAttractions called");
        List<AttractionDiffResponse> responses = new ArrayList<>();

        for (AttractionCsv csvAttraction : csvAttractions) {
            log.debug("Comparing attraction: {}", csvAttraction.getName());
            Optional<Attraction> optionalAttraction = attractionRepository.findByNameAndCityName(csvAttraction.getName(), csvAttraction.getCity());

            if (optionalAttraction.isPresent()) {
                Attraction dbAttraction = optionalAttraction.get();
                List<FieldDifference> differences = generateDiff(csvAttraction, dbAttraction);
                AttractionDiffResponse response = new AttractionDiffResponse(csvAttraction.getName(), differences);
                responses.add(response);
            } else {
                log.info("No database record found for attraction: {}", csvAttraction.getName());
                AttractionDiffResponse response = new AttractionDiffResponse(csvAttraction.getName(), List.of(), false, DiffType.NEW);
                responses.add(response);
            }
        }

        return responses;
    }

    public List<FieldDifference> generateDiff(AttractionCsv csvAttraction, Attraction dbAttraction) {
        List<FieldDifference> differences = new ArrayList<>();

        // Compare each field
        compareField("name", csvAttraction.getName(), dbAttraction.getName(), differences);
        compareField("description", csvAttraction.getDescription(), dbAttraction.getDescription(), differences);
        compareField("mapLink", csvAttraction.getMapLink(), dbAttraction.getMapLink(), differences);
        compareField("imageLink", csvAttraction.getImageUrl(), dbAttraction.getImageLink(), differences);

        // Complex fields comparison
        compareCityField(csvAttraction.getCity(), dbAttraction.getCity(), differences);
        compareAttractionTypeField(csvAttraction.getAttractionType(), dbAttraction.getAttractionType(), differences);
        compareSegmentationsField(csvAttraction.getSegmentations(), dbAttraction.getSegmentations(), differences);
        compareInfoLinksField(csvAttraction.getMoreInfoLinks().values().stream().toList(), dbAttraction.getMoreInfoLinks(), differences);

        return differences;
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            log.error("File is empty");
            throw new ImportFileException("File cannot be empty");
        }
        if (!file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
            log.error("The file is not an CSV: {}", file.getOriginalFilename());
            throw new ImportFileException("File must be a CSV");
        }
    }

    private void importAttraction(AttractionCsv attractionCsv) {
        log.debug("Importing attraction: {}", attractionCsv.getName());

        City city = cityRepository.findCityByNameIgnoreCase(attractionCsv.getCity())
                .orElseThrow(() -> new ObjectNotFoundException("City not found: " + attractionCsv.getCity()));
        AttractionType attractionType = findOrCreateAttractionType(attractionCsv.getAttractionType());
        List<TourismSegmentation> segmentations = attractionCsv.getSegmentations().stream().parallel().map(this::findOrCreateSegmentation).toList();
        List<MoreInfoLink> moreInfoLinks = attractionCsv.getMoreInfoLinks().values().stream()
                .filter(link -> link != null && !link.isBlank())
                .map(link -> new MoreInfoLink(link,""))
                .toList();

        Attraction attraction = Attraction.builder()
            .name(attractionCsv.getName())
            .description(attractionCsv.getDescription())
            .mapLink(attractionCsv.getMapLink())
            .imageLink(attractionCsv.getImageUrl())
            .city(city)
            .attractionType(attractionType)
            .segmentations(segmentations)
            .moreInfoLinks(moreInfoLinks)
            .build();

        attractionRepository.save(attraction);
    }

    private AttractionType findOrCreateAttractionType(String typeName) {
        return attractionTypeRepository.findByNameIgnoreCase(typeName)
                .orElseGet(() -> {
                    return attractionTypeRepository.save(new AttractionType(null, typeName, ""));
                });
    }

    private TourismSegmentation findOrCreateSegmentation(String segmentationName) {
        return tourismSegmentationRepository.findByNameIgnoreCase(segmentationName)
                .orElseGet(() -> {
                    return tourismSegmentationRepository.save(new TourismSegmentation(null, segmentationName, ""));
                });
    }

    // TODO: Create tests for verify diferentes csv files and structures
    private List<AttractionCsv> readAndConvertFile(MultipartFile file) {
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CsvToBean<AttractionCsv> csvToBean = new CsvToBeanBuilder<AttractionCsv>(reader)
                    .withType(AttractionCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            var list = csvToBean.parse();
            list.removeFirst();
            log.debug("Parsed {} attractions from CSV file", list.size());
            return list;
        } catch (Exception e) {
            log.error("Error reading or parsing CSV file: {}", e.getMessage());
            throw new RuntimeException("Failed to parse CSV file", e);
        }
    }

    private void compareField(String fieldName, String csvValue, String dbValue, List<FieldDifference> differences) {
        if (!csvValue.equals(dbValue)) {
            differences.add(new FieldDifference(fieldName, csvValue, dbValue, DiffType.CHANGED));
        }
    }

    private void compareCityField(String csvCityName, City dbCity, List<FieldDifference> differences) {
        String dbCityName = dbCity != null ? dbCity.getName() : null;
        compareField("city", csvCityName, dbCityName, differences);
    }

    private void compareAttractionTypeField(String csvTypeName, AttractionType dbType, List<FieldDifference> differences) {
        String dbTypeName = dbType != null ? dbType.getName() : null;
        compareField("attractionType", csvTypeName, dbTypeName, differences);
    }

    private void compareInfoLinksField(List<String> moreInfoLinks, List<MoreInfoLink> moreInfoLinks1, List<FieldDifference> differences) {
        if (moreInfoLinks.size() != moreInfoLinks1.size()) {
            differences.add(new FieldDifference("moreInfoLinks", moreInfoLinks, moreInfoLinks1, DiffType.CHANGED));
            return;
        }

        for (int i = 0; i < moreInfoLinks.size(); i++) {
            String csvLink = moreInfoLinks.get(i);
            String dbLink = moreInfoLinks1.get(i).getLink();
            if (!csvLink.equals(dbLink)) {
                differences.add(new FieldDifference("moreInfoLink[" + i + "]", csvLink, dbLink, DiffType.CHANGED));
            }
        }
    }

    private void compareSegmentationsField(List<String> csvSegmentations, List<TourismSegmentation> dbSegmentations, List<FieldDifference> differences) {
        if (csvSegmentations.size() != dbSegmentations.size()) {
            differences.add(new FieldDifference("segmentations", csvSegmentations, dbSegmentations, DiffType.CHANGED));
            return;
        }

        for (int i = 0; i < csvSegmentations.size(); i++) {
            String csvSegmentation = csvSegmentations.get(i);
            String dbSegmentation = dbSegmentations.get(i).getName();
            if (!csvSegmentation.equals(dbSegmentation)) {
                differences.add(new FieldDifference("segmentation[" + i + "]", csvSegmentation, dbSegmentation, DiffType.CHANGED));
            }
        }
    }
}