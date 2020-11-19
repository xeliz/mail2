package com.github.xeliz.mail2;

import com.github.xeliz.mail2.entities.Mail2;
import com.github.xeliz.mail2.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Mail2ActionHandler {

    @Autowired
    private Mail2Repository mail2Repository;

    public Mail2ResponseDTO dispatch(Mail2RequestDTO request) {
        switch (request.getAction()) {
            case SEND:
                return send(request.getBody());
            case RECEIVE:
                return receive(request.getBody());
            case AUTH:
                return auth(request.getBody());
            default:
                return new Mail2ResponseDTO(Mail2ResponseStatusType.ERROR, "Unknown action: " + request.getAction());
        }
    }

    private Mail2ResponseDTO send(Mail2RequestBodyDTO body) {
        // https://www.baeldung.com/spring-5-webclient
		/* WebClient.create("http://localhost").get().retrieve().bodyToMono(String.class)
				.subscribe(s -> {
					System.out.println("Response: " + s);
				});*/
        mail2Repository.saveMail2(new Mail2(
                body.getFrom(),
                body.getTo(),
                body.getData()
        ));
        return new Mail2ResponseDTO(
                Mail2ResponseStatusType.OK,
                "saved"
        );
    }

    private Mail2ResponseDTO receive(Mail2RequestBodyDTO body) {
        return new Mail2ResponseDTO(
                Mail2ResponseStatusType.OK,
                "found mail2s",
                mail2Repository.findMailsByAddress(body.getToken())
                        .stream()
                        .map(mail2 -> new Mail2DTO(
                                mail2.getFrom(),
                                mail2.getTo(),
                                mail2.getData()
                        ))
                        .collect(Collectors.toList())
        );
    }

    private Mail2ResponseDTO auth(Mail2RequestBodyDTO body) {
        return new Mail2ResponseDTO(
                Mail2ResponseStatusType.OK,
                "authorized",
                body.getAddress()
        );
    }
}
