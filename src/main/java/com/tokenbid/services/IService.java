package com.tokenbid.services;

import java.util.List;

public interface IService<T> {
    /**
     * Adds a new item to the database.
     * 
     * @param item The item to be saved.
     * @return The id of the saved item.
     */
    int add(T item);

    /**
     * Updates an item in the database.
     * 
     * @param item The item to be updated.
     */
    void update(T item);

    /**
     * Deletes an item from the database.
     * 
     * @param item The item to be deleted.
     */
    void delete(int id);

    /**
     * Gets an item from the database.
     * 
     * @param id The id of the item to be retrieved.
     * @return The item with the given id.
     */
    T getById(int id);

    /**
     * Gets all items from the database.
     * 
     * @return A list of all items in the database.
     */
    List<T> getAll();
}
