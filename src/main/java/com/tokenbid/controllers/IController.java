package com.tokenbid.controllers;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.ResponseEntity;

public interface IController<T> {
    /**
     * Adds a new item to the database.
     * 
     * @param item The item to be saved.
     * @return The id of the saved item.
     * @throws URISyntaxException
     */
    ResponseEntity<String> add(T item) throws URISyntaxException;

    /**
     * Updates an item in the database.
     * 
     * @param id          The if of item to be updated.
     * @param updatedItem The updated item.
     */
    ResponseEntity<String> update(int id, T updatedItem);

    /**
     * Deletes an item from the database.
     * 
     * @param item The item to be deleted.
     */
    ResponseEntity<String> delete(int id);

    /**
     * Gets an item from the database.
     * 
     * @param id The id of the item to be retrieved.
     * @return The item with the given id.
     */
    ResponseEntity<T> get(int id);

    /**
     * Gets all items from the database.
     * 
     * @return A list of all items in the database.
     */
    ResponseEntity<List<T>> getAll();
}
