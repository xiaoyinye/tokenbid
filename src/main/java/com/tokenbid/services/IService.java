package com.tokenbid.services;

import java.util.List;

public abstract class IService<T> {
    /**
     * Adds a new item to the database.
     * 
     * @param item The item to be saved.
     * @return The id of the saved item.
     */
    public abstract int add(T item);

    /**
     * Updates an item in the database.
     * 
     * @param item The item to be updated.
     */
    public abstract void update(T item);

    /**
     * Deletes an item from the database.
     * 
     * @param item The item to be deleted.
     */
    public abstract void delete(int id);

    /**
     * Gets an item from the database.
     * 
     * @param id The id of the item to be retrieved.
     * @return The item with the given id.
     */
    public abstract T getById(int id);

    /**
     * Gets all items from the database.
     * 
     * @return A list of all items in the database.
     */
    public abstract List<T> getAll();
}
