/**
 * 
 */
package edu.ncsu.csc326.coffee_maker.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.exception.ResourceNotFoundException;
import edu.ncsu.csc326.coffee_maker.repositories.RecipeRepository;

/**
 * tests RecipeService
 */
@SpringBootTest
class RecipeServiceTest {
	
	/** the recipe service */
	@Autowired
	private RecipeService recipeService;

	/** Reference to recipe repository */
	@Autowired
	private RecipeRepository recipeRepository;

	
	/**
	 * Sets up the test case.
	 * @throws java.lang.Exception if error
	 */
	@BeforeEach
	public void setUp() throws Exception {
		recipeRepository.deleteAll();
	}
	
    /**
     * tests if two ingredients are the same
     * @param i1 an ingredient
     * @param i2 an ingredient
     * @return whether both name and amount are same for both ingredients
     */
	private boolean exactIngredients(Ingredient i1, Ingredient i2) {
	    return i1.equals(i2) && i1.getAmount() == i2.getAmount();
	}
	
    
	
	/**
	 * Test method for {@link edu.ncsu.csc326.coffee_maker.services.RecipeService#createRecipe(edu.ncsu.csc326.coffee_maker.dto.RecipeDto)}.
	 */
	@Test
	@Transactional
	void testCreateRecipe() {
		RecipeDto recipeDto = new RecipeDto(0L, "Coffee", 50);	
		Ingredient i1 = new Ingredient("Coffee", 4);
		Ingredient i2 = new Ingredient("Milk", 1);
		recipeDto.addIngredient(i1);
		recipeDto.addIngredient(i2);
		RecipeDto savedRecipe = recipeService.createRecipe(recipeDto);
		assertAll("Recipe contents",
				() -> assertTrue(savedRecipe.getId() >= 1L),
				() -> assertEquals("Coffee", savedRecipe.getName()),
				() -> assertEquals(50, savedRecipe.getPrice()),
				() -> assertEquals(2, savedRecipe.getIngredients().size()),
				() -> assertTrue(exactIngredients(i1, savedRecipe.getIngredients().get(0))),
				() -> assertTrue(exactIngredients(i2, savedRecipe.getIngredients().get(1))));
		
		RecipeDto retrievedRecipe = recipeService.getRecipeById(savedRecipe.getId());
		assertAll("Recipe contents",
				() -> assertEquals(savedRecipe.getId(), retrievedRecipe.getId()),
				() -> assertEquals("Coffee", retrievedRecipe.getName()),
				() -> assertEquals(50, retrievedRecipe.getPrice()),
                () -> assertEquals(2, retrievedRecipe.getIngredients().size()),
                () -> assertTrue(exactIngredients(i1, retrievedRecipe.getIngredients().get(0))),
                () -> assertTrue(exactIngredients(i2, retrievedRecipe.getIngredients().get(1))));
	}

	/**
	 * Test method for {@link edu.ncsu.csc326.coffee_maker.services.RecipeService#getRecipeById(java.lang.Long)}.
	 */
	@Test
	@Transactional
	void testGetRecipeById() {
		RecipeDto recipeDto1 = new RecipeDto(0L, "Coffee", 50);	
        Ingredient i1 = new Ingredient("Coffee", 4);
        Ingredient i2 = new Ingredient("Milk", 1);
        recipeDto1.addIngredient(i1);
        recipeDto1.addIngredient(i2);
		RecipeDto savedRecipe1 = recipeService.createRecipe(recipeDto1);
		
		assertThrows(ResourceNotFoundException.class, () -> recipeService.getRecipeById(0L)); // the id is not the id supplied
		RecipeDto recipeDto2 = new RecipeDto(0L, "Latte", 100);
        Ingredient i3 = new Ingredient("Coffee", 2);
        Ingredient i4 = new Ingredient("Milk", 2);
        recipeDto2.addIngredient(i3);
        recipeDto2.addIngredient(i4);
		RecipeDto savedRecipe2 = recipeService.createRecipe(recipeDto2);
		
		RecipeDto retrievedRecipe1 = recipeService.getRecipeById(savedRecipe1.getId());
		assertAll("Recipe contents",
				() -> assertEquals(savedRecipe1.getId(), retrievedRecipe1.getId()),
				() -> assertEquals("Coffee", retrievedRecipe1.getName()),
				() -> assertEquals(50, retrievedRecipe1.getPrice()),
                () -> assertEquals(2, retrievedRecipe1.getIngredients().size()),
                () -> assertTrue(exactIngredients(i1, retrievedRecipe1.getIngredients().get(0))),
                () -> assertTrue(exactIngredients(i2, retrievedRecipe1.getIngredients().get(1))));
		
		RecipeDto retrievedRecipe2 = recipeService.getRecipeById(savedRecipe2.getId());
		assertAll("Recipe contents",
				() -> assertEquals(savedRecipe2.getId(), retrievedRecipe2.getId()),
				() -> assertEquals("Latte", retrievedRecipe2.getName()),
				() -> assertEquals(100, retrievedRecipe2.getPrice()),
                () -> assertEquals(2, retrievedRecipe2.getIngredients().size()),
                () -> assertTrue(exactIngredients(i3, retrievedRecipe2.getIngredients().get(0))),
                () -> assertTrue(exactIngredients(i4, retrievedRecipe2.getIngredients().get(1))));
	}

