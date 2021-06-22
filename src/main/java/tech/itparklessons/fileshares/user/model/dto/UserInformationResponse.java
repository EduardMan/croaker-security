package tech.itparklessons.fileshares.user.model.dto;

import lombok.Data;

@Data
public class UserInformationResponse {
    private Long id;
    private String login;
    private String username;
    private String email;
}