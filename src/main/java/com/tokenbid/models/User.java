package com.tokenbid.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "tokens")
    private int tokens;

    /**
     * Default constructor.
     */
    public User() {
    }

    /**
     * Parameterized constructor for User class.
     * 
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Parameterized constructor for User class.
     * 
     * @param username  The username of the user.
     * @param password  The password of the user.
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     * @param email     The email of the user.
     */
    public User(String username, String password, String firstName, String lastName, String email) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * Parameterized constructor for User class.
     * 
     * @param username  The username of the user.
     * @param password  The password of the user.
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     * @param email     The email of the user.
     * @param tokens    The tokens of the user.
     */
    public User(String username, String password, String firstName, String lastName, String email, int tokens) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.tokens = tokens;
    }

    /**
     * Getter for the userId.
     * 
     * @return The userId of the user.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Setter for the userId.
     * 
     * @param userId The userId of the user.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Getter for the username.
     * 
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for the username.
     * 
     * @param username The username of the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the password.
     * 
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for the password.
     * 
     * @param password The password of the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for the firstName.
     * 
     * @return The firstName of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for the firstName.
     * 
     * @param firstName The firstName of the user.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for the lastName.
     * 
     * @return The lastName of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for the lastName.
     * 
     * @param lastName The lastName of the user.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter for the email.
     * 
     * @return The email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for the email.
     * 
     * @param email The email of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for the tokens.
     * 
     * @return The tokens of the user.
     */
    public int getTokens() {
        return tokens;
    }

    /**
     * Setter for the tokens.
     * 
     * @param tokens The tokens of the user.
     */
    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId +
                ", username=" + username +
                ", password=" + password +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", email=" + email +
                ", tokens=" + tokens + "]";
    }
}
