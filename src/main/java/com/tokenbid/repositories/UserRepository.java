package com.tokenbid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tokenbid.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Check if a user exists with the given username
     * 
     * @param username Username to check
     * @return True if a user with the given username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Check if a user exists with the given email
     * 
     * @param email Email to check
     * @return True if a user with the given email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Finds the user by their username
     * 
     * @param username The username of the user to be retrieved
     * @return User The user with the given username
     */
    @Query(value = "SELECT * FROM users WHERE username = :username", nativeQuery = true)
    public User findByUsername(@Param("username") String username);
}
