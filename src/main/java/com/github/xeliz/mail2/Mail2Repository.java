package com.github.xeliz.mail2;

import com.github.xeliz.mail2.entities.Mail2;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;

@RepositoryDefinition(domainClass = Mail2.class, idClass = Long.class)
public interface Mail2Repository {

    void save(Mail2 mail2);

    @Query("from Mail2 m where m.from = :address or :address member of m.to")
    List<Mail2> findAllByAddress(@Param("address") final String address);
}
