package com.github.xeliz.mail2;

import com.github.xeliz.mail2.entities.Mail2;
import com.github.xeliz.mail2.entities.Mail2Status;
import com.github.xeliz.mail2.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class Mail2ActionHandler {

    @Autowired
    private Mail2Repository mail2Repository;

    @Value("${mail2.sid}")
    private String currentServerSid;

    public Mail2ResponseDTO dispatch(Mail2RequestDTO request) {
        switch (request.getAction()) {
            case SEND:
                return send(request.getBody());
            case RECEIVE:
                return receive(request.getBody());
            case AUTH:
                return auth(request.getBody());
            default:
                return new Mail2ResponseDTO(Mail2ResponseDTOStatus.ERROR, "Unknown action: " + request.getAction());
        }
    }

    private Mail2ResponseDTO send(Mail2RequestBodyDTO body) {
        final Map<String, List<String>> groupedTargetAddresses = new HashMap<>();
        for (final String targetAddress : body.getTo()) {
            try {
                final URL url = new URL(targetAddress);
                final String sid = String.format(
                        "%s://%s:%s%s",
                        url.getProtocol(),
                        url.getHost(),
                        url.getPort(),
                        url.getPath()
                );

                if (!groupedTargetAddresses.containsKey(sid)) {
                    groupedTargetAddresses.put(sid, new LinkedList<>());
                }

                groupedTargetAddresses.get(sid).add(targetAddress);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
        for (final String sid : groupedTargetAddresses.keySet()) {
            if (sid.equals(currentServerSid)) {
                mail2Repository.saveMail2(new Mail2(
                        body.getFrom(),
                        body.getTo(),
                        body.getData(),
                        Mail2Status.DELIVERED
                ));
            } else {
                final Mail2 mail2 = new Mail2(
                        body.getFrom(),
                        body.getTo(),
                        body.getData(),
                        Mail2Status.PENDING
                );
                mail2Repository.saveMail2(mail2);

                final Mail2RequestDTO requestDTO = new Mail2RequestDTO(
                        Mail2RequestDTOAction.SEND,
                        new Mail2RequestBodyDTO(
                                body.getToken(),
                                body.getFrom(),
                                body.getTo(),
                                body.getData()
                        )
                );
                WebClient.create(sid).post()
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(requestDTO))
                        .retrieve()
                        .bodyToMono(Mail2ResponseDTO.class)
                        .subscribe(mail2ResponseDTO -> {
                            if (mail2ResponseDTO.getStatus() == Mail2ResponseDTOStatus.ERROR) {
                                // TODO: set error status
                            } else {
                                // TODO: set delivered status
                            }
                        });
            }
        }
        return new Mail2ResponseDTO(
                Mail2ResponseDTOStatus.OK,
                "sent"
        );
    }

    private Mail2ResponseDTO receive(Mail2RequestBodyDTO body) {
        return new Mail2ResponseDTO(
                Mail2ResponseDTOStatus.OK,
                "found mail2s",
                mail2Repository.findMailsByAddress(body.getToken())
                        .stream()
                        .map(mail2 -> new Mail2DTO(
                                mail2.getFrom(),
                                mail2.getTo(),
                                mail2.getData(),
                                mail2.getStatus()
                        ))
                        .collect(Collectors.toList())
        );
    }

    private Mail2ResponseDTO auth(Mail2RequestBodyDTO body) {
        return new Mail2ResponseDTO(
                Mail2ResponseDTOStatus.OK,
                "authorized",
                body.getAddress()
        );
    }
}
