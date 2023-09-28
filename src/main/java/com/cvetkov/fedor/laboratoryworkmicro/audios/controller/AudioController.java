package com.cvetkov.fedor.laboratoryworkmicro.audios.controller;

import com.cvetkov.fedor.laboratoryworkmicro.audios.service.AudioService;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.request.AudioRequest;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.request.UserAndAudioRequest;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.AudioResponse;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.update.AudioUpdate;
import com.cvetkov.fedor.laboratoryworkmicro.entities.model.Audio;
import com.cvetkov.fedor.laboratoryworkmicro.entities.model.UploadedByUsers;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/audio")
public class AudioController {

    private final AudioService audioService;

    @GetMapping
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Flux<AudioResponse> getAllAudios(@PageableDefault(size = 5) Pageable pageable) {
        return audioService.getAllPage(pageable);
    }

    @GetMapping("/all-audio")
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Flux<AudioResponse> getAllAudios() {
        return audioService.getAllList();
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Mono<AudioResponse> getAudioById(@PathVariable Long id) {
        return audioService.findById(id);
    }

    @PostMapping
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Mono<Audio> addAudio(@Valid @RequestBody AudioRequest audioRequest) {
        return audioService.save(audioRequest);
    }

    @PutMapping
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Mono<Audio> updateAudio(@Valid @RequestBody AudioUpdate audioUpdate) {
        return audioService.update(audioUpdate);
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Mono<Void> deleteAudio(@PathVariable Long id) {
        return audioService.deleteById(id);
    }


    @PostMapping("/add-audio")
     @PreAuthorize("hasAuthority('user')")
    public Flux<UploadedByUsers> addAudiosForUser(@Valid @RequestBody UserAndAudioRequest request, @AuthenticationPrincipal Jwt jwt) {

//        return audioService.addAudiosByIdForUser(request.getUserId(), request.getAudiosId());
        return audioService.addAudiosByIdForUser(jwt.getSubject(), request.getAudiosId());
    }

    @DeleteMapping("/delete-audio")
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Mono<Void> deleteAudiosForUser(@Valid @RequestBody UserAndAudioRequest request,  @AuthenticationPrincipal Jwt jwt) {
//       return audioService.deleteAudiosByIdForUser(request.getUserId(), request.getAudiosId());
       return audioService.deleteAudiosByIdForUser(jwt.getSubject(), request.getAudiosId());
    }

    @GetMapping("/all-aploaded-by-users")
    @PreAuthorize("hasAuthority('admin')")
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Flux<UploadedByUsers> getAllUploadedByUsers() {
       return audioService.getAllUploadedByUsers();

    }
}
