package edu.ncsu.csc326.coffee_maker.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.mapper.IngredientMapper;
import edu.ncsu.csc326.coffee_maker.repositories.IngredientRepository;
import edu.ncsu.csc326.coffee_maker.repositories.RecipeRepository;
import jakarta.transaction.Transactional;

/**
 * tests the IngredientServiceImpl class
 */
@SpringBootTest
public class IngredientServiceTest {


    /** the ingredient service being tested */
    @Autowired
    private IngredientService ingredientService;

    /** the repository of the inventory to help in testing */
    @Autowired
    private InventoryService inventoryService;

    /** used for testing deleteAll */
    @Autowired
    private IngredientRepository ingredientRepository;

    /** used for ensuring environment has no recipes as this interferes with delete all */
    @Autowired
    private RecipeRepository recipeRepository;

    /**
     * setup deltes all existant ingredients
     * @throws Exception if trouble deleting ingredients
     */
    @BeforeEach
    public void setUp() throws Exception {
        inventoryService.getInventory();
        inventoryService.updateInventory(new InventoryDto());
        recipeRepository.deleteAll();
        ingredientService.deleteAllIngredients();
    }

    /**
     * tests creating ingredients
     */
    @Test
    @Transactional
    public void testCreateIngredient() {

        final IngredientDto ingredient1 = new IngredientDto("Coffee", 5);

        final IngredientDto createdIngredient1 = ingredientService.createIngredient(ingredient1);
        assertAll("Ingredient contents",
                () -> assertEquals("Coffee", createdIngredient1.getName()),
                () -> assertEquals(5, createdIngredient1.getAmount()));

        final IngredientDto ingredient2 = new IngredientDto("Pumpkin Spice", 10);
        final IngredientDto createdIngredient2 = ingredientService.createIngredient(ingredient2);
        assertAll("Ingredient contents",
                () -> assertEquals("Pumpkin Spice", createdIngredient2.getName()),
                () -> assertEquals(10, createdIngredient2.getAmount()));
    }

    /*
	@Test
	@Transactional
	public void testGetIngredientById() {

		IngredientDto ingredient1 = new IngredientDto(IngredientType.COFFEE, 5);

		IngredientDto createdIngredient1 = ingredientService.createIngredient(ingredient1);
		IngredientDto fetchedIngredient1 = ingredientService.getIngredientById(createdIngredient1.getId());
		assertAll("Ingredient contents",
				() -> assertEquals(IngredientType.COFFEE, fetchedIngredient1.getIngredientType()),
				() -> assertEquals(5, fetchedIngredient1.getAmount()));

		IngredientDto ingredient2 = new IngredientDto(IngredientType.PUMPKIN_SPICE, 10);
		IngredientDto createdIngredient2 = ingredientService.createIngredient(ingredient2);
		IngredientDto fetchedIngredient2 = ingredientService.getIngredientById(createdIngredient2.getId());
		assertAll("Ingredient contents",
				() -> assertEquals(IngredientType.PUMPKIN_SPICE, fetchedIngredient2.getIngredientType()),
				() -> assertEquals(10, fetchedIngredient2.getAmount()));
	}
     */


    /**
     * tests getAllIngredients()
     */
    @Test
    @Transactional
    public void testGetAllIngredients() {

        final List<IngredientDto> allIngredients1 = ingredientService.getAllIngredients();
        assertEquals(0, allIngredients1.size());

        final IngredientDto ingredient1 = new IngredientDto("coffee", 5);
        final IngredientDto ingredient2 = new IngredientDto("milk", 4);

        final IngredientDto savedIngredient1 = ingredientService.createIngredient(ingredient1);
        final IngredientDto savedIngredient2 = ingredientService.createIngredient(ingredient2);

        final List<IngredientDto> allIngredients2 = ingredientService.getAllIngredients();
        assertEquals(2, allIngredients2.size());
        assertEquals(savedIngredient1.getId(), allIngredients2.get(0).getId());
        assertEquals(savedIngredient1.getName(), allIngredients2.get(0).getName());
        assertEquals(savedIngredient1.getAmount(), allIngredients2.get(0).getAmount());
        assertEquals(savedIngredient2.getId(), allIngredients2.get(1).getId());
        assertEquals(savedIngredient2.getName(), allIngredients2.get(1).getName());
        assertEquals(savedIngredient2.getAmount(), allIngredients2.get(1).getAmount());
    }


    /**
     * tests deleting an ingredient
     */
    @Test
    @Transactional
    public void testDeleteIngredient() {
        final IngredientDto ingredient1 = new IngredientDto("coffee", 5);
        final IngredientDto ingredient2 = new IngredientDto("milk", 4);

        ingredientService.createIngredient(ingredient1);
        final IngredientDto savedIngredient2 = ingredientService.createIngredient(ingredient2);

        ingredientService.deleteIngredient(savedIngredient2.getId());

        final List<IngredientDto> ingredients = ingredientService.getAllIngredients();
        assertEquals(1, ingredients.size());
        assertEquals("coffee", ingredients.get(0).getName());
    }

    /**
     * tests deleting all ingredients
     */
    @Test
    @Transactional
    public void testDeleteAllIngredients() {
        final IngredientDto ingredient1 = new IngredientDto("Cofffee", 5);
        ingredientRepository.save(IngredientMapper.mapToIngredient(ingredient1));
        ingredientRepository.save(IngredientMapper.mapToIngredient(ingredient1));

        ingredientService.deleteAllIngredients();

        final List<Ingredient> allIngredients = inventoryService.getInventory().getIngredients();
        assertEquals(0, allIngredients.size());
    }

}