	/**
	 * Test method for {@link edu.ncsu.csc326.coffee_maker.services.RecipeService#getRecipeByName(java.lang.String)}.
	 */
	@Test
	@Transactional
	void testGetRecipeByName() {
		RecipeDto recipeDto1 = new RecipeDto(0L, "Coffee", 50);	
        Ingredient i1 = new Ingredient("Coffee", 4);
        Ingredient i2 = new Ingredient("Milk", 1);
        recipeDto1.addIngredient(i1);
        recipeDto1.addIngredient(i2);
		RecipeDto savedRecipe1 = recipeService.createRecipe(recipeDto1);
		
		assertThrows(ResourceNotFoundException.class, () -> recipeService.getRecipeByName("Latte"));
		RecipeDto recipeDto2 = new RecipeDto(0L, "Latte", 100);
        Ingredient i3 = new Ingredient("Coffee", 2);
        Ingredient i4 = new Ingredient("Milk", 2);
        recipeDto2.addIngredient(i3);
        recipeDto2.addIngredient(i4);
		RecipeDto savedRecipe2 = recipeService.createRecipe(recipeDto2);
		
		RecipeDto retrievedRecipe1 = recipeService.getRecipeByName(savedRecipe1.getName());
		assertAll("Recipe contents",
				() -> assertEquals(savedRecipe1.getId(), retrievedRecipe1.getId()),
				() -> assertEquals("Coffee", retrievedRecipe1.getName()),
				() -> assertEquals(50, retrievedRecipe1.getPrice()),
                () -> assertEquals(2, retrievedRecipe1.getIngredients().size()),
                () -> assertTrue(exactIngredients(i1, retrievedRecipe1.getIngredients().get(0))),
                () -> assertTrue(exactIngredients(i2, retrievedRecipe1.getIngredients().get(1))));
		
		RecipeDto retrievedRecipe2 = recipeService.getRecipeByName(savedRecipe2.getName());
		assertAll("Recipe contents",
				() -> assertEquals(savedRecipe2.getId(), retrievedRecipe2.getId()),
				() -> assertEquals("Latte", retrievedRecipe2.getName()),
				() -> assertEquals(100, retrievedRecipe2.getPrice()),
                () -> assertEquals(2, retrievedRecipe2.getIngredients().size()),
                () -> assertTrue(exactIngredients(i3, retrievedRecipe2.getIngredients().get(0))),
                () -> assertTrue(exactIngredients(i4, retrievedRecipe2.getIngredients().get(1))));
	}

	/**
	 * Test method for {@link edu.ncsu.csc326.coffee_maker.services.RecipeService#isDuplicateName(java.lang.String)}.
	 */
	@Test
	@Transactional
	void testIsDuplicateName() {
		assertFalse(recipeService.isDuplicateName(""));
		assertFalse(recipeService.isDuplicateName("Coffee"));
		assertFalse(recipeService.isDuplicateName("Coffee"));
		assertFalse(recipeService.isDuplicateName("Latte"));
		
		RecipeDto recipeDto1 = new RecipeDto(0L, "Coffee", 50);	
		recipeService.createRecipe(recipeDto1);
		
		assertFalse(recipeService.isDuplicateName(""));
		assertTrue(recipeService.isDuplicateName("Coffee"));
		assertFalse(recipeService.isDuplicateName("Latte"));
		
		RecipeDto recipeDto2 = new RecipeDto(0L, "Latte", 100);
		recipeService.createRecipe(recipeDto2);
		
		assertFalse(recipeService.isDuplicateName(""));
		assertTrue(recipeService.isDuplicateName("Coffee"));
		assertTrue(recipeService.isDuplicateName("Latte"));
	}

