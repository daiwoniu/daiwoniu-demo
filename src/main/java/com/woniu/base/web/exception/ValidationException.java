package com.woniu.base.web.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationException extends ApplicationException {
    private final BindingResult bindingResult;

    public ValidationException(BindingResult bindingResult) {
        super(ErrorCode.VALIDATION_ERROR);
        this.bindingResult = bindingResult;
    }
    
    public List<ObjectError> getAllObjectErrors() {
        return bindingResult.getAllErrors();
    }
    
    public List<FieldError> getAllFieldErrors() {
        return bindingResult.getFieldErrors();
    }
    
    public Map<String, Object> toJson(MessageSource messageSource) {
        Map<String, Object> json = new HashMap<>();
        json.put("success", false);
        json.put("code", ErrorCode.VALIDATION_ERROR.code);
        
        String defaultMessage = null;
        
        if (bindingResult.getGlobalErrorCount() > 0) {
            List<GlobalErrorResource> globalErrors = new ArrayList<>();
            for (ObjectError objectError : bindingResult.getGlobalErrors()) {
                GlobalErrorResource error = new GlobalErrorResource();
                error.setCode(objectError.getCode());
                error.setMessage(messageSource.getMessage(objectError, null));
                if (defaultMessage == null) {
                    defaultMessage = error.getMessage();
                }
                globalErrors.add(error);
            }
            json.put("globalErrors", globalErrors);
        }
        
        if (bindingResult.getFieldErrorCount() > 0) {
            List<FieldErrorResource> fieldErrors = new ArrayList<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                FieldErrorResource error = new FieldErrorResource();
                error.setCode(fieldError.getCode());
                error.setField(fieldError.getField());
                error.setMessage(messageSource.getMessage(fieldError, null));
                if (defaultMessage == null) {
                    defaultMessage = error.getField() + error.getMessage();
                }
                fieldErrors.add(error);
            }
            json.put("fieldErrors", fieldErrors);
        }
        
        json.put("message", defaultMessage);
        
        return json;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GlobalErrorResource {
        private String code;
        private String message;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FieldErrorResource {
        private String field;
        private String code;
        private String message;

        public String getField() { return field; }

        public void setField(String field) { this.field = field; }

        public String getCode() { return code; }

        public void setCode(String code) { this.code = code; }

        public String getMessage() { return message; }

        public void setMessage(String message) { this.message = message; }
    }
    
}
