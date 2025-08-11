package edu.ncsu.csc326.coffee_maker.services;

import java.util.List;

import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;

/**
 * Interface defining the inventory behaviors.
 */
public interface InventoryService {
	
	/**
	 * Creates the inventory.
	 * @param inventoryDto inventory to create
	 * @return updated inventory after creation
	 */
	InventoryDto createInventory(InventoryDto inventoryDto);
	
	/**
	 * Returns the single inventory.
	 * @return the single inventory
	 */
	InventoryDto getInventory();
	
	/**
	 * Updates the contents of the inventory.
	 * @param inventoryDto values to update
	 * @return updated inventory
	 */
	InventoryDto updateInventory(InventoryDto inventoryDto);
	
	/**
     * Checks the given list of ingredients for validity.
     * @param ingredients the list of ingredients to check
     */
    void checkIngredients(List<Ingredient> ingredients);

}
