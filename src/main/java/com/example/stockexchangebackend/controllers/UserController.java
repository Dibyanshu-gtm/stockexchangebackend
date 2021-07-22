package com.example.stockexchangebackend.controllers;

import com.example.stockexchangebackend.models.User1;
import com.example.stockexchangebackend.models.LoginRequest;
import com.example.stockexchangebackend.models.LoginResponse;
import com.example.stockexchangebackend.models.MessageResponse;

import com.example.stockexchangebackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    @CrossOrigin(origins ={"http://127.0.0.1:3000","http://localhost:3000/","https://stockexchangefrontend.herokuapp.com"})
    @RequestMapping(value = "/setuserapi",method= RequestMethod.POST)
    public ResponseEntity<?> Stringreactuserapi(@RequestBody User1 user) throws AddressException {
        user.setAdmin(false);
        if(user.getUsername().equals("admin"))
        {
            user.setAdmin(true);
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
                    "<h1><a href =\"http://127.0.0.1:8080/confirmuser/"+userid+"/\"> Click to confirm </a></h1>",
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
        return "User confirmed" +usr.getUsername() +" Get back to the login Page <a href =\"http://127.0.0.1:3000/login\"> Click to Login Page </a> ";
    }

    @CrossOrigin(origins ={"http://127.0.0.1:3000","http://localhost:3000/","https://stockexchangefrontend.herokuapp.com"})
    @RequestMapping(value="/signin",method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){

        User1 user = userRepository.getByNameAndPassword(loginRequest.getUsername(),loginRequest.getPassword());
        if(Objects.isNull(user))
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(new LoginResponse(user.getUsername(), user.getEmail(),user.getAdmin()));
    }


}
