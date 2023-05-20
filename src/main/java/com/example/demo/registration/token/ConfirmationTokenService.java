package com.example.demo.registration.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConfirmationTokenService {
  @Autowired
  private IConfirmationTokenRepository iConfirmationTokenRepository;

  public void saveConfirmationToken(ConfirmationToken confirmationToken) {
    iConfirmationTokenRepository.save(confirmationToken);
  }

  public Optional<ConfirmationToken> getToken(String token) {
    return iConfirmationTokenRepository.findByToken(token);
  }

  public void setTokenConfirmedAt(ConfirmationToken confirmationToken, LocalDateTime time) {
    confirmationToken.setConfirmedAt(time);
    saveConfirmationToken(confirmationToken);
  }

}
