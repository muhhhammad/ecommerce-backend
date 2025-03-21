package com.practiceProject.ecommece.controller;

import com.practiceProject.ecommece.config.JwtProvider;
import com.practiceProject.ecommece.entity.Cart;
import com.practiceProject.ecommece.entity.User;
import com.practiceProject.ecommece.exception.UserException;
import com.practiceProject.ecommece.repository.UserRepository;
import com.practiceProject.ecommece.request.LoginRequest;
import com.practiceProject.ecommece.response.AuthResponse;
import com.practiceProject.ecommece.service.CartService;
import com.practiceProject.ecommece.service.CustomerUserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private UserRepository userRepository;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;
    private CustomerUserServiceImplementation customDetailService;
    private CartService cartService;


    //All the dependencies are injected here...
    @Autowired
    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          CustomerUserServiceImplementation customDetailService,
                          JwtProvider jwtProvider,
                          CartService cartService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customDetailService = customDetailService;
        this.jwtProvider = jwtProvider;
        this.cartService = cartService;

    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {

        //Extracting the details from the user class with @RequestBody Annotation
        String email = user.getEmail();
        String pass = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        //extracting the email..
        User ifAlreadyInUse = userRepository.findByEmail(email);

        //checking if email given by user is already in use or not
        if (ifAlreadyInUse != null) {

            throw new UserException("Email is Already Registered");

        }

        //Creating new user
        User createNewUser = new User();
        createNewUser.setEmail(email);
        createNewUser.setPassword(passwordEncoder.encode(pass)); //encoding password for security
        createNewUser.setFirstName(firstName);
        createNewUser.setLastName(lastName);

        //Saving the new user details
        User saveNewUser = userRepository.save(createNewUser);
        //creating the cart the moment new user is created
        Cart cart = cartService.createCart(saveNewUser);


        Authentication auth = new UsernamePasswordAuthenticationToken(saveNewUser.getEmail(), saveNewUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        //generating the token after authentication..
        String token = jwtProvider.generateToken(auth);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Signed up Successfully");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);


    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest) throws UserException {

        //Extracting the email and password from the loginRequest class
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        //passing the extracted value to authenticate the username and password given by the user
        Authentication auth = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(auth);

        //Generating the token.... after getting the authentication
        String token = jwtProvider.generateToken(auth);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Signing in Successfull");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);

    }

    //This Method is to authenticate the username and password given by user...
    //This method will throw exception if the username & password do not match the username and password in DataSource....
    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = customDetailService.loadUserByUsername(username);
        if (userDetails == null) {

            throw new BadCredentialsException("Invalid Username");

        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {

            throw new BadCredentialsException("Invalid Password");

        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }
}


//Code Written By: Muhammad Misbah...