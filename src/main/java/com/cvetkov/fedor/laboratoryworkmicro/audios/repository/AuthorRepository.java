package com.cvetkov.fedor.laboratoryworkmicro.audios.repository;

import com.cvetkov.fedor.laboratoryworkmicro.entities.model.Author;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AuthorRepository extends ReactiveCrudRepository<Author, Long> {
}
