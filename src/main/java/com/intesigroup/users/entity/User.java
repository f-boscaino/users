package com.intesigroup.users.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @Email
    @NotBlank
    String email;
    String username;
    String name;
    String surname;
    String taxCode;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    List<Role> roleList;

    public void update(User user) {
        setUsername(user.getUsername());
        setName(user.getName());
        setSurname(user.getSurname());
        setTaxCode(user.getTaxCode());
        setRoleList(user.getRoleList());
    }
}
