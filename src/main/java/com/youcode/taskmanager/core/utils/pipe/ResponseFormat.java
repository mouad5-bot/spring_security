package com.youcode.taskmanager.core.utils.pipe;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Scope("prototype")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ResponseFormat<T> {

    private LocalDateTime timestamp;
    private String message;
    private T data;

    public ResponseFormat<T> format() {
        this.timestamp = LocalDateTime.now();
        this.message = "AFSAT API responded : operation successful";
        return this;
    }

    public ResponseFormat<T> format(String message) {
        this.timestamp = LocalDateTime.now();
        this.message = "AFSAT API responded : " + message;
        return this;
    }

    public ResponseFormat<T> format(T data) {
        this.timestamp = LocalDateTime.now();
        this.message = "AFSAT API responded : operation successful";
        this.data = data;
        return this;
    }

    public ResponseFormat<T> format(T data, String message) {
        this.timestamp = LocalDateTime.now();
        this.message = "AFSAT API responded : " + message;
        this.data = data;
        return this;
    }

}
