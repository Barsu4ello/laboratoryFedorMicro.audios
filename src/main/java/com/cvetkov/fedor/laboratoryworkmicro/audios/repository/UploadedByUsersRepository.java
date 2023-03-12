package com.cvetkov.fedor.laboratoryworkmicro.audios.repository;

import com.cvetkov.fedor.laboratoryworkmicro.entities.model.UploadedByUsers;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UploadedByUsersRepository extends ReactiveCrudRepository<UploadedByUsers, Long> {

    Mono<Void> deleteAllByAudioId(Long id);

    @Modifying
    @Query("delete from audios.uploaded_by_users u where u.audio_id in (:audiosId)")// work with h2
    Mono<Void> deleteAllByAudioIds(Flux<Long> audiosId);


    @Modifying
    @Query("delete from audios.uploaded_by_users u where u.user_id = :userId and u.audio_id in (:audioIds)")
    Mono<Void> deleteAllByUserIdAndAudioIds(Long userId, List<Long> audioIds);
}
