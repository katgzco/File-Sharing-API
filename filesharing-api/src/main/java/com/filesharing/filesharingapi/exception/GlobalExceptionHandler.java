package com.filesharing.filesharingapi.exception;

import com.filesharing.filesharingapi.dto.response.ErrorResponseDto;
import com.filesharing.filesharingapi.exception.storage.DataBaseConstrainException;
import com.filesharing.filesharingapi.exception.storage.DataBaseOperationException;
import com.filesharing.filesharingapi.exception.storage.StorageException;
import com.filesharing.filesharingapi.exception.storage.StorageInitializationException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException exception,
                                                                        WebRequest webRequest){
        ErrorResponseDto errorResponseEntity = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseEntity, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataBaseConstrainException.class)
    public ResponseEntity<ErrorResponseDto> handleDataBaseConstrain(DataBaseConstrainException exception,
                                                                    WebRequest webRequest){
        ErrorResponseDto errorResponseEntity = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseEntity, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataBaseOperationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataBaseOperationException(DataBaseOperationException exception,
                                                                        WebRequest webRequest){
        ErrorResponseDto errorResponseEntity = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseEntity, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorResponseDto> handleStorageException(StorageException exception,
                                                                             WebRequest webRequest){
        ErrorResponseDto errorResponseEntity = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseEntity, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(StorageInitializationException.class)
    public ResponseEntity<ErrorResponseDto> handleStorageInitializationException(StorageInitializationException exception,
                                                                   WebRequest webRequest){
        ErrorResponseDto errorResponseEntity = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseEntity, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    
    @ExceptionHandler(FileShareException.class)
    public ResponseEntity<ErrorResponseDto> handleFileShareException(FileShareException exception,
                                                                            WebRequest webRequest){
        ErrorResponseDto errorResponseEntity = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.CONFLICT,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseEntity, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(EntityAlreadyExistException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityAlreadyExistException(EntityAlreadyExistException exception,
                                                                             WebRequest webRequest){
        ErrorResponseDto errorResponseEntity = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.CONFLICT,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseEntity, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(EntityNotFoundException exception,
                                                                             WebRequest webRequest){
        ErrorResponseDto errorResponseEntity = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseEntity, HttpStatus.NOT_FOUND);
    }



    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ErrorResponseDto> handleUnsupportedOperationException(UnsupportedOperationException exception,
                                                                          WebRequest webRequest){
        ErrorResponseDto errorResponseEntity = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseEntity, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }


    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponseDto> handleSecurityException(SecurityException exception,
                                                                                WebRequest webRequest){
        ErrorResponseDto errorResponseEntity = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseEntity, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException exception,
                                                                    WebRequest webRequest){
        ErrorResponseDto errorResponseEntity = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseEntity, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException exception,
                                                                           WebRequest webRequest){
        ErrorResponseDto errorResponseEntity = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseEntity, HttpStatus.BAD_REQUEST);
    }
}
