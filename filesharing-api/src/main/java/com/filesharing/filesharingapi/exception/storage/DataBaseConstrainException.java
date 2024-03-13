package com.filesharing.filesharingapi.exception.storage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class DataBaseConstrainException extends RuntimeException {
    public DataBaseConstrainException(String message) {
        super(message);
    }
}
