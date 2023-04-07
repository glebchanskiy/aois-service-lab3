package org.glebchanskiy.aois3.controller;

import lombok.AllArgsConstructor;
import org.glebchanskiy.aois3.model.Message;
import org.glebchanskiy.aois3.service.LogicService;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/validate")
public class LogicController {

    private final LogicService logicService;

    @PostMapping
    public Message getInfo(@RequestBody Message message) {
        return logicService.getInfo(message.getText());
    }
}