	/**
	 * Test method for {@link edu.ncsu.csc326.coffee_maker.services.RecipeService#getAllRecipes()}.
	 */
	@Test
	@Transactional
	void testGetAllRecipes() {
		List<RecipeDto> recipes = recipeService.getAllRecipes();
		assertEquals(0, recipes.size());
		
		RecipeDto recipeDto1 = new RecipeDto(0L, "Coffee", 50);	
        Ingredient i1 = new Ingredient("Coffee", 4);
        Ingredient i2 = new Ingredient("Milk", 1);
        recipeDto1.addIngredient(i1);
        recipeDto1.addIngredient(i2);
		RecipeDto savedRecipe1 = recipeService.createRecipe(recipeDto1);
		RecipeDto recipeDto2 = new RecipeDto(0L, "Latte", 100);
        Ingredient i3 = new Ingredient("Coffee", 2);
        Ingredient i4 = new Ingredient("Milk", 2);
        recipeDto2.addIngredient(i3);
        recipeDto2.addIngredient(i4);
		RecipeDto savedRecipe2 = recipeService.createRecipe(recipeDto2);
		
		recipes = recipeService.getAllRecipes();
		assertEquals(2, recipes.size());
		
		RecipeDto retrievedRecipe1 = recipes.get(0);
		assertAll("Recipe contents",
				() -> assertEquals(savedRecipe1.getId(), retrievedRecipe1.getId()),
				() -> assertEquals("Coffee", retrievedRecipe1.getName()),
				() -> assertEquals(50, retrievedRecipe1.getPrice()),
                () -> assertEquals(2, retrievedRecipe1.getIngredients().size()),
                () -> assertTrue(exactIngredients(i1, retrievedRecipe1.getIngredients().get(0))),
                () -> assertTrue(exactIngredients(i2, retrievedRecipe1.getIngredients().get(1))));
		
		RecipeDto retrievedRecipe2 = recipes.get(1);
		assertAll("Recipe contents",
				() -> assertEquals(savedRecipe2.getId(), retrievedRecipe2.getId()),
				() -> assertEquals("Latte", retrievedRecipe2.getName()),
				() -> assertEquals(100, retrievedRecipe2.getPrice()),
                () -> assertEquals(2, retrievedRecipe2.getIngredients().size()),
                () -> assertTrue(exactIngredients(i3, retrievedRecipe2.getIngredients().get(0))),
                () -> assertTrue(exactIngredients(i4, retrievedRecipe2.getIngredients().get(1))));
		
		recipeService.deleteRecipe(savedRecipe1.getId());
		recipes = recipeService.getAllRecipes();
		assertEquals(1, recipes.size());
		
		RecipeDto retrievedRecipe3 = recipes.get(0);
		assertAll("Recipe contents",
				() -> assertEquals(savedRecipe2.getId(), retrievedRecipe3.getId()),
				() -> assertEquals("Latte", retrievedRecipe3.getName()),
				() -> assertEquals(100, retrievedRecipe3.getPrice()));
	}

