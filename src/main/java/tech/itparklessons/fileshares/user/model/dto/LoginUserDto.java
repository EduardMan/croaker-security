package tech.itparklessons.fileshares.user.model.dto;

import lombok.Data;

@Data
public class LoginUserDto {
    private String login;
    private String password;
}