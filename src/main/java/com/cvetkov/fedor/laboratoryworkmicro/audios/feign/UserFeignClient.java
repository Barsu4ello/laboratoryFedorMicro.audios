package com.cvetkov.fedor.laboratoryworkmicro.audios.feign;

import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.UserResponse;
import com.cvetkov.fedor.laboratoryworkmicro.utils.exception.ExceptionResponseStatusChecker;
import com.cvetkov.fedor.laboratoryworkmicro.utils.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "users-client")
public interface UserFeignClient {

    @CircuitBreaker(name = "userServiceCircuitBreaker", fallbackMethod = "getUserByIdUnavailable")
    @GetMapping("/api/v1/user/{id}")
    UserResponse getUserById(@PathVariable Long id);

    @CircuitBreaker(name = "userServiceCircuitBreaker", fallbackMethod = "changeAuthorUnavailable")
    @PutMapping("/api/v1/user/change-author-id/{authorId}")
    void changeAuthorIdToNull(@PathVariable Long authorId);

//    @CircuitBreaker(name = "userServiceCircuitBreaker", fallbackMethod = "changeAuthorUnavailable")
//    @PutMapping("/api/v1/user/change-author-id/{authorId}")
//    Mono<Void> changeAuthorIdToNull(@PathVariable Long authorId);

    default UserResponse getUserByIdUnavailable(Exception e) {
        ExceptionResponseStatusChecker.check404StatusAndExceptionFeignType("User", e);
        throw new ServiceUnavailableException("User server is not available", e);
    }

    default void changeAuthorUnavailable(Exception e){
        throw new ServiceUnavailableException("User server is not available", e);
    }
}

