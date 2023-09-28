package com.cvetkov.fedor.laboratoryworkmicro.audios.service.impl;

import com.cvetkov.fedor.laboratoryworkmicro.audios.feign.UserFeignClient;
import com.cvetkov.fedor.laboratoryworkmicro.audios.repository.AudioRepository;
import com.cvetkov.fedor.laboratoryworkmicro.audios.repository.UploadedByUsersRepository;
import com.cvetkov.fedor.laboratoryworkmicro.audios.service.AudioService;
import com.cvetkov.fedor.laboratoryworkmicro.audios.webClient.UserWebClient;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.request.AudioRequest;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.AudioResponse;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.update.AudioUpdate;
import com.cvetkov.fedor.laboratoryworkmicro.entities.model.Audio;
import com.cvetkov.fedor.laboratoryworkmicro.entities.model.UploadedByUsers;
import com.cvetkov.fedor.laboratoryworkmicro.utils.exception.ObjectNotFoundException;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AudioServiceImpl implements AudioService {
    private final AudioRepository audioRepository;
    private final UploadedByUsersRepository uploadedByUsersRepository;
    private final UserFeignClient userFeignClient;
//    private final UserWebClient webClient;

    private final TransactionalOperator operator;
    private final ConnectionFactory connectionFactory;

    @Override
    public Flux<AudioResponse> getAllPage(Pageable pageable) {
        return audioRepository.findAll().map(audio -> {
                    AudioResponse audioResponse = new AudioResponse();
                    audioResponse.setId(audio.getId());
                    audioResponse.setTitle(audio.getTitle());
                    audioResponse.setAuthor(audio.getAuthorId());
                    return audioResponse;
                })
                .skip(pageable.getPageNumber() * pageable.getPageSize())
                .take(pageable.getPageSize());
    }

    @Override
    public Flux<AudioResponse> getAllList() {
        return audioRepository.findAll().map(audio -> {
            AudioResponse audioResponse = new AudioResponse();
            audioResponse.setId(audio.getId());
            audioResponse.setTitle(audio.getTitle());
            audioResponse.setAuthor(audio.getAuthorId());
            return audioResponse;
        });
    }

    @Override
    public Mono<AudioResponse> findById(Long id) {
        return audioRepository
                .findById(id)
                .map(audio -> {
                    AudioResponse audioResponse = new AudioResponse();
                    audioResponse.setId(audio.getId());
                    audioResponse.setTitle(audio.getTitle());
                    audioResponse.setAuthor(audio.getAuthorId());
                    return audioResponse;
                })
                .switchIfEmpty(Mono.error(new ObjectNotFoundException("Audio with id " + id + " not found")));
    }

    @Override
    public Mono<Audio> save(AudioRequest audioRequest) {
        Audio audio = new Audio();
        audio.setId(audioRequest.getId());
        audio.setTitle(audioRequest.getTitle());
        audio.setAuthorId(audioRequest.getAuthorId());

        return audioRepository.save(audio);
    }

    @Override
    public Mono<Audio> update(AudioUpdate audioUpdate) {
        Audio audio = new Audio();
        audio.setId(audioUpdate.getId());
        audio.setTitle(audioUpdate.getTitle());
        audio.setAuthorId(audioUpdate.getAuthorId());

        return audioRepository.save(audio);
    }

    @Override
//    @Transactional
    public Mono<Void> deleteById(Long id) {
//        return uploadedByUsersRepository.deleteAllByAudioId(id)
//                .then(audioRepository.deleteById(id));
        return audioRepository.deleteById(id);
    }

    @Override
//    @Transactional
//    public Flux<UploadedByUsers> addAudiosByIdForUser(Long userId, List<Long> audiosId) {
    public Flux<UploadedByUsers> addAudiosByIdForUser(String userId, List<Long> audiosId) {
//        Проверим есть такой пользователь в микросервисе users, иначе будет FeignException (c KeyCloak это не надо)
//      webClient.getUserById(userId);



        List<UploadedByUsers> uploadedByUsers = new ArrayList<>();
        for (Long audioId : audiosId) {
            uploadedByUsers.add(new UploadedByUsers(userId, audioId));
        }

        return uploadedByUsersRepository.saveAll(uploadedByUsers);
    }

    @Override
//    public Mono<Void> deleteAudiosByIdForUser(Long userId, List<Long> audiosId) {
    public Mono<Void> deleteAudiosByIdForUser(String userId, List<Long> audiosId) {
        return uploadedByUsersRepository.deleteAllByUserIdAndAudioIds(userId, audiosId);
    }

    //h2
//    @Override
//    @Transactional
//    public Mono<Void> deleteByAuthorId(Long id) {
//        Flux<Long> audiosId = audioRepository.findAllByAuthorId(id).map(Audio::getId);
//        return uploadedByUsersRepository.deleteAllByAudioIds(audiosId.)
//                .then(audioRepository.deleteAllByAuthorId(id));
//    }

    //postgresql
    //Используем каскадное удаление на уровне бд для постгреса. Тогда метод не нужен.
    @Override
    public Mono<Void> deleteByAuthorId(Long id) {
        Flux<Long> audiosId = audioRepository.findAllByAuthorId(id).map(Audio::getId);
        return uploadedByUsersRepository.deleteAllByAudioIds(audiosId)
                .then(audioRepository.deleteAllByAuthorId(id));

        //Вариант для транзакции
//        return Mono.from(connectionFactory.create())
//                .flatMap(c -> Mono.from(c.beginTransaction())
//                .then(Mono.from(c.createStatement("delete from audios.uploaded_by_users u where u.audio_id in ($1)")
//                        .bind("$1", audiosId.get(0))
//                        .execute())
//                        .then(Mono.from(c.createStatement("delete from audios.audios u where u.author_id in ($1)")
//                                .bind("$1", id)
//                                .execute())
//                        ))
//                        .delayUntil(r -> c.commitTransaction())
//                        .doFinally(st -> c.close()))
//                .flatMap(o -> Mono.empty());

//                .as(operator::transactional);)
    }


    @Override
    public Flux<UploadedByUsers> getAllUploadedByUsers() {
        return uploadedByUsersRepository.findAll();
    }
}
