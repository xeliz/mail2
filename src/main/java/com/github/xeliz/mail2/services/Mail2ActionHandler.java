package com.github.xeliz.mail2.services;

import com.github.xeliz.mail2.entities.Mail2;
import com.github.xeliz.mail2.entities.AccessToken;
import com.github.xeliz.mail2.repositories.Mail2Repository;
import com.github.xeliz.mail2.repositories.AccessTokenRepository;
import com.github.xeliz.mail2.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Mail2ActionHandler {

    @Autowired
    private Mail2Repository mail2Repository;

    @Autowired
    private AccessTokenRepository tokenRepository;

    @Autowired
    private TokenUtils tokenUtils;

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
            case VALIDATE_TOKEN:
                return validateToken(request.getBody());
            default:
                return new Mail2ResponseDTO(Mail2ResponseDTO.Mail2ResponseDTOStatus.ERROR, "Unknown action: " + request.getAction());
        }
    }

    private Mail2ResponseDTO send(Mail2RequestBodyDTO body) {
        final URL fromURL;
        try {
            fromURL = new URL(body.getFrom());
        } catch (final MalformedURLException e) {
            return new Mail2ResponseDTO(
                    Mail2ResponseDTO.Mail2ResponseDTOStatus.ERROR,
                    "invalid address format: from"
            );
        }
        final String fromSid = String.format(
                "%s://%s:%s%s",
                fromURL.getProtocol(),
                fromURL.getHost(),
                fromURL.getPort(),
                fromURL.getPath()
        );

        if (fromSid.equals(currentServerSid)) {
            return sendFromLocal(body);
        } else {
            return sendFromRemote(body);
        }
    }

    private Mail2ResponseDTO sendFromLocal(final Mail2RequestBodyDTO body) {
        final URL fromURL;
        try {
            fromURL = new URL(body.getFrom());
        } catch (final MalformedURLException e) {
            return new Mail2ResponseDTO(
                    Mail2ResponseDTO.Mail2ResponseDTOStatus.ERROR,
                    "invalid address format: from"
            );
        }
        final String fromSid = String.format(
                "%s://%s:%s%s",
                fromURL.getProtocol(),
                fromURL.getHost(),
                fromURL.getPort(),
                fromURL.getPath()
        );

        final URL localURL;
        try {
            localURL = new URL(currentServerSid);
        } catch (final MalformedURLException e) {
            return new Mail2ResponseDTO(
                    Mail2ResponseDTO.Mail2ResponseDTOStatus.ERROR,
                    "Internal server error: sid is not configured properly"
            );
        }
        final String localServerAddress = String.format(
                "%s://server@%s:%s%s",
                localURL.getProtocol(),
                localURL.getHost(),
                localURL.getPort(),
                localURL.getPath()
        );

        final Mail2 mail2 = new Mail2(
                body.getFrom(),
                body.getTo(),
                body.getData()
        );

        final Optional<AccessToken> accessTokenOptional = tokenRepository.findByToken(body.getToken());
        if (fromSid.equals(currentServerSid)) {
            if (accessTokenOptional.isEmpty() || !accessTokenOptional.get().getAddress().equals(mail2.getFrom()) ) {
                return new Mail2ResponseDTO(
                        Mail2ResponseDTO.Mail2ResponseDTOStatus.ERROR,
                        "invalid token"
                );
            }
        }

        final Map<String, List<String>> groupedTargetAddresses = new HashMap<>();
        for (final String targetAddress : mail2.getTo()) {
            try {
                final URL url = new URL(targetAddress);
                final String sid = String.format(
                        "%s://%s:%s%s",
                        url.getProtocol(),
                        url.getHost(),
                        url.getPort(),
                        url.getPath()
                );
                groupedTargetAddresses.putIfAbsent(sid, new LinkedList<>());
                groupedTargetAddresses.get(sid).add(targetAddress);
            } catch (final MalformedURLException e) {
                return new Mail2ResponseDTO(
                        Mail2ResponseDTO.Mail2ResponseDTOStatus.ERROR,
                        "invalid address format: to"
                );
            }
        }

        for (final String sid : groupedTargetAddresses.keySet()) {
            if (!sid.equals(currentServerSid)) {
                final Mail2RequestBodyDTO mail2RequestBodyDTO = new Mail2RequestBodyDTO();
                mail2RequestBodyDTO.setToken(tokenUtils.createToken(localServerAddress).getToken());
                mail2RequestBodyDTO.setFrom(mail2.getFrom());
                mail2RequestBodyDTO.setTo(mail2.getTo());
                mail2RequestBodyDTO.setData(mail2.getData());

                final Mail2RequestDTO requestDTO = new Mail2RequestDTO(
                        Mail2RequestDTO.Mail2RequestDTOAction.SEND,
                        mail2RequestBodyDTO
                );

                WebClient.create(sid).post()
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(requestDTO))
                        .retrieve()
                        .bodyToMono(Mail2ResponseDTO.class)
                        // TODO: a temporary solution. A better idea is to schedule request performing with an interval for N times.
                        .onErrorReturn(new Mail2ResponseDTO(Mail2ResponseDTO.Mail2ResponseDTOStatus.ERROR, "network error"))
                        .subscribe(mail2ResponseDTO -> {
                            if (mail2ResponseDTO.getStatus() == Mail2ResponseDTO.Mail2ResponseDTOStatus.ERROR) {
                                // mail2.setStatus(Mail2.Mail2Status.ERROR);
                            } else {
                                // mail2.setStatus(Mail2.Mail2Status.DELIVERED);
                            }
                            // mail2Repository.save(mail2);
                        });
            }
        }

        mail2Repository.save(mail2);

        return new Mail2ResponseDTO(
                Mail2ResponseDTO.Mail2ResponseDTOStatus.OK,
                "sent"
        );
    }

    private Mail2ResponseDTO sendFromRemote(final Mail2RequestBodyDTO body) {
        final URL url;
        try {
            url = new URL(body.getFrom());
        } catch (MalformedURLException e) {
            return new Mail2ResponseDTO(
                    Mail2ResponseDTO.Mail2ResponseDTOStatus.ERROR,
                    "invalid url format: from"
            );
        }
        final String sid = String.format(
                "%s://%s:%s%s",
                url.getProtocol(),
                url.getHost(),
                url.getPort(),
                url.getPath()
        );
        final String remoteServerAddress = String.format(
                "%s://server@%s:%s%s",
                url.getProtocol(),
                url.getHost(),
                url.getPort(),
                url.getPath()
        );

        final Mail2 mail2 = new Mail2(
                body.getFrom(),
                body.getTo(),
                body.getData()
        );

        final Mail2RequestBodyDTO mail2RequestBodyDTO = new Mail2RequestBodyDTO();
        mail2RequestBodyDTO.setToken(body.getToken());
        mail2RequestBodyDTO.setAddress(remoteServerAddress);

        final Mail2RequestDTO requestDTO = new Mail2RequestDTO(
                Mail2RequestDTO.Mail2RequestDTOAction.VALIDATE_TOKEN,
                mail2RequestBodyDTO
        );

        WebClient.create(sid).post()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestDTO))
                .retrieve()
                .bodyToMono(Mail2ResponseDTO.class)
                // TODO: a temporary solution. A better idea is to schedule request performing with an interval for N times.
                .onErrorReturn(new Mail2ResponseDTO(Mail2ResponseDTO.Mail2ResponseDTOStatus.ERROR, "network error"))
                .subscribe(mail2ResponseDTO -> {
                    if (mail2ResponseDTO.getStatus() == Mail2ResponseDTO.Mail2ResponseDTOStatus.OK) {
                        mail2Repository.save(mail2);
                    }
                });

        return new Mail2ResponseDTO(
                Mail2ResponseDTO.Mail2ResponseDTOStatus.OK,
                "sent"
        );
    }

    private Mail2ResponseDTO receive(Mail2RequestBodyDTO body) {
        final Optional<AccessToken> accessTokenOptional = tokenRepository.findByToken(body.getToken());
        if (accessTokenOptional.isEmpty()) {
            return new Mail2ResponseDTO(
                    Mail2ResponseDTO.Mail2ResponseDTOStatus.ERROR,
                    "invalid token"
            );
        }
        final String address = accessTokenOptional.get().getAddress();

        return new Mail2ResponseDTO(
                Mail2ResponseDTO.Mail2ResponseDTOStatus.OK,
                "found mail2s",
                mail2Repository.findAllByAddress(address)
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
        final URL url;
        try {
            url = new URL(body.getAddress());
        } catch (final MalformedURLException e) {
            return new Mail2ResponseDTO(
                    Mail2ResponseDTO.Mail2ResponseDTOStatus.ERROR,
                    "invalid address format"
            );
        }

        if (url.getUserInfo().equals("server")) {
            return new Mail2ResponseDTO(
                    Mail2ResponseDTO.Mail2ResponseDTOStatus.ERROR,
                    "not authroized"
            );
        }

        // TODO: every login/password is correct
        return new Mail2ResponseDTO(
                Mail2ResponseDTO.Mail2ResponseDTOStatus.OK,
                "authorized",
                tokenUtils.createToken(body.getAddress()).getToken()
        );
    }

    private Mail2ResponseDTO validateToken(Mail2RequestBodyDTO body) {
        final Optional<AccessToken> accessTokenOptional = tokenRepository.findByToken(body.getToken());
        if (accessTokenOptional.isPresent() && accessTokenOptional.get().getAddress().equals(body.getAddress())) {
            return new Mail2ResponseDTO(
                    Mail2ResponseDTO.Mail2ResponseDTOStatus.OK,
                    "token is valid"
            );
        }
        return new Mail2ResponseDTO(
                Mail2ResponseDTO.Mail2ResponseDTOStatus.ERROR,
                "Invalid token"
        );
    }
}
