package com.ticketapp.Queue;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class Email implements Serializable {

    private String id;
    private String to;
    private String subject;
    private String body;

    public Email() {
        this.id = UUID.randomUUID().toString();
    }
}