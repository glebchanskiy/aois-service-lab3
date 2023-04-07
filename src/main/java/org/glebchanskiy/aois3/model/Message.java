package org.glebchanskiy.aois3.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private String text;
    private String from;
    private String to;
}
