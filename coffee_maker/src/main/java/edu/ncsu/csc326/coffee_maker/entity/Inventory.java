package edu.ncsu.csc326.coffee_maker.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is a Data Access Object (DAO) is tied to the database using
 * Hibernate libraries. InventoryRepository provides the methods for database CRUD operations.
 */
@Entity
public class Inventory {
	
	/** id for inventory entry */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;
    /** the ingredients in the inventory */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Ingredient> ingredients;
    
    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
        this.ingredients = new ArrayList<Ingredient>();
    }
    
    /**
     * Creates an Inventory with all fields
     * @param id inventory's id
     * @param ingredients the inventory's ingredients
     */
    public Inventory(Long id, List<Ingredient> ingredients) {
		super();
		this.id = id;
		this.ingredients =  new ArrayList<Ingredient>();
        for (Ingredient ingredient : ingredients) {
            addIngredient(ingredient);
        }
	}


    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * adds an ingredient to the ingredients in the Inventory
     * 
     * @param ingredient
     *              the ingredient to add
     */
    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }
    
    /**
     * gets the list of ingredients not a copy so edits made to the returned list will
     * change the stored list
     * 
     * @return list of Ingredients in the Recipe
     */
    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

}
