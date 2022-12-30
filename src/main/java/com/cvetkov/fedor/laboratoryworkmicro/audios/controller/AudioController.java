package com.cvetkov.fedor.laboratoryworkmicro.audios.controller;

import com.cvetkov.fedor.laboratoryworkmicro.audios.service.AudioService;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.request.AudioRequest;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.AudioResponse;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.update.AudioUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/audio")
public class AudioController {

    private final AudioService audioService;

    @GetMapping
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Page<AudioResponse> getAllAudios(@PageableDefault(size = 5) Pageable pageable) {
        return audioService.getAllPage(pageable);
    }

    @GetMapping("/all-audio")
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public List<AudioResponse> getAllAudios() {
        return audioService.getAllList();
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public AudioResponse getAudioById(@PathVariable Long id) {
        return audioService.findById(id);
    }

    @PostMapping
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public void addAudio(@Valid @RequestBody AudioRequest audioRequest) {
        audioService.save(audioRequest);
    }

    @PutMapping
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public void updateAudio(@Valid @RequestBody AudioUpdate audioUpdate) {
        audioService.update(audioUpdate);
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public void deleteAudio(@PathVariable Long id) {
        audioService.disableById(id);
    }
}
