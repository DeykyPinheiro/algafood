package com.apigaworks.algafood.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@SecurityRequirement(name = "security_auth")
@Tag(name = "CheckHost")
public class HostCheckController {

    @Operation(summary = "Mostra o host Atual")
    @GetMapping("host")
    public String checkHost() throws UnknownHostException {
        return "ADDRESS: " + InetAddress.getLocalHost().getHostAddress() +
                " - HOSTNAME: " + InetAddress.getLocalHost().getHostName();
    }
}
