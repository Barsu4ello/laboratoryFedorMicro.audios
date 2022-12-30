package com.cvetkov.fedor.laboratoryworkmicro.audios.repository;

import com.cvetkov.fedor.laboratoryworkmicro.entities.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
