package uz.geeks.springsecurity_with_springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<CustomResponseDto> handleLoginFailedException(UserAlreadyRegisteredException e) {
        CustomResponseDto body = new CustomResponseDto(
                Status.UNAUTHORIZED.getCode(),
                "User already exist exception"
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotExistException.class)
    public ResponseEntity<CustomResponseDto> handleLoginFailedException(UserNotExistException e) {
        CustomResponseDto body = new CustomResponseDto(
                Status.NOT_FOUND.getCode(),
                "User not exist exception"
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

}

