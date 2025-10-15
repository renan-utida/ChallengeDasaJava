package com.dasa.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class PingController {

    @GetMapping({"/", "/api/ping"})
    public Map<String, Object> ping() {
        return Map.of(
                "service", "Challenge Dasa Java API",
                "status", "ok"
        );
    }
}
