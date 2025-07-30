package com.example.hadilprojectspring.entity;

import java.util.Map;

public class ValidationErrorResponse {
    private Boolean success;
    private String message;
    private Map<String, String> errors;

    public ValidationErrorResponse(Boolean success, String message, Map<String, String> errors) {
        this.success = success;
        this.message = message;
        this.errors = errors;
    }

    // Getters et Setters
    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Map<String, String> getErrors() { return errors; }
    public void setErrors(Map<String, String> errors) { this.errors = errors; }
}
