package tech.itparklessons.fileshares.user.service.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.itparklessons.fileshares.user.model.dto.RegistrationUserDto;
import tech.itparklessons.fileshares.user.model.dto.UserInformationResponse;
import tech.itparklessons.fileshares.user.model.entity.Users;
import tech.itparklessons.fileshares.user.model.enums.Role;
import tech.itparklessons.fileshares.user.repository.UserRepository;
import tech.itparklessons.fileshares.user.service.UserService;
import tech.itparklessons.fileshares.user.util.mapper.UserMapper;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${app-properties.jwt.ttl}")
    private int jwtTTL;

    @Value("${app-properties.jwt.secret}")
    private String jwtSecret;

    @Override
    public String createUser(RegistrationUserDto registrationUserDto) throws JOSEException {
        Users userEntity = userMapper.toEntity(registrationUserDto);

        userEntity.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        userEntity.setAuthorities(Collections.singleton(new SimpleGrantedAuthority(Role.USER.getSpringRole())));

        Users savedUser = userRepository.save(userEntity);

        return toJwt(savedUser);
    }

    @Override
    public String toJwt(Users user) throws JOSEException {

        final JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .expirationTime(Date.from(new Date().toInstant().plus(jwtTTL, ChronoUnit.MINUTES)))
                .issueTime(new Date())
                .claim("userId", user.getId())
                .subject(user.getLogin())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("authorities", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .build();

        final SignedJWT signed = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
        signed.sign(new MACSigner(jwtSecret));

        return signed.serialize();
    }

    @Override
    public String toJwt(Authentication authenticate) throws JOSEException {
        return toJwt((Users) authenticate.getPrincipal());
    }

    @Override
    public boolean verifyJWT(String token) throws JOSEException, ParseException {
        final SignedJWT deserialized = SignedJWT.parse(token.substring(7));
        final MACVerifier verifier = new MACVerifier(jwtSecret);

        Date expirationTime = deserialized.getJWTClaimsSet().getExpirationTime();

        if (new Date().after(expirationTime)) {
            throw new RuntimeException("token is expired");
        }

        return deserialized.verify(verifier);
    }

    @Override
    public UserInformationResponse getUserInfo(String loginOrEmail) {
        return userRepository.findByLoginOrEmailIs(loginOrEmail);
    }
}