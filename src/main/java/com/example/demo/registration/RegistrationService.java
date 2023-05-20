package com.example.demo.registration;

import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.AppUserRole;
import com.example.demo.appuser.AppUserService;
import com.example.demo.email.EmailService;
import com.example.demo.email.EmailTemplate;
import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrationService {
  @Autowired
  private EmailValidator emailValidator;
  @Autowired
  private AppUserService appUserService;
  @Autowired
  private ConfirmationTokenService confirmationTokenService;
  @Autowired
  private EmailService emailService;

  public String register(RegistrationRequest registrationRequest) {
    boolean isEmailValid = emailValidator.test(registrationRequest.email());

    if (!isEmailValid) {
      throw new IllegalStateException("email is not valid");
    }

    String newUserToken = appUserService.signupUser(
            new AppUser(
                    registrationRequest.firstName(),
                    registrationRequest.lastName(),
                    registrationRequest.email(),
                    registrationRequest.password(),
                    AppUserRole.USER
            )
    );

    String confirmationEmailLink = "http://localhost:8080/api/v1/registration/confirm?token=" + newUserToken;

    emailService.send(registrationRequest.email(), new EmailTemplate().build(registrationRequest.firstName(), confirmationEmailLink));

    return newUserToken;
  }

  public String confirmToken(String token) {
    LocalDateTime currentTime = LocalDateTime.now();
    ConfirmationToken confirmationToken = confirmationTokenService
            .getToken(token)
            .orElseThrow(() -> new IllegalStateException("token not found"));

    if (confirmationToken.getConfirmedAt() != null) {
      throw new IllegalStateException("Email already confirmed");
    }

    if (confirmationToken.getExpiresAt().isBefore(currentTime)) {
      throw new IllegalStateException("Token Expired");
    }

    //TODO what if token expires and user want a new token?

    confirmationTokenService.setTokenConfirmedAt(confirmationToken, currentTime);
    appUserService.enableAppUser(confirmationToken.getAppuser().getId());

    return "Email verified and user is enabled now...";
  }
}
