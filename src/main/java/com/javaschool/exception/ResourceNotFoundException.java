package com.javaschool.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Data
public class ResourceNotFoundException extends RuntimeException{

    private String resourceName;
    private String resourceFiled;
    private String fieldValue;


    public ResourceNotFoundException(String resourceName, String resourceFiled,String fieldValue){
        super(String.format("%s not found with %s : '%s'",resourceName,resourceFiled,fieldValue));
        this.resourceName = resourceName;
        this.resourceFiled = resourceFiled;
        this.fieldValue = fieldValue;
    }
}
