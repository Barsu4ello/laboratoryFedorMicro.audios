package com.cvetkov.fedor.laboratoryworkmicro.audios.repository;

import com.cvetkov.fedor.laboratoryworkmicro.entities.model.Audio;
import com.cvetkov.fedor.laboratoryworkmicro.entities.model.UploadedByUsers;
import com.cvetkov.fedor.laboratoryworkmicro.entities.model.UploadedByUsersPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UploadedByUsersRepository extends JpaRepository<UploadedByUsers, UploadedByUsersPK> {

    void deleteByAudioId(Long id);

    @Modifying
    @Query("delete from UploadedByUsers u where u.audioId in :audiosId")
    void deleteAllByAudioIds(@Param("audiosId") List<Long> audiosId);
}
