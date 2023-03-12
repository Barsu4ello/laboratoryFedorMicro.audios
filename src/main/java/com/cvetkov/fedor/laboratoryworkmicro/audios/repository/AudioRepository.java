package com.cvetkov.fedor.laboratoryworkmicro.audios.repository;

import com.cvetkov.fedor.laboratoryworkmicro.entities.model.Audio;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AudioRepository extends ReactiveCrudRepository<Audio, Long> {

    Flux<Audio> findAllByAuthorId(Long authorId);

    Mono<Void> deleteAllByAuthorId(Long id);
}
