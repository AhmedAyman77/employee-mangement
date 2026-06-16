package com.example.employee_mangement.share;

import java.util.List;

import lombok.Getter;

@Getter
public class GlobalResponse<T> {
    public final static String ERROR = "error";
    public final static String SUCCESS = "success";

    private final String status;
    private final T data;
    private final List<ErrorItems> errors;

    public record ErrorItems (String message) {}

    public GlobalResponse(List<ErrorItems> errors) {
        this.status = ERROR;
        this.data = null;
        this.errors = errors;
    }

    public GlobalResponse(T data) {
        this.status = SUCCESS;
        this.data = data;
        this.errors = null;
    }
}

