package com.example.dm.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PasswordGenerator {
  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
  private static final int PASSWORD_LENGTH = 12;

  public static String generatePassword() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    String formattedDateTime = now.format(formatter);
    String seed = formattedDateTime + "dev_mingle";
    SecureRandom random = new SecureRandom(seed.getBytes());
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < PASSWORD_LENGTH; i++) {
      int randomIndex = random.nextInt(CHARACTERS.length());
      sb.append(CHARACTERS.charAt(randomIndex));
    }
    return sb.toString();
  }
}

