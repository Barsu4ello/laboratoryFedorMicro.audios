package com.cvetkov.fedor.laboratoryworkmicro.audios.controller;

import com.cvetkov.fedor.laboratoryworkmicro.audios.service.AuthorService;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.request.AuthorRequest;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.AuthorResponse;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.update.AuthorUpdate;
import com.cvetkov.fedor.laboratoryworkmicro.entities.model.Author;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/author")
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Flux<AuthorResponse> getAllAuthors(@PageableDefault(size = 5) Pageable pageable) {
        return authorService.getAllPage(pageable);
    }

    @GetMapping("/all-author")
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Flux<AuthorResponse> getAllAuthors() {
        return authorService.getAllList();
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Mono<AuthorResponse> getAuthorById(@PathVariable Long id) {
        return authorService.findById(id);
    }

    @PostMapping
    // @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<Author> addAuthor(@Valid @RequestBody AuthorRequest authorRequest) {
        return authorService.save(authorRequest);
    }

    @PutMapping
    // @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<Author> updateAuthor(@Valid @RequestBody AuthorUpdate authorUpdate) {
        return authorService.update(authorUpdate);
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasAuthority('ADMIN')")
//    @Transactional
    public Mono<Void> deleteAuthor(@PathVariable Long id) {
        return authorService.deleteById(id);
    }
}
