package com.example.demo.registration.token;

import com.example.demo.appuser.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {
  @Id
  @SequenceGenerator(name = "token_seq", sequenceName = "token_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
  private Long id;
  @Column(nullable = false)
  private String token;
  @Column(nullable = false)

  private LocalDateTime createdAt;
  @Column(nullable = false)

  private LocalDateTime expiresAt;
  private LocalDateTime confirmedAt;

  @ManyToOne
  @JoinColumn(nullable = false, name = "app_user_id")
  private AppUser appuser;

  public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, AppUser appuser) {
    this.token = token;
    this.createdAt = createdAt;
    this.expiresAt = expiresAt;
    this.appuser = appuser;
  }
}
