package com.cvetkov.fedor.laboratoryworkmicro.audios.service.impl;

import com.cvetkov.fedor.laboratoryworkmicro.audios.repository.AudioRepository;
import com.cvetkov.fedor.laboratoryworkmicro.audios.service.AudioService;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.request.AudioRequest;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.AudioResponse;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.update.AudioUpdate;
import com.cvetkov.fedor.laboratoryworkmicro.entities.mapper.AudioMapper;
import com.cvetkov.fedor.laboratoryworkmicro.utils.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AudioServiceImpl implements AudioService {
    private final AudioRepository audioRepository;
    private final AudioMapper audioMapper;

    @Override
    public Page<AudioResponse> getAllPage(Pageable pageable) {
        return audioMapper.audioToAudioResponsePage(audioRepository.findAll(pageable));
    }

    @Override
    public List<AudioResponse> getAllList() {
        return audioMapper.audioToAudioResponseList(audioRepository.findAll());
    }

    @Override
    public AudioResponse findById(Long id) {
        return audioMapper
                .audioToAudioResponse(audioRepository
                        .findById(id)
                        .orElseThrow(() -> new ObjectNotFoundException("Audio with id " + id + " not found")));
    }

    @Override
    public void save(AudioRequest audioRequest) {
        audioRepository.save(audioMapper.audioRequestToAudio(audioRequest));
    }

    @Override
    public void update(AudioUpdate audioUpdate) {
        audioRepository.save(audioMapper.audioUpdateToAudio(audioUpdate));
    }

    @Override
    public void disableById(Long id) {
        audioRepository.deleteById(id);
    }
}
