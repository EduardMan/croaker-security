package tech.itparklessons.fileshares.user.controller;

import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tech.itparklessons.fileshares.user.model.dto.LoginUserDto;
import tech.itparklessons.fileshares.user.model.dto.RegistrationUserDto;
import tech.itparklessons.fileshares.user.model.entity.Users;
import tech.itparklessons.fileshares.user.service.UserService;

import java.text.ParseException;

@RequestMapping("/api/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/registration")
    public String createAccount(@RequestBody RegistrationUserDto registrationUserDto) throws JOSEException {
        return userService.createUser(registrationUserDto);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginUserDto loginAccountDto) throws JOSEException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginAccountDto.getLogin(), loginAccountDto.getPassword()));
        return userService.toJwt(authentication);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/removeUser")
    public String removeUser(@AuthenticationPrincipal Users user) {
        return "Ok";
    }

    @GetMapping("/verify")
    public boolean verify(@RequestHeader("Authorization") String token) throws JOSEException, ParseException {
        return userService.verifyJWT(token);
    }
}