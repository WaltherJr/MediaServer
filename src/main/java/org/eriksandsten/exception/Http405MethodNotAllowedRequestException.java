package org.eriksandsten.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class Http405MethodNotAllowedRequestException extends HttpClientErrorException {
    public Http405MethodNotAllowedRequestException(String message) {
        super(HttpStatusCode.valueOf(405), message);
    }
}
