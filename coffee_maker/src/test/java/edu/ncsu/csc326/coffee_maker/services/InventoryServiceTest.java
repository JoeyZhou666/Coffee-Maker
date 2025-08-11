package edu.ncsu.csc326.coffee_maker.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import edu.ncsu.csc326.coffee_maker.controllers.IngredientController;
import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.exception.ConflictException;
import edu.ncsu.csc326.coffee_maker.repositories.IngredientRepository;
import edu.ncsu.csc326.coffee_maker.repositories.InventoryRepository;

/**
 * Tests InventoryServiceImpl.
 */
@SpringBootTest
public class InventoryServiceTest {
	
	/** Reference to InventoryService (and InventoryServiceImpl). */
	@Autowired
	private InventoryService inventoryService;
	
	/** reference to IngredientController */
	@Autowired
	private IngredientController ingredientController;
	
    /** reference to IngredientRepository */
    @Autowired
    private IngredientRepository ingredientRepository;	
    
    /** reference to InventoryRepository */
    @Autowired
    private InventoryRepository inventoryRepository;  
	
	/**
	 * Sets up the test case.  We assume only one inventory row. Because inventory is
	 * treated as a singleton (only one row), we must truncate for 
	 * auto increment on the id to work correctly.
	 * @throws java.lang.Exception if error
	 */
	@BeforeEach
	//@Transactional
	public void setUp() throws Exception {
		//Query query = entityManager.createNativeQuery("TRUNCATE TABLE inventory");
		//query.executeUpdate();
		// First, delete from the child table
	    //entityManager.createNativeQuery("DELETE FROM inventory_ingredients").executeUpdate();

	    // Then, delete from the parent table
	    //entityManager.createNativeQuery("DELETE FROM inventory").executeUpdate();
	    
	    //entityManager.createNativeQuery("DELETE FROM ingredients").executeUpdate();
	    
	    //entityManager.createNativeQuery("TRUNCATE TABLE inventory").executeUpdate();
	    
	    //inventoryRepository.deleteAll();
	    //ingredientRepository.deleteAll();
	    
        inventoryService.getInventory();
        inventoryService.updateInventory(new InventoryDto());
        ingredientRepository.deleteAll();
	}
	
	/**
	 * Tests InventoryService.createInventory().
	 */
	@Test
	//@Transactional
	public void testCreateInventory() {
		InventoryDto inventoryDto = new InventoryDto();
		inventoryDto.setId(2L);
	    inventoryDto.addIngredient(new Ingredient("Coffee", 50));
	    inventoryDto.addIngredient(new Ingredient("Milk", 100));

	    // Create the inventory using the service
	    InventoryDto createdInventoryDto = inventoryService.createInventory(inventoryDto);

	    // Check contents of returned InventoryDto
	    assertAll("InventoryDto contents",
	        () -> assertNotNull(createdInventoryDto.getId()), // Ensure the ID is not null
	        () -> assertEquals(2, createdInventoryDto.getIngredients().size()), // Check the number of ingredients
	        () -> assertEquals("Coffee", createdInventoryDto.getIngredients().get(0).getName()), // Check the name of the first ingredient
	        () -> assertEquals(50, createdInventoryDto.getIngredients().get(0).getAmount()), // Check the quantity of the first ingredient
	        () -> assertEquals("Milk", createdInventoryDto.getIngredients().get(1).getName()), // Check the name of the second ingredient
	        () -> assertEquals(100, createdInventoryDto.getIngredients().get(1).getAmount()) // Check the quantity of the second ingredient
	    );
	    inventoryRepository.deleteById(createdInventoryDto.getId());
	    
	}
	
	/**
     * Tests InventoryService.checkIngredients() with invalid quantity.
     */
    @Test
    //@Transactional
    public void testCheckIngredientsInvalidQuantity() {
        
        ingredientController.createIngredient(new IngredientDto("Coffee", 5));
        
        // Create a list of ingredients with an invalid quantity
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("Coffee", -5));
        
        // Expect IllegalArgumentException due to negative quantity
        assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.checkIngredients(ingredients);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.checkIngredients(new ArrayList<Ingredient>());
        });
        
        List<Ingredient> ingredients2 = new ArrayList<Ingredient>();
        ingredients2.add(new Ingredient("Milk", 5));
        assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.checkIngredients(ingredients2);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.checkIngredients(null);
        });
        
        List<Ingredient> ingredients3 = new ArrayList<Ingredient>();
        ingredients3.add(new Ingredient("Coffee", 5));
        ingredients3.add(new Ingredient("Coffee", 4));
        assertThrows(ConflictException.class, () -> {
            inventoryService.checkIngredients(ingredients3);
        });
        
        List<Ingredient> ingredients4 = new ArrayList<Ingredient>();
        ingredients4.add(new Ingredient("Coffee", 5));
        ingredients4.add(null);
        
        assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.checkIngredients(ingredients4);
        });
        
        
    }
    
}
