package uz.geeks.springsecurity_with_springboot.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomResponseDto {
    private int statusCode = 0;
    private String message;

    public CustomResponseDto() {
    }

    public CustomResponseDto(int status_code, String message) {
        this.statusCode = status_code;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int status_code) {
        this.statusCode = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
