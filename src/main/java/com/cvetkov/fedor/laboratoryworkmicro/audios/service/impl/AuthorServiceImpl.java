package com.cvetkov.fedor.laboratoryworkmicro.audios.service.impl;

import com.cvetkov.fedor.laboratoryworkmicro.audios.feign.UserFeignClient;
import com.cvetkov.fedor.laboratoryworkmicro.audios.repository.AuthorRepository;
import com.cvetkov.fedor.laboratoryworkmicro.audios.service.AudioService;
import com.cvetkov.fedor.laboratoryworkmicro.audios.service.AuthorService;
import com.cvetkov.fedor.laboratoryworkmicro.audios.webClient.UserWebClient;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.request.AuthorRequest;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.AuthorResponse;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.update.AuthorUpdate;
import com.cvetkov.fedor.laboratoryworkmicro.entities.model.Author;
import com.cvetkov.fedor.laboratoryworkmicro.utils.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AudioService audioService;
    private final UserFeignClient userFeignClient;
    private final UserWebClient webClient;


    @Override
    public Flux<AuthorResponse> getAllPage(Pageable pageable) {
        return authorRepository.findAll().map(author -> {
                    AuthorResponse authorResponse = new AuthorResponse();
                    authorResponse.setId(author.getId());
                    authorResponse.setName(author.getName());
                    authorResponse.setDescription(author.getDescription());
                    return authorResponse;
                })
                .skip(pageable.getPageNumber() * pageable.getPageSize())
                .take(pageable.getPageSize());
    }

    @Override
    public Flux<AuthorResponse> getAllList() {
        return authorRepository.findAll().map(author -> {
            AuthorResponse authorResponse = new AuthorResponse();
            authorResponse.setId(author.getId());
            authorResponse.setName(author.getName());
            authorResponse.setDescription(author.getDescription());
            return authorResponse;
        });
    }

    @Override
    public Mono<AuthorResponse> findById(Long id) {
        return authorRepository
                .findById(id).map(author -> {
                    AuthorResponse authorResponse = new AuthorResponse();
                    authorResponse.setId(author.getId());
                    authorResponse.setName(author.getName());
                    authorResponse.setDescription(author.getDescription());
                    return authorResponse;
                })
                .switchIfEmpty(Mono.error(new ObjectNotFoundException("Author with id " + id + " not found")));
    }

    @Override
    public Mono<Author> save(AuthorRequest authorRequest) {
        Author author = new Author();
        author.setId(authorRequest.getId());
        author.setName(authorRequest.getName());
        author.setDescription(authorRequest.getDescription());
        return authorRepository.save(author);
    }

    @Override
    public Mono<Author> update(AuthorUpdate authorUpdate) {
        Author author = new Author();
        author.setId(authorUpdate.getId());
        author.setName(authorUpdate.getName());
        author.setDescription(authorUpdate.getDescription());
        return authorRepository.save(author);
    }

    //For h2 db @Transactional works
//    @Override
//    @Transactional
//    public Mono<Void> deleteById(Long id) {
//        return webClient.changeAuthorIdToNull(id)
//                .then(audioService.deleteByAuthorId(id))
//                .then(authorRepository.deleteById(id));
//    }

//    //But For postgresql db @Transactional doesn't work
//    @Transactional
    public Mono<Void> deleteById(Long id) {

        return webClient.changeAuthorIdToNull(id)
//                .then(audioService.deleteByAuthorId(id))
                .then(authorRepository.deleteById(id));
    }


}
