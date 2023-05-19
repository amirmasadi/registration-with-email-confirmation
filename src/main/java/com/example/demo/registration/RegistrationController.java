package com.example.demo.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/registration")
public class RegistrationController {
  @Autowired
  RegistrationService registrationService;

  @PostMapping
  public String registration(@RequestBody RegistrationRequest registrationRequest) {
    return registrationService.register(registrationRequest);
  }

  @GetMapping("confirm")
  public String confirmEmail(@RequestParam("token") String token) {
    return registrationService.confirmToken(token);
  }
}
