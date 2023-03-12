package com.cvetkov.fedor.laboratoryworkmicro.audios.service;

import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.request.AudioRequest;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.AudioResponse;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.update.AudioUpdate;
import com.cvetkov.fedor.laboratoryworkmicro.entities.model.Audio;
import com.cvetkov.fedor.laboratoryworkmicro.entities.model.UploadedByUsers;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AudioService {
    Flux<AudioResponse> getAllPage(Pageable pageable);

    Flux<AudioResponse> getAllList();

    Mono<AudioResponse> findById(Long id);

    Mono<Audio> save(AudioRequest audioRequest);

    Mono<Audio> update(AudioUpdate audioUpdate);

    Mono<Void> deleteById(Long id);

    Flux<UploadedByUsers> addAudiosByIdForUser(Long userId, List<Long> audiosId);

    Mono<Void> deleteAudiosByIdForUser(Long userId, List<Long> audiosId);

    Mono<Void> deleteByAuthorId(Long id);

    Flux<UploadedByUsers> getAllUploadedByUsers();
}
