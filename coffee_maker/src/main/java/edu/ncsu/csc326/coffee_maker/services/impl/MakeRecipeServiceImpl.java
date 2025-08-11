package edu.ncsu.csc326.coffee_maker.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Inventory;
import edu.ncsu.csc326.coffee_maker.entity.Recipe;
import edu.ncsu.csc326.coffee_maker.mapper.InventoryMapper;
import edu.ncsu.csc326.coffee_maker.mapper.RecipeMapper;
import edu.ncsu.csc326.coffee_maker.repositories.InventoryRepository;
import edu.ncsu.csc326.coffee_maker.services.MakeRecipeService;

/**
 * Implementation of the MakeRecipeService interface.
 */
@Service
public class MakeRecipeServiceImpl implements MakeRecipeService {
	
	/** Connection to the repository to work with the DAO + database */
	@Autowired
	private InventoryRepository inventoryRepository;

	/**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param inventoryDto
     * 			  current inventory
     * @param recipeDto
     *            recipe to make
     * @return updated inventory 
     */
	@Override
	public boolean makeRecipe(InventoryDto inventoryDto, RecipeDto recipeDto) {
		Inventory inventory = InventoryMapper.mapToInventory(inventoryDto);
		Recipe recipe = RecipeMapper.mapToRecipe(recipeDto);
		
		if ( enoughIngredients( inventory, recipe ) ) {
            inventoryRepository.save(inventory);
            return true;
		}
		        
        return false; 
	}
	
    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param inventory
     * 			  coffee maker inventory
     * @param recipe
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    private boolean enoughIngredients (Inventory inventory, Recipe recipe) {
        boolean isEnough = true;

        return isEnough;
    }

}