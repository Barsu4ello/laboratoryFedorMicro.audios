package com.cvetkov.fedor.laboratoryworkmicro.audios.service;

import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.request.AudioRequest;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.AudioResponse;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.update.AudioUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AudioService {
    Page<AudioResponse> getAllPage(Pageable pageable);

    List<AudioResponse> getAllList();

    AudioResponse findById(Long id);

    void save(AudioRequest audioRequest);

    void update(AudioUpdate audioUpdate);

    void disableById(Long id);
}
