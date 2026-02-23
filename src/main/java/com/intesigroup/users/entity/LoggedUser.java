package com.intesigroup.users.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class LoggedUser {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long loggedUserId;

    String username;
    String email;
    String operation;
    LocalDateTime loggedAt;

}
