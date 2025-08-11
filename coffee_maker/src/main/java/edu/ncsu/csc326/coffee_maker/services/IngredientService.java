package edu.ncsu.csc326.coffee_maker.services;

import java.util.List;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.exception.ResourceNotFoundException;

/**
 * interface defining Ingredient behaviors
 * @author William Walton
 */
public interface IngredientService {

    /**
     * saves an ingredient to the repository
     * @param ingredientDto the data of the ingredient to save
     * @return the saved ingredient
     */
    IngredientDto createIngredient(IngredientDto ingredientDto);

    /*

	IngredientDto getIngredientById(Long ingredientId);
     */

    /**
     * Gets all of the ingredients in the repository
     * @return list of all ingredients in repository
     */
    List<IngredientDto> getAllIngredients();

    /**
     * Deletes the ingredient with the given id
     * @param ingredientId recipe's id
     * @throws ResourceNotFoundException if the ingredient doesn't exist
     */
    void deleteIngredient(Long ingredientId);


    /**
     * deletes all ingredients in the repository
     */
    void deleteAllIngredients();

}