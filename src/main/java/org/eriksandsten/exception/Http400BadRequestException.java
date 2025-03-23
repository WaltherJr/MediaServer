package org.eriksandsten.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class Http400BadRequestException extends HttpClientErrorException {
    public Http400BadRequestException(String message) {
        super(HttpStatusCode.valueOf(400), message);
    }
}
