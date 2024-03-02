package tn.esprit.com.Payment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeepAliveController {

    @GetMapping("/ping")
    public String ping() {
        return "Server is up and running!";
    }
}
