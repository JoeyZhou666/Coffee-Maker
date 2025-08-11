package edu.ncsu.csc326.coffee_maker.dto;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc326.coffee_maker.entity.Ingredient;

/**
 * Used to transfer Recipe data between the client and server.  
 * This class will serve as the response in the REST API.
 */
public class RecipeDto {

	/** Recipe Id */
    private Long    id;

    /** Recipe name */
    private String  name;

    /** Recipe price */
    private Integer price;
    
    /** the Ingredients used to make the recipe */
    private List<Ingredient> ingredients;
    
    /**
     * Default constructor for Recipe.
     */
    public RecipeDto() {
    	this.ingredients = new ArrayList<Ingredient>();
    }
    
    /**
     * Creates recipe from field values.
     * @param id recipe's id
     * @param name recipe's name
     * @param price recipe's price
     */
	public RecipeDto(Long id, String name, Integer price) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.ingredients = new ArrayList<Ingredient>();
	}

	/**
	 * Gets the recipe id.
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Recipe id to set.
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets recipe's name
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Recipe name to set.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the recipe's price
	 * @return the price
	 */
	public Integer getPrice() {
		return price;
	}

	/**
	 * Prices value to set.
	 * @param price the price to set
	 */
	public void setPrice(Integer price) {
		this.price = price;
	}
	
    /**
     * adds an ingredient to the ingredients in the recipe
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
