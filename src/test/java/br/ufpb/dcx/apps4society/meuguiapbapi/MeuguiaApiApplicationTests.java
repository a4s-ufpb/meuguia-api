package br.ufpb.dcx.apps4society.meuguiapbapi;

import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MockAuthentication;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.UserRequestUtil;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class MeuguiaApiApplicationTests {
	public final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyaWQiLCJuYW1lIjoiSm9obiBEb2UifQ.invalidsignature";
	public final Long INVALID_ID = -1L;

	public final MockAuthentication mockAuthentication = new MockAuthentication();
	public final UserRequestUtil userRequestUtil = new UserRequestUtil();

    public static int port;
    public static String baseURI;
    public static String basePath;

	@BeforeAll
	public void _setUp() {
		log.info("Setting up RestAssured");
        log.debug("Port: {}", port);
        log.debug("BaseURI: {}", baseURI);
        log.debug("BasePath: {}", basePath);
		RestAssured.port = port;
		RestAssured.baseURI = baseURI;
		RestAssured.basePath = basePath;
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
}
