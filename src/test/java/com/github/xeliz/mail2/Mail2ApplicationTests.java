package com.github.xeliz.mail2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xeliz.mail2.types.Mail2RequestBodyDTO;
import com.github.xeliz.mail2.types.Mail2RequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class Mail2ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${mail2.endpoint.path:/mail2}")
    private String path;

    @Test
    void contextLoads() throws Exception {
        mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new Mail2RequestDTO(
                                Mail2RequestDTO.Mail2RequestDTOAction.AUTH,
                                new Mail2RequestBodyDTO(
                                        "http://alice@localhost:8080/mail2"
                                )
                        )
                )))
                .andExpect(status().isOk());
    }
}
