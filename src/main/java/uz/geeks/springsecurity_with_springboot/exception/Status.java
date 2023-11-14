package uz.geeks.springsecurity_with_springboot.exception;

public enum Status {
    SUCCESS(200, "Success"),
    ERROR(500, "Error"),
    UNAUTHORIZED(401, "Unauthorized"),

    NOT_FOUND(404, "Not found"),
    WARNING(300, "Warning"),
    INFO(100, "Information");

    private final int code;
    private final String description;

    // Constructor for enum constants
    Status(int code, String description) {
        this.code = code;
        this.description = description;
    }

    // Getter methods
    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    // Additional method
    public String getFormattedStatus() {
        return code + ": " + description;
    }
}
