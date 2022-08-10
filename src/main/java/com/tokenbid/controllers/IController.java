package com.tokenbid.controllers;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.ResponseEntity;

public abstract class IController<T> {
    /**
     * Adds a new item to the database.
     * 
     * @param item The item to be saved.
     * @return The id of the saved item.
     * @throws URISyntaxException
     */
    public abstract ResponseEntity<Boolean> add(T item) throws URISyntaxException;

    /**
     * Updates an item in the database.
     * 
     * @param id          The if of item to be updated.
     * @param updatedItem The updated item.
     */
    public abstract ResponseEntity<Boolean> update(int id, T updatedItem);

    /**
     * Deletes an item from the database.
     * 
     * @param item The item to be deleted.
     */
    public abstract ResponseEntity<Boolean> delete(int id);

    /**
     * Gets an item from the database.
     * 
     * @param id The id of the item to be retrieved.
     * @return The item with the given id.
     */
    public abstract ResponseEntity<T> get(int id);

    /**
     * Gets all items from the database.
     * 
     * @return A list of all items in the database.
     */
    public abstract ResponseEntity<List<T>> getAll();
}
