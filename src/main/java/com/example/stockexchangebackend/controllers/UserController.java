package com.example.stockexchangebackend.controllers;

import com.example.stockexchangebackend.models.*;
import com.sendgrid.*;
import com.example.stockexchangebackend.repositories.RoleRepository;
import com.example.stockexchangebackend.repositories.UserRepository;
import com.example.stockexchangebackend.security.JwtUtils;
import com.example.stockexchangebackend.security.UserDetailsImpl;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import javax.mail.internet.AddressException;

import java.io.IOException;
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

    private static final String API_KEY="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";
    @CrossOrigin(origins ={"http://127.0.0.1:3000","http://localhost:3000/","https://stockexchangefrontend.herokuapp.com"})
    @RequestMapping(value = "/setuserapi",method= RequestMethod.POST)
    public ResponseEntity<?> Stringreactuserapi(@RequestBody User1 user) throws AddressException, IOException {
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

    public void sendemail(Long userid) throws AddressException, IOException {

        User1 user = userRepository.getById(userid);

        Email from = new Email("thanos.starky@gmail.com");
        Email to = new Email(user.getEmail());
        String subject ="Your StockApp Confirmation Link";
        Content content = new Content("text/html", "<h1><a href =\"https://stockexchangebackend.herokuapp.com/auth/confirmuser/"+userid+"/\"> Click to confirm </a></h1>");
        Mail mail = new Mail(from,subject,to,content);
        SendGrid sg = new SendGrid(API_KEY);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getHeaders());
        System.out.println(response.getBody());

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
