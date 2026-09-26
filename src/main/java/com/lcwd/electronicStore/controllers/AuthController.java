package com.lcwd.electronicStore.controllers;

import com.google.api.client.auth.openidconnect.IdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.lcwd.electronicStore.dtos.JwtRequest;
import com.lcwd.electronicStore.dtos.JwtResponse;
import com.lcwd.electronicStore.dtos.UserDto;
import com.lcwd.electronicStore.entities.User;
import com.lcwd.electronicStore.exceptions.BadApiRequestException;
import com.lcwd.electronicStore.security.JwtHelper;
import com.lcwd.electronicStore.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${googleClientId}")
    private String googleClientId;

    @Value("${newPassword}")
    private String newPassword;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper jwtHelper;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
        this.doAuthentication(request.getEmail(),request.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.jwtHelper.generateToken(userDetails);

        UserDto userDto = mapper.map(userDetails, UserDto.class);

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .user(userDto)
                .build();

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    private void doAuthentication(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email,password);
        try{
            manager.authenticate(authentication);
        }catch (BadCredentialsException e){
            throw new BadApiRequestException(" Invalid Username or Password !!");
        }
    }

    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal){
        String name = principal.getName();
        return new ResponseEntity<>(mapper.map(userDetailsService.loadUserByUsername(name),UserDto.class), HttpStatus.OK);
    }

    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String, Object> data) throws IOException {
        String idToken = data.get("idToken").toString();

        NetHttpTransport netHttpTransport = new NetHttpTransport();

        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory).setAudience(Collections.singleton(googleClientId));

        GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), idToken);

        GoogleIdToken.Payload payload = googleIdToken.getPayload();

        logger.info("Payload : {}",payload);

        String email = payload.getEmail();

        User user = null;

        user = this.userService.findUserByEmailOptional(email).orElse(null);

        if(user == null){
            user=this.saveUser(email,data.get("name").toString(),data.get("photoUrl").toString());
        }

        ResponseEntity<JwtResponse> JwtResponse = this.login(JwtRequest.builder().email(user.getEmail()).password(newPassword).build());

        return JwtResponse;

    }

    private User saveUser(String email, String name, String photoUrl) {

        UserDto newUser = UserDto.builder()
                .email(email)
                .name(name)
                .password(newPassword)
                .userImage(photoUrl)
                .roles(new HashSet<>())
                .build();

        UserDto user = userService.createUser(newUser);

        return mapper.map(user,User.class);
    }
}
