package com.messenger_application.messenger.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class APIResponse {

    private Integer status;
    private Object body;
    private Object error;

    public APIResponse(){
        this.status = HttpStatus.OK.value();
    }
}
