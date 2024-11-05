package br.ufpb.dcx.apps4society.meuguiapbapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class MeuguiaApiApplication {
	@Value("${app.version}")
	private String appVersion;

	public static void main(String[] args) {
		SpringApplication.run(MeuguiaApiApplication.class, args);
	}

	@GetMapping("/")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String home() {
		return "Bem vindo ao MeuGuiaPB API. Versão: " + appVersion;
	}
}
