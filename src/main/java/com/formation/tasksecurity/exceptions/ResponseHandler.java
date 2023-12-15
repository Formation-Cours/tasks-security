package com.formation.tasksecurity.exceptions;

import java.util.Collection;

public class ResponseHandler {
    public int status;
    public String message;
    public Collection<?> errors;
    public Collection<?> data;

    public ResponseHandler() {
    }

    public ResponseHandler(int status, String message, Collection<?> errors, Collection<?> data) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.data = data;
    }

    public ResponseHandler(int status, String message, Collection<?> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ResponseHandler(int status, Collection<?> errors) {
        this.status = status;
        this.errors = errors;
    }

    public ResponseHandler(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
