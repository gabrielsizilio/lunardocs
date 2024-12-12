package io.gitgub.gabrielsizilio.lunardocs.controllers;

import io.gitgub.gabrielsizilio.lunardocs.domain.helloWorld.dto.HelloWorldRequestDTO;
import io.gitgub.gabrielsizilio.lunardocs.domain.helloWorld.dto.HelloWorldResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class HelloWorldController {


    @GetMapping("/")
    public ResponseEntity<HelloWorldResponseDTO> sayHello() {
        return ResponseEntity.ok(new HelloWorldResponseDTO("Hello World!"));
    }

    @PostMapping("/say")
    public ResponseEntity<HelloWorldResponseDTO> saySomething(@RequestBody @Validated HelloWorldRequestDTO data) {
        System.out.println(">> Message: " + data.message());
        HelloWorldResponseDTO helloWorldResponseDTO = new HelloWorldResponseDTO(data.message());
        return ResponseEntity.ok(helloWorldResponseDTO);
    }
}
