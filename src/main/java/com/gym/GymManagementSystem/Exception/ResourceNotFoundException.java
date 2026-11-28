package com.gym.GymManagementSystem.Exception;

public class ResourceNotFoundException extends RuntimeException  {
    public ResourceNotFoundException(String message){
        super(message);
    }
}
