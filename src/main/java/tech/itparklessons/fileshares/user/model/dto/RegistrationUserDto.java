package tech.itparklessons.fileshares.user.model.dto;

import lombok.Data;

@Data
public class RegistrationUserDto {
    private String login;
    private String username;
    private String email;
    private String password;
}
