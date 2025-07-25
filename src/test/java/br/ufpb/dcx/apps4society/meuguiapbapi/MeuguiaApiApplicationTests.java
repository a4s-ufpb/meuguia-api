package br.ufpb.dcx.apps4society.meuguiapbapi;

import br.ufpb.dcx.apps4society.meuguiapbapi.helper.UserTestsHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.UserTestsHelper.delete;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MeuguiaApiApplicationTests {
    public static final Long INVALID_ID = 999999L;
    public static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyaWQiLCJuYW1lIjoiSm9obiBEb2UifQ.invalidsignature";
    public static int port;
    public static String baseURI;
    public static String basePath;
    public final Logger log = LoggerFactory.getLogger(MeuguiaApiApplicationTests.class);
    @Autowired
    private UserTestsHelper userTestsHelper;
    private String adminToken;
    private String defaultToken;

    @BeforeAll
    public void _setUp() {
        log.info("Setting up");
        this.adminToken = userTestsHelper.createAndAuthenticateUserWithAdminPermission();
        this.defaultToken = userTestsHelper.createAndAuthenticateUserWithDefaultPermissions();

        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                (type, s) -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.LowerCamelCaseStrategy());
                    return objectMapper;
                })
        );

        log.debug("Port: {}", port);
        log.debug("BaseURI: {}", baseURI);
        log.debug("BasePath: {}", basePath);
        RestAssured.port = port;
        RestAssured.baseURI = baseURI;
        RestAssured.basePath = basePath;
    }

    @AfterAll
    public void _tearDown() {
        log.info("Tearing down");
        delete(adminToken);
        delete(defaultToken);
    }

    @Value("${spring.test.server.port}")
    public void _setStaticPort(int value) {
        port = value;
    }

    @Value("${spring.test.server.baseURI}")
    public void _setStaticBaseURI(String value) {
        baseURI = value;
    }

    @Value("${spring.test.base-path}")
    public void _setBasePath(String value) {
        basePath = value;
    }

    public Logger getLog() {
        return log;
    }

    public UserTestsHelper getUserTestsHelper() {
        return userTestsHelper;
    }

    public String getAdminToken() {
        return adminToken;
    }

    public String getDefaultToken() {
        return defaultToken;
    }
}
