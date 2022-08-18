package com.tokenbid.services;

import java.util.List;

import com.tokenbid.controllers.AuctionController;
import com.tokenbid.utils.EmailUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tokenbid.models.User;
import com.tokenbid.repositories.UserRepository;

@Service
public class UserService implements IService<User> {

    private int freeTokens = 250;
    private UserRepository userRepository;
    private EmailUtil emailUtil;
    private static Logger logger = LogManager.getLogger(AuctionController.class.getName());

    @Autowired
    public UserService(UserRepository userRepository, EmailUtil emailUtil) {
        this.userRepository = userRepository;
        this.emailUtil = emailUtil;
    }

    @Override
    public int add(User user) throws IllegalArgumentException {
        logger.debug("Adding a new user with userID: "+user.getUserId());
        if (userRepository.existsByUsername(user.getUsername()))
            throw new IllegalArgumentException("Username is unavailable");
        if (userRepository.existsByEmail(user.getEmail()))
            throw new IllegalArgumentException("Email is unavailable");
        User newUser = userRepository.save(user);
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String activationLink = baseUrl + "/users/" + newUser.getUserId() + "/verify";
        String message = buildRegistrationEmail(newUser.getFirstName(), newUser.getLastName(), activationLink);
        emailUtil.sendHTMLEmail(newUser.getEmail(), "Welcome to TokenBid!", message);
        return newUser.getUserId();
    }

    @Override
    public void update(User user) {
        if (userRepository.findById(user.getUserId()).isPresent()) {
            User currentUser = userRepository.findById(user.getUserId()).get();
            if (user.getFirstName() != null)
                currentUser.setFirstName(user.getFirstName());
            if (user.getLastName() != null)
                currentUser.setLastName(user.getLastName());
            if (user.getPassword() != null)
                currentUser.setPassword(user.getPassword());
            logger.debug("Updating the user with userID: "+user.getUserId());
            userRepository.save(currentUser);
        }
    }

    @Override
    public void delete(int id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        }
    }

    @Override
    public User getById(int id) {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get();
        }

        return null;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * If the user is found, and not verified already, it verifies the email and add
     * 250 free tokens to the user's account.
     * 
     * @param userId The user's id.
     * @return True if the user was found and verified, false otherwise.
     */
    public boolean verifyUserAndAddFreeTokens(int userId) {
        if (userRepository.findById(userId).isPresent() &&
                !userRepository.findById(userId).get().isEmailVerified()) {
            User user = getById(userId);
            user.setEmailVerified(true);
            user.setTokens(freeTokens);
            logger.debug("user with id: " +userId +" has verified the email and "+freeTokens +" tokens added to the user");
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /**
     * @param firstName      The user's first name.
     * @param lastName       The last name of the user.
     * @param activationLink The link to the user's account activation page.
     * @return The email message to be sent to the user.
     */
    private String buildRegistrationEmail(String firstName, String lastName, String activationLink) {
        return "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 24px; font-weight: bold; background-color: black; color: white; padding: 0.5em;\">"
                +
                "<p style=\"margin: 0; padding: 0; text-align: center;\">Confirm your email</p>" +
                "</div>" +
                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 16px; margin: 0;\">" +
                "<p style=\"margin-top: 1.5em;\">Hi " + firstName + " " + lastName + ",</p>" +
                "<p style=\"margin-top: 1.5em;\">Thank you for registering with TokenBid. Please click on the link below to activate your account:</p>"
                +
                "<p style=\"margin-top: 1.5em; margin-left: 2em; background-color: #ddd; padding: 0.5em;\"><a href="
                + activationLink + ">Activate</a></p>" +
                "</div>";
    }

    /**
     * Checks if the username exists in the database. If it does, it returns the
     * user.
     * 
     * @param username The username to check.
     * @return The user if it exists, null otherwise.
     */
    public User getByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user != null) {
            return user;
        }

        return null;
    }

    /**
     * To add tokens to user account based on payment on Paypal
     * @param tokens no of tokens to be added to user
     * @param userId userId of the user who buys new tokens
     * @return true if tokens added
     */
    public boolean addTokenAsPaypalAmount(int tokens, int userId){

        logger.debug("adding tokens: " +tokens +" to the user with userId: " +userId);
        User user = getById(userId);
        if(tokens > 0 && user != null) {
            int oldTokens = user.getTokens();
            user.setTokens(oldTokens + tokens);
            if(oldTokens + tokens == user.getTokens()){
                userRepository.save(user);
                return true;
            } else {
                return false;
            }
        } else{
            return false;
        }
    }
}
