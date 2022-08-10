package com.tokenbid.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "category")
    private String category;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    /**
     * Default constructor.
     */
    public Item() {
    }

    /**
     * Parameterized constructor for Item class.
     * 
     * @param userId   The user id of the item.
     * @param category The category of the item.
     * @param title    The title of the item.
     */
    public Item(int userId, String category, String title) {
        this.userId = userId;
        this.category = category;
        this.title = title;
    }

    /**
     * Parameterized constructor for Item class.
     * 
     * @param userId      The user id of the item.
     * @param category    The category of the item.
     * @param title       The title of the item.
     * @param description The description of the item.
     */
    public Item(int userId, String category, String title, String description) {
        this.userId = userId;
        this.category = category;
        this.title = title;
        this.description = description;
    }

    /**
     * Gets the item id of the item.
     * 
     * @return The item id of the item.
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * Sets the item id of the item.
     * 
     * @param itemId The item id of the item.
     */
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    /**
     * Gets the user id of the item.
     * 
     * @return The user id of the item.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user id of the item.
     * 
     * @param userId The user id of the item.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the category of the item.
     * 
     * @return The category of the item.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the item.
     * 
     * @param category The category of the item.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the title of the item.
     * 
     * @return The title of the item.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the item.
     * 
     * @param title The title of the item.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description of the item.
     * 
     * @return The description of the item.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the item.
     * 
     * @param description The description of the item.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Item [itemId=" + itemId +
                ", userId=" + userId +
                ", category=" + category +
                ", title=" + title +
                ", description=" + description +
                "]";
    }

}
