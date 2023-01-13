package com.cvetkov.fedor.laboratoryworkmicro.audios.repository;

import com.cvetkov.fedor.laboratoryworkmicro.entities.model.Audio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AudioRepository extends JpaRepository<Audio, Long> {

    void deleteAllByAuthorId(Long id);

    @Query(value = "select a.id from audios.audios a where a.author_id = :authorId", nativeQuery = true)
    List<Long> findIdByAuthorId(@Param("authorId") Long authorId);
}
