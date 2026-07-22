package com.example.ilovecoffee.exception;

import com.example.ilovecoffee.dto.error.ErrorResponse;
import com.example.ilovecoffee.service.slack.SlackNotificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final SlackNotificationService slackNotificationService;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> businessExceptionHandler(
            BusinessException e,
            HttpServletRequest request
    ) {
        String api = extractApi(request);
        String location = extractLocation(e);

        log.warn(
                "[비즈니스 예외] code={}, status={}, api={}, location={}, message={}",
                e.getCode(),
                e.getStatus().value(),
                api,
                location,
                e.getMessage()
        );

        ErrorResponse response = createResponse(
                e.getStatus(),
                e.getCode(),
                e.getMessage(),
                api,
                location
        );

        slackNotificationService.sendErrorNotification(response);

        return ResponseEntity
                .status(e.getStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validationExceptionHandler(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        String api = extractApi(request);
        String location = extractLocation(e);

        String fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> "%s=%s".formatted(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .reduce((left, right) -> left + ", " + right)
                .orElse("검증 오류 정보 없음");

        log.warn(
                "[DTO 검증 실패] api={}, location={}, errors={}",
                api,
                location,
                fieldErrors
        );

        ErrorResponse response = createResponse(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                "입력값이 올바르지 않습니다.",
                api,
                location
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> constraintViolationHandler(
            ConstraintViolationException e,
            HttpServletRequest request
    ) {
        String api = extractApi(request);
        String location = extractLocation(e);

        String violations = e.getConstraintViolations()
                .stream()
                .map(violation -> "%s=%s".formatted(
                        violation.getPropertyPath(),
                        violation.getMessage()
                ))
                .reduce((left, right) -> left + ", " + right)
                .orElse("검증 오류 정보 없음");

        log.warn(
                "[파라미터 검증 실패] api={}, location={}, violations={}",
                api,
                location,
                violations
        );

        ErrorResponse response = createResponse(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                "입력값이 올바르지 않습니다.",
                api,
                location
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> notDefinedExceptionHandler(
            Exception e,
            HttpServletRequest request
    ) {
        String api = extractApi(request);
        String location = extractLocation(e);

        log.error(
                "[예상하지 못한 예외] api={}, location={}, exception={}, message={}",
                api,
                location,
                e.getClass().getSimpleName(),
                e.getMessage(),
                e
        );

        ErrorResponse response = createResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "서버 내부 오류가 발생했습니다.",
                api,
                location
        );

        slackNotificationService.sendErrorNotification(response);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    private ErrorResponse createResponse(
            HttpStatus status,
            String code,
            String message,
            String api,
            String location
    ) {
        return ErrorResponse.of(
                status.value(),
                code,
                message,
                api,
                location,
                LocalDateTime.now()
        );
    }

    private String extractApi(HttpServletRequest request) {
        String queryString = request.getQueryString();

        String uri = queryString == null
                ? request.getRequestURI()
                : "%s?%s".formatted(request.getRequestURI(), queryString);

        return "%s %s".formatted(
                request.getMethod(),
                uri
        );
    }

    private String extractLocation(Throwable throwable) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();

        if (stackTrace.length == 0) {
            return "Unknown";
        }

        StackTraceElement element = stackTrace[0];

        return "%s.%s():%d".formatted(
                element.getClassName()
                        .substring(element.getClassName().lastIndexOf('.') + 1),
                element.getMethodName(),
                element.getLineNumber()
        );
    }
}