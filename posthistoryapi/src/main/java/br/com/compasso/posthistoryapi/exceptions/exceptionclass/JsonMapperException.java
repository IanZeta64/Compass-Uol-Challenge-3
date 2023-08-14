package br.com.compasso.posthistoryapi.exceptions.exceptionclass;

import com.fasterxml.jackson.core.JsonProcessingException;
public class JsonMapperException extends RuntimeException {
    public JsonMapperException(JsonProcessingException message) {
        super(message);
    }
}
