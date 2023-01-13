package com.cvetkov.fedor.laboratoryworkmicro.audios.service.impl;

import com.cvetkov.fedor.laboratoryworkmicro.audios.feign.UserFeignClient;
import com.cvetkov.fedor.laboratoryworkmicro.audios.repository.AudioRepository;
import com.cvetkov.fedor.laboratoryworkmicro.audios.repository.UploadedByUsersRepository;
import com.cvetkov.fedor.laboratoryworkmicro.audios.service.AudioService;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.request.AudioRequest;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.AudioResponse;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.update.AudioUpdate;
import com.cvetkov.fedor.laboratoryworkmicro.entities.mapper.AudioMapper;
import com.cvetkov.fedor.laboratoryworkmicro.entities.model.Audio;
import com.cvetkov.fedor.laboratoryworkmicro.entities.model.UploadedByUsers;
import com.cvetkov.fedor.laboratoryworkmicro.utils.exception.ExceptionResponseStatusChecker;
import com.cvetkov.fedor.laboratoryworkmicro.utils.exception.ObjectNotFoundException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AudioServiceImpl implements AudioService {
    private final AudioRepository audioRepository;
    private final AudioMapper audioMapper;
    private final UserFeignClient userFeignClient;
    private final UploadedByUsersRepository uploadedByUsersRepository;

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
    @Transactional
    public void deleteById(Long id) {
        uploadedByUsersRepository.deleteByAudioId(id);
        audioRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addAudiosByIdForUser(Long userId, List<Long> audiosId) {
        // Проверим есть такой пользователь в микросервисе users, иначе будет FeignException
        userFeignClient.getUserById(userId);

        List<UploadedByUsers> uploadedByUsers = new ArrayList<>();
        for (Long audioId : audiosId) {
            uploadedByUsers.add(new UploadedByUsers(userId, audioId));
        }

        uploadedByUsersRepository.saveAll(uploadedByUsers);
    }

    @Override
    public void deleteAudiosByIdForUser(Long userId, List<Long> audiosId) {
        // Проверим есть такой пользователь в микросервисе users, иначе будет FeignException

        userFeignClient.getUserById(userId);

        List<UploadedByUsers> uploadedByUsers = new ArrayList<>();
        for (Long audioId : audiosId) {
            uploadedByUsers.add(new UploadedByUsers(userId, audioId));
        }

        uploadedByUsersRepository.deleteAll(uploadedByUsers);
    }

    @Override
    public void deleteByAuthorId(Long id) {
        List<Long> audiosId = audioRepository.findIdByAuthorId(id);
        uploadedByUsersRepository.deleteAllByAudioIds(audiosId);
        audioRepository.deleteAllByAuthorId(id);
    }
}
