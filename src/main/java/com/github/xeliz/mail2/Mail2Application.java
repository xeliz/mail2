package com.github.xeliz.mail2;

import com.github.xeliz.mail2.services.Mail2ActionHandler;
import com.github.xeliz.mail2.types.Mail2RequestDTO;
import com.github.xeliz.mail2.types.Mail2ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Mail2Application {

	@Autowired
	private Mail2ActionHandler mail2ActionHandler;

	@PostMapping("${mail2.endpoint.path:/mail2}")
	public Mail2ResponseDTO mailEndpoint(@RequestBody Mail2RequestDTO mail2RequestDTO) {
		return mail2ActionHandler.dispatch(mail2RequestDTO);
	}

	public static void main(String[] args) {
		SpringApplication.run(Mail2Application.class, args);
	}
}
