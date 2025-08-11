
package edu.ncsu.csc326.coffee_maker.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.entity.Inventory;
import edu.ncsu.csc326.coffee_maker.exception.ConflictException;
import edu.ncsu.csc326.coffee_maker.mapper.InventoryMapper;
import edu.ncsu.csc326.coffee_maker.repositories.InventoryRepository;
import edu.ncsu.csc326.coffee_maker.services.InventoryService;

/**
 * Implementation of the InventoryService interface.
 */
@Service
public class InventoryServiceImpl implements InventoryService {
	
	/** Connection to the repository to work with the DAO + database */
	@Autowired
	private InventoryRepository inventoryRepository;

	/**
	 * Creates the inventory.
	 * @param inventoryDto inventory to create
	 * @return updated inventory after creation
	 */
	@Override
	public InventoryDto createInventory(InventoryDto inventoryDto) {
		Inventory inventory = InventoryMapper.mapToInventory(inventoryDto);
		Inventory savedInventory = inventoryRepository.save(inventory);
		return InventoryMapper.mapToInventoryDto(savedInventory);
	}

	/**
	 * Returns the single inventory.
	 * @return the single inventory
	 */
	@Override
	public InventoryDto getInventory() {
		List<Inventory> inventory = inventoryRepository.findAll();
		if (inventory.size() == 0) {
			InventoryDto newInventoryDto = new InventoryDto();
			
			InventoryDto savedInventoryDto = createInventory(newInventoryDto);
			return savedInventoryDto;
		}
		return InventoryMapper.mapToInventoryDto(inventory.get(0));
	}

	/**
	 * Updates the contents of the inventory.
	 * @param inventoryDto values to update
	 * @return updated inventory
	 */
	@Override
	public InventoryDto updateInventory(InventoryDto inventoryDto) {
		
		Inventory inventory = inventoryRepository.findById(1L).orElse(null);
	    if (inventory == null) {
	        inventory = new Inventory();
	        inventory.setId(1L);  // Set the singleton inventory ID
	    }

	    // Clear existing ingredients and add the new ones
	    inventory.getIngredients().clear();
	    for (Ingredient ingredient : inventoryDto.getIngredients()) {
	        inventory.addIngredient(ingredient);
	    }

	    // Save the inventory to the repository
	    Inventory savedInventory = inventoryRepository.save(inventory);

	    return InventoryMapper.mapToInventoryDto(savedInventory);
	}

	   /**
     * Checks the given list of ingredients for validity.
     * @param ingredients the list of ingredients to check
     * @throws IllegalArgumentException if the list is empty, has duplicate names, 
     *          has ingredients with non-positve amounts, or has ingredient that does not exist in inventory
     * @throws ConflictException if the same ingredient appears twice in list.
     */
	@Override
	public void checkIngredients(List<Ingredient> ingredients) {
	    // Check if the list contains at least one ingredient
	    if (ingredients == null || ingredients.isEmpty()) {
	        throw new IllegalArgumentException("The empty ingredient list is invalid.");
	    }

	    // Use a Set to track unique ingredient names to detect duplicates
	    Set<String> ingredientNames = new HashSet<>();

	    // Get the current inventory to check if all ingredients exist in the inventory
	    InventoryDto currentInventory = getInventory();
	    List<Ingredient> currentInventoryIngredients = currentInventory.getIngredients();
	    
        if (!currentInventoryIngredients.containsAll(ingredients)) {
            throw new IllegalArgumentException("Ingredient not in inventory");
        }
	    
	    for (Ingredient ingredient : ingredients) {
	        
	        // Check for duplicate names
	        if (!ingredientNames.add(ingredient.getName())) {
	            throw new ConflictException("Duplicate ingredient found: " + ingredient.getName());
	        }

	        // Check if quantity is positive
	        if (ingredient.getAmount() <= 0) {
	            throw new IllegalArgumentException("Ingredient quantity must be positive for: " + ingredient.getName());
	        }

	        

	    }
	}


}
