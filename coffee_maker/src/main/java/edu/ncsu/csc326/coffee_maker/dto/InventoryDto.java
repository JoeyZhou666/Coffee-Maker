package edu.ncsu.csc326.coffee_maker.dto;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc326.coffee_maker.entity.Ingredient;

/**
 * Used to transfer Inventory data between the client and server.  
 * This class will serve as the response in the REST API.
 */
public class InventoryDto {
	
	/** id for inventory entry */
    private Long    id;
    /** the ingredients in the inventory */
    private List<Ingredient> ingredients;
    
    /** 
     * Default InventoryDto constructor.
     */
    public InventoryDto() {
        this.ingredients = new ArrayList<Ingredient>();
    }
    
    /**
     * Constructs an InventoryDto object from field values.
     * @param id inventory id
     * @param ingredients the inventory's ingredients
     */
	public InventoryDto(Long id, List<Ingredient> ingredients) {
        super();
        this.id = id;
        this.ingredients = new ArrayList<Ingredient>();
        for (Ingredient ingredient : ingredients) {
            addIngredient(ingredient);
        }
    }

	/**
	 * Gets the inventory id.
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Inventory id to set.
	 * @param id the id to set
	 */
	public void setId(Long id) {
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