	/**
	 * Test method for {@link edu.ncsu.csc326.coffee_maker.services.RecipeService#updateRecipe(java.lang.Long, edu.ncsu.csc326.coffee_maker.dto.RecipeDto)}.
	 */
	@Test
	@Transactional
	void testUpdateRecipe() {
		assertThrows(ResourceNotFoundException.class, 
				() -> recipeService.updateRecipe(0L, new RecipeDto(0L, "Coffee", 50)));
		assertThrows(ResourceNotFoundException.class, 
				() -> recipeService.getRecipeByName("Coffee"));
		
		RecipeDto recipeDto1 = new RecipeDto(0L, "Coffee", 50);	
        Ingredient i1 = new Ingredient("Coffee", 4);
        Ingredient i2 = new Ingredient("Milk", 1);
        recipeDto1.addIngredient(i1);
        recipeDto1.addIngredient(i2);
		RecipeDto savedRecipe1 = recipeService.createRecipe(recipeDto1);
		RecipeDto recipeDto2 = new RecipeDto(0L, "Latte", 100);
        Ingredient i3 = new Ingredient("Coffee", 2);
        Ingredient i4 = new Ingredient("Sugar", 2);
        recipeDto2.addIngredient(i3);
        recipeDto2.addIngredient(i4);
		RecipeDto savedRecipe2 = recipeService.createRecipe(recipeDto2);
		
		RecipeDto recipeDto3 = new RecipeDto(0L, "test", 1);
        Ingredient i5 = new Ingredient("Sugar", 50);
        Ingredient i6 = new Ingredient("Milk", 11);
        recipeDto3.addIngredient(i5);
        recipeDto3.addIngredient(i6);
		
		RecipeDto savedRecipe3 = recipeService.updateRecipe(savedRecipe1.getId(), recipeDto3);
		
		RecipeDto retrievedRecipe1 = recipeService.getRecipeById(savedRecipe3.getId());
		
		assertAll("Recipe contents",
				() -> assertEquals(savedRecipe3.getId(), retrievedRecipe1.getId()),
				() -> assertEquals("test", retrievedRecipe1.getName()),
                () -> assertEquals(2, retrievedRecipe1.getIngredients().size()),
                () -> assertTrue(exactIngredients(i5, retrievedRecipe1.getIngredients().get(0))),
                () -> assertTrue(exactIngredients(i6, retrievedRecipe1.getIngredients().get(1))));

		
		RecipeDto retrievedRecipe2 = recipeService.getRecipeById(savedRecipe2.getId());
		assertAll("Recipe contents",
				() -> assertEquals(savedRecipe2.getId(), retrievedRecipe2.getId()),
				() -> assertEquals("Latte", retrievedRecipe2.getName()),
				() -> assertEquals(100, retrievedRecipe2.getPrice()),
                () -> assertEquals(2, retrievedRecipe2.getIngredients().size()),
                () -> assertTrue(exactIngredients(i3, retrievedRecipe2.getIngredients().get(0))),
                () -> assertTrue(exactIngredients(i4, retrievedRecipe2.getIngredients().get(1))));
		
		assertThrows(ResourceNotFoundException.class, 
				() -> recipeService.getRecipeByName("Coffee"));
		
		assertThrows(ResourceNotFoundException.class, () -> recipeService.updateRecipe(-1L, recipeDto3));
		
	}

	/**
	 * Test method for {@link edu.ncsu.csc326.coffee_maker.services.RecipeService#deleteRecipe(java.lang.Long)}.
	 */
	@Test
	@Transactional
	void testDeleteRecipe() {
		assertThrows(ResourceNotFoundException.class, 
				() -> recipeService.deleteRecipe(0L));
		assertThrows(ResourceNotFoundException.class, 
				() -> recipeService.getRecipeByName("Coffee"));
		
		RecipeDto recipeDto1 = new RecipeDto(0L, "Coffee", 50);	
		RecipeDto savedRecipe1 = recipeService.createRecipe(recipeDto1);
		RecipeDto recipeDto2 = new RecipeDto(0L, "Latte", 100);
		RecipeDto savedRecipe2 = recipeService.createRecipe(recipeDto2);
		
		recipeService.deleteRecipe(savedRecipe1.getId());
		assertEquals(1, recipeService.getAllRecipes().size());
		
		assertThrows(ResourceNotFoundException.class, 
				() -> recipeService.getRecipeByName("Coffee"));
		
		RecipeDto retrievedRecipe2 = recipeService.getRecipeByName(savedRecipe2.getName());
		assertAll("Recipe contents",
				() -> assertEquals(savedRecipe2.getId(), retrievedRecipe2.getId()),
				() -> assertEquals("Latte", retrievedRecipe2.getName()),
				() -> assertEquals(100, retrievedRecipe2.getPrice()));
	}
	
	/**
	 * test method for RecipeService.deleteAllRecipes()
	 */
	@Test
	@Transactional
	void testDeleteAllRecipes() {
		
		RecipeDto recipeDto1 = new RecipeDto(0L, "Coffee", 50);	
        Ingredient i1 = new Ingredient("Coffee", 4);
        Ingredient i2 = new Ingredient("Milk", 1);
        recipeDto1.addIngredient(i1);
        recipeDto1.addIngredient(i2);
		recipeService.createRecipe(recipeDto1);
		RecipeDto recipeDto2 = new RecipeDto(0L, "Latte", 100);
        Ingredient i3 = new Ingredient("Coffee", 2);
        Ingredient i4 = new Ingredient("Sugar", 2);
        recipeDto2.addIngredient(i3);
        recipeDto2.addIngredient(i4);
		recipeService.createRecipe(recipeDto2);
		
		recipeService.deleteAllRecipes();
		
		assertEquals(0, recipeService.getAllRecipes().size());
	}

}
