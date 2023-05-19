package com.example.demo.appuser;

import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserService implements UserDetailsService {
  @Autowired
  private IAppUserRepository appUserRepository;
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  @Autowired
  private ConfirmationTokenService confirmationTokenService;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return appUserRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found."));
  }

  public String signupUser(AppUser appuser) {
    boolean isUserExists = appUserRepository.findByEmail(appuser.getEmail()).isPresent();
    if (isUserExists) {
      throw new IllegalStateException("Email already taken");
    }
    String encodedPass = bCryptPasswordEncoder.encode(appuser.getPassword());
    appuser.setPassword(encodedPass);
    appUserRepository.save(appuser);
    String token = UUID.randomUUID().toString();
    ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15), appuser);
    confirmationTokenService.saveConfirmationToken(confirmationToken);
    //TODO: Send email
    return token;
  }

  public void enableAppUser(Long id) {
    AppUser user = appUserRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("User not found"));
    user.setEnabled(true);
    appUserRepository.save(user);
  }
}
