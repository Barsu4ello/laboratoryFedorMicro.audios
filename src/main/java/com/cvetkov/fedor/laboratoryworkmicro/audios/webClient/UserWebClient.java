package com.cvetkov.fedor.laboratoryworkmicro.audios.webClient;

import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.UserResponse;
import com.cvetkov.fedor.laboratoryworkmicro.utils.exception.ExceptionResponseStatusChecker;
import com.cvetkov.fedor.laboratoryworkmicro.utils.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserWebClient {
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    WebClient webClient;

    private static final String baseUrl = "http://localhost:8765/api/v1/user/";

    @PostConstruct
    public void setWebClient() {
        webClient = WebClient.create(baseUrl);
    }

    public Mono<Void> changeAuthorIdToNull(Long authorId) {

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("userServiceCircuitBreaker", "userServiceCircuitBreaker");

            return webClient
                    .put()
                    .uri("change-author-id/{authorId}", authorId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorResume(this::changeAuthorUnavailable)
                    .transformDeferred(CircuitBreakerOperator.of(circuitBreaker));
    }

    public Mono<Void> changeAuthorUnavailable(Throwable e) {
        throw new ServiceUnavailableException("User server is not available. Check user application", e);
    }

    public void getUserById(Long id) {

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("userServiceCircuitBreaker", "userServiceCircuitBreaker");

        webClient
                    .get()
                    .uri("{id}", id)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .onErrorResume(e -> {
                        getUserByIdUnavailable(e);
                        return Mono.empty();
                    })
                    .transformDeferred(CircuitBreakerOperator.of(circuitBreaker)).block();
    }

    public void getUserByIdUnavailable(Throwable e) {
        ExceptionResponseStatusChecker.check404StatusAndExceptionFeignType("User", e);
        throw new ServiceUnavailableException("User server is not available", e);
    }
}
