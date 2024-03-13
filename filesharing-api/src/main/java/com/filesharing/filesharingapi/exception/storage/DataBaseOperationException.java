package com.filesharing.filesharingapi.exception.storage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class DataBaseOperationException extends RuntimeException {
    public DataBaseOperationException(String message) {
        super(message);
    }
}