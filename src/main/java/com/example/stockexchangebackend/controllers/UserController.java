package com.example.stockexchangebackend.controllers;

import com.example.stockexchangebackend.models.*;

import com.example.stockexchangebackend.repositories.RoleRepository;
import com.example.stockexchangebackend.repositories.UserRepository;
import com.example.stockexchangebackend.security.JwtUtils;
import com.example.stockexchangebackend.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @CrossOrigin(origins ={"http://127.0.0.1:3000","http://localhost:3000/","https://stockexchangefrontend.herokuapp.com"})
    @RequestMapping(value = "/setuserapi",method= RequestMethod.POST)
    public ResponseEntity<?> Stringreactuserapi(@RequestBody User1 user) throws AddressException {
        user.setAdmin(false);
        user.setConfirmed(false);
        user.setPassword(encoder.encode(user.getPassword()));
        if(user.getUsername().equals("admin"))
        {
            user.setAdmin(true);
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
            user.setRoles(roles);
        }

        if(userRepository.existsByUsername(user.getUsername())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if(userRepository.existsByEmail(user.getEmail()))
        {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        Set<Role> strRoles = user.getRoles();
        Set<Role> roles = new HashSet<>();

        if(strRoles.isEmpty())
        {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);


            user.setRoles(roles);
        }


        User1 usrsaved = userRepository.save(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Responded", "UserController");
        headers.add("Access-Control-Allow-Origin", "*");
        sendemail(user.getId());

        return ResponseEntity.ok(new MessageResponse("User registered successfully! Check your email for confirmation link before login"));
    }

    public void sendemail(Long userid) throws AddressException {

        User1 user = userRepository.getById(userid);

        final String username = "thanos.starky@gmail.com";
        final String password = "VKMKB123";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("thanos.starky@gmail.com"));
            //message.setRecipients(
            //	Message.RecipientType.TO,
            //	InternetAddress.parse("sftrainerram@gmail.com")
            //	);
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(user.getEmail())
            );
            message.setSubject("USer confirmation email");
            //     message.setText("Dear Mail Crawler,"
            //           + "\n\n Please do not spam my email!");
            message.setContent(
                    "<h1><a href =\"https://stockexchangebackend.herokuapp.com/auth/confirmuser/"+userid+"/\"> Click to confirm </a></h1>",
                    "text/html");
            Transport.send(message);

            System.out.println("Done");

        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
        }
    }
    @CrossOrigin(origins ={"http://localhost:3000","https://stockexchangefrontend.herokuapp.com"})
    @RequestMapping(value="/confirmuser/{userid}", method=RequestMethod.GET)
    public String welcomepage(@PathVariable Long userid) {
        Optional<User1> userlist =   userRepository.findById(userid);
        //do a null check for home work
        User1 usr = new User1();
        usr = userRepository.getById(userid);
        usr.setConfirmed(true);
        userRepository.save(usr);
        return "User confirmed" +usr.getUsername() +" Get back to the login Page <a href =\"https://stockexchangefrontend.herokuapp.com/login\"> Click to Login Page </a> ";
    }

    @CrossOrigin(origins ={"http://127.0.0.1:3000","http://localhost:3000/","https://stockexchangefrontend.herokuapp.com"})
    @RequestMapping(value="/signin",method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){

        User1 user = userRepository.findByUsername(loginRequest.getUsername()).get();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);


        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
//        if(Objects.isNull(user))
//        {
//            return new ResponseEntity(HttpStatus.NO_CONTENT);
//        }
        if(!user.getConfirmed())
        {
            return ResponseEntity.ok("User Not confirmed his email");
        }
        if(userDetails.getUsername().equals("admin"))
        {
            userDetails.setAdmin(true);
        }
        return ResponseEntity.ok(new LoginResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.isAdmin(),
                roles
        ));
    }


}
