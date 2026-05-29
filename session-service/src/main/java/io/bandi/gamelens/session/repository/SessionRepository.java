package io.bandi.gamelens.session.repository;

import io.bandi.gamelens.session.domain.model.Session;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends ListCrudRepository<Session, Long> {

}
