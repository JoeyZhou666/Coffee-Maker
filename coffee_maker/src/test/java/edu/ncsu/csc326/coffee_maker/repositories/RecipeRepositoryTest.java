/**
 * 
 */
package edu.ncsu.csc326.coffee_maker.repositories;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.entity.Recipe;

/**
 * Tests Recipe repository
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class RecipeRepositoryTest {
    
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
        
        Recipe recipe1 = new Recipe(1L, "Coffee", 50);
        Recipe recipe2 = new Recipe();
        recipe2.setId(2L);
        recipe2.setName("Latte");
        recipe2.setPrice(100);

        recipeRepository.save(recipe1);
        recipeRepository.save(recipe2);
    }

    @Test
    public void testAddIngredients() {
        Recipe recipe1 = new Recipe(1L, "Coffee", 500);
        recipe1.addIngredient(new Ingredient("Coffee", 3));
        recipe1.addIngredient(new Ingredient("Pumpkin Spice", 2));
        recipe1.addIngredient(new Ingredient("Milk", 1));
        
        Recipe savedRecipe = recipeRepository.save(recipe1);
        Optional<Recipe> retrievedRecipe = recipeRepository.findById(savedRecipe.getId());
        assertAll("Recipe contents",
                () -> assertEquals(savedRecipe.getId(), retrievedRecipe.get().getId()),
                () -> assertEquals("Coffee", retrievedRecipe.get().getName()),
                () -> assertEquals(500, retrievedRecipe.get().getPrice()),
                () -> assertEquals(3, retrievedRecipe.get().getIngredients().size()));
        
        Ingredient i1 = retrievedRecipe.get().getIngredients().get(0);
        Ingredient i2 = retrievedRecipe.get().getIngredients().get(1);
        Ingredient i3 = retrievedRecipe.get().getIngredients().get(2);
        
        assertAll("Ingredient contents",
                () -> assertEquals("Coffee", i1.getName()),
                () -> assertEquals(3, i1.getAmount()));
        
        assertAll("Ingredient contents",
                () -> assertEquals("Pumpkin Spice", i2.getName()),
                () -> assertEquals(2, i2.getAmount()));
        
        assertAll("Ingredient contents",
                () -> assertEquals("Milk", i3.getName()),
                () -> assertEquals(1, i3.getAmount()));
    }
    
    @Test
    public void testGetRecipeByName1() {
        Optional<Recipe> recipe = recipeRepository.findByName("Coffee");
        Recipe actualRecipe = recipe.get();
        assertAll("Recipe contents",
                () -> assertEquals("Coffee", actualRecipe.getName()),
                () -> assertEquals(50, actualRecipe.getPrice()),
                () -> assertEquals(0, actualRecipe.getIngredients().size()));
    }
    
    @Test
    public void testGetRecipeByName2() {
        Optional<Recipe> recipe = recipeRepository.findByName("Latte");
        Recipe actualRecipe = recipe.get();
        assertAll("Recipe contents",
                () -> assertEquals("Latte", actualRecipe.getName()),
                () -> assertEquals(100, actualRecipe.getPrice()),
                () -> assertEquals(0, actualRecipe.getIngredients().size()));
    }
    
    @Test
    public void testGetRecipeByNameInvalid() {
        Optional<Recipe> recipe = recipeRepository.findByName("Unknown");
        assertTrue(recipe.isEmpty());
    }
}
