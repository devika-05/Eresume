package com.digitalresume.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ApiResponse {
    private boolean success;
    private String message;
    private Object data;
    private LocalDateTime timestamp = LocalDateTime.now();
    
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public ApiResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    /**
     * Static factory method to create a success response
     * @param message Success message
     * @return ApiResponse with success flag set to true
     */
    public static ApiResponse success(String message) {
        return new ApiResponse(true, message);
    }
    
    /**
     * Static factory method to create a success response with data
     * @param message Success message
     * @param data Data object to include in the response
     * @return ApiResponse with success flag set to true and data
     */
    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(true, message, data);
    }
    
    /**
     * Static factory method to create an error response
     * @param message Error message
     * @return ApiResponse with success flag set to false
     */
    public static ApiResponse error(String message) {
        return new ApiResponse(false, message);
    }
    
    /**
     * Static factory method to create an error response with data
     * @param message Error message
     * @param data Data object to include in the response
     * @return ApiResponse with success flag set to false and data
     */
    public static ApiResponse error(String message, Object data) {
        return new ApiResponse(false, message, data);
    }
}