package com.cvetkov.fedor.laboratoryworkmicro.audios.service;

import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.request.AuthorRequest;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.AuthorResponse;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.update.AuthorUpdate;
import com.cvetkov.fedor.laboratoryworkmicro.entities.model.Author;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthorService {
    Flux<AuthorResponse> getAllPage(Pageable pageable);

    Flux<AuthorResponse> getAllList();

    Mono<AuthorResponse> findById(Long id);

    Mono<Author> save(AuthorRequest authorRequest);

    Mono<Author> update(AuthorUpdate authorUpdate);

    Mono<Void> deleteById(Long id);
}
