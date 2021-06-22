package tech.itparklessons.fileshares.user.service;

import com.nimbusds.jose.JOSEException;
import org.springframework.security.core.Authentication;
import tech.itparklessons.fileshares.user.model.dto.RegistrationUserDto;
import tech.itparklessons.fileshares.user.model.dto.UserInformationResponse;
import tech.itparklessons.fileshares.user.model.entity.Users;

import java.text.ParseException;

public interface UserService {
    String createUser(RegistrationUserDto registrationUserDto) throws JOSEException;

    String toJwt(Users user) throws JOSEException;

    String toJwt(Authentication authenticate) throws JOSEException;

    boolean verifyJWT(String token) throws JOSEException, ParseException;

    UserInformationResponse getUserInfo(String loginOrEmail);
}