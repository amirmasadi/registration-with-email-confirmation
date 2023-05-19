package com.example.demo.registration;

import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.AppUserRole;
import com.example.demo.appuser.AppUserService;
import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import org.aspectj.apache.bcel.classfile.LocalVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RegistrationService {
  @Autowired
  private EmailValidator emailValidator;
  @Autowired
  private AppUserService appUserService;
  @Autowired
  private ConfirmationTokenService confirmationTokenService;

  public String register(RegistrationRequest registrationRequest) {
    boolean isEmailValid = emailValidator.test(registrationRequest.email());
    if (!isEmailValid) {
      throw new IllegalStateException("email is not valid");
    }
    return appUserService.signupUser(
            new AppUser(
                    registrationRequest.firstName(),
                    registrationRequest.lastName(),
                    registrationRequest.email(),
                    registrationRequest.password(),
                    AppUserRole.USER
            )
    );
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

    confirmationTokenService.setTokenConfirmedAt(confirmationToken, currentTime);
    appUserService.enableAppUser(confirmationToken.getAppuser().getId());

    return "Email verified and user is enabled now...";
  }
}
