package com.github.xeliz.mail2;

import com.github.xeliz.mail2.entities.Mail2;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A temporary in-memory and non-efficient implementation of mail2 repository
 */
@Repository
public class Mail2Repository {

    private final List<Mail2> mail2List = new LinkedList<>();

    public synchronized void saveMail2(Mail2 mail2) {
        mail2List.add(mail2);
    }

    public synchronized List<Mail2> findMailsByAddress(final String address) {
        return mail2List
                .stream()
                .filter(mail2 -> mail2.getFrom().equals(address) || mail2.getTo().contains(address))
                .collect(Collectors.toList());
    }
}
