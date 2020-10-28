package core.item;

import java.io.Serializable;

/**
 * Represents an item that can be traded in the system.
 */
public class Item implements Serializable {
    private final String type;
    private final String name;
    private final String description;
    private final int id;

    /**
     * Construct an item with its <code>type</code>, <code>name</code>, <code>description</code> and a unique
     * <code>id</code>.
     *
     * @param type        the type of this item.
     * @param name        the name of this item.
     * @param description the description of this item.
     * @param id          the id of this item.
     */
    public Item(String type, String name, String description, int id) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.id = id;
    }

    /**
     * Gets the <code>id</code> of this item.
     *
     * @return the <code>id</code> of this item.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the <code>name</code> of this item.
     *
     * @return the <code>name</code> of this item.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the <code>type</code> of this item.
     *
     * @return the <code>type</code> of this item.
     */
    public String getType() {
        return type;
    }

    /**
     * Gives a string representation of this item.
     *
     * @return information about this item as a string, including <code>name</code>, <code>type</code>,
     * <code>description</code> and <code>id</code>.
     */
    @Override
    public String toString() {
        return "Item Name: [" + name + "] Item Type: [" + type +
                "] Item Description: [" + description + "] Item ID: [" + id + "]";
    }
}
