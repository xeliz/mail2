package com.github.xeliz.mail2.services;

import com.github.xeliz.mail2.entities.AccessToken;
import com.github.xeliz.mail2.repositories.AccessTokenRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class TokenUtils {

    @Value("${mail2.token.length:128}")
    private int tokenLength;

    @Autowired
    private AccessTokenRepository tokenRepository;

    public long getTokenLength() {
        return tokenLength;
    }

    public AccessToken createToken(final String address) {
        while (true) {
            final String token = RandomStringUtils.randomAlphanumeric(tokenLength);
            if (tokenRepository.existsByToken(token)) {
                continue;
            }
            AccessToken accessToken = new AccessToken();
            accessToken.setAddress(address);
            accessToken.setToken(token);
            return tokenRepository.save(accessToken);
        }
    }
}
