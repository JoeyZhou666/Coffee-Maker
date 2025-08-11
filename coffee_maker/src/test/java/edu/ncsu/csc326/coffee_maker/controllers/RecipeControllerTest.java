package edu.ncsu.csc326.coffee_maker.controllers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.repositories.IngredientRepository;
import edu.ncsu.csc326.coffee_maker.repositories.RecipeRepository;
import edu.ncsu.csc326.coffee_maker.services.InventoryService;


/**
 * tests the RecipeController class
 */
@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerTest {
	
	/** Mock MVC for testing controller */
	@Autowired
	private MockMvc mvc;

	/** Reference to recipe repository */
	@Autowired
	private RecipeRepository recipeRepository;

	/** used to clear inventory after use */
	@Autowired
    private InventoryService inventoryService;

    /** used to clear ingredients after use */
	@Autowired
    private IngredientRepository ingredientRepository;

    /** used to create ingredients */
	@Autowired
    private IngredientController ingredientController;
	
    
    
	/**
	 * Sets up the test case.
	 * @throws java.lang.Exception if error
	 */
	@BeforeEach
	public void setUp() throws Exception {
        inventoryService.getInventory();
        inventoryService.updateInventory(new InventoryDto());
        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();
        
        ingredientController.createIngredient(new IngredientDto("coffee", 10));
        ingredientController.createIngredient(new IngredientDto("milk", 10));
        ingredientController.createIngredient(new IngredientDto("sugar", 10));
	}
	
	
	/**
	 * tests RecipeController.getRecipes()
	 * @throws Exception if setup fails
	 */
	@Test
	@Transactional
	public void testGetRecipes() throws Exception {
	   
	    String recipe = mvc.perform(get("/api/recipes"))
	            .andDo(print())  
	            .andExpect(status().isOk())
	            .andReturn().getResponse().getContentAsString();

	    
	    assertFalse(recipe.contains("Mocha"));
	}
	
	
	/**
	 * tests RecipeController.createRecipe()
	 * also tests the getRecipe() method
	 * @throws Exception if setup fails
	 */
	@Test
    @Transactional
    public void testCreateRecipe() throws Exception {
        RecipeDto recipeDto1 = new RecipeDto(0L, "Mocha", 200);
        recipeDto1.getIngredients().add(new Ingredient("coffee", 2));
        recipeDto1.getIngredients().add(new Ingredient("milk", 1));
        
        mvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto1))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mocha"))
                .andExpect(jsonPath("$.price").value(200))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'coffee')].amount").value(2))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'milk')].amount").value(1));
        
        String recipe = mvc.perform(get("/api/recipes"))
	            .andExpect(status().isOk())
	            .andReturn().getResponse().getContentAsString();

	    
	    assertTrue(recipe.contains("Mocha"));
	    assertTrue(recipe.contains("\"name\":\"Mocha\",\"price\":200"));
	    
	    mvc.perform(get("/api/recipes/Mocha"))
	            .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mocha"))
                .andExpect(jsonPath("$.price").value(200));
	    
	    RecipeDto recipeDto2 = new RecipeDto(0L, "Mocha", 100);
	    
        mvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto2))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CONFLICT.value()));
        
        mvc.perform(get("/api/recipes/Mocha"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(recipeDto1.getName()))
                .andExpect(jsonPath("$.price").value(recipeDto1.getPrice()));
        
        RecipeDto recipeDto3 = new RecipeDto(1L, "Coffee", 50);
        
        recipeDto3.getIngredients().add(new Ingredient("coffee", 3));
        recipeDto3.getIngredients().add(new Ingredient("sugar", 1));
        mvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto3))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(recipeDto3.getName()))
                .andExpect(jsonPath("$.price").value(recipeDto3.getPrice()))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'coffee')].amount").value(3))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'sugar')].amount").value(1));
        
        RecipeDto recipeDto5 = new RecipeDto(0L, "Almond Milk", 4);
        recipeDto5.getIngredients().add(new Ingredient("milk", 3));
        recipeDto5.getIngredients().add(new Ingredient("milk", 5));
        mvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto5))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CONFLICT.value()));
        
        RecipeDto recipeDto7 = new RecipeDto(0L, "NyQuil", 100);
        recipeDto7.addIngredient(new Ingredient("coffee", -5));
        mvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto7))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value(recipeDto7.getName()))
                .andExpect(jsonPath("$.price").value(recipeDto7.getPrice()))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'coffee')].amount").value(-5));
        
        RecipeDto recipeDto8 = new RecipeDto(0L, "ToxicWaste", -100);
        recipeDto8.addIngredient(new Ingredient("coffee", 5));
        mvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto8))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value(recipeDto8.getName()))
                .andExpect(jsonPath("$.price").value(recipeDto8.getPrice()))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'coffee')].amount").value(5));
        
        
        RecipeDto recipeDto4 = new RecipeDto(2L, "Latte", 100);
        recipeDto4.getIngredients().add(new Ingredient("coffee", 3));
        recipeDto4.getIngredients().add(new Ingredient("milk", 1));
        recipeDto4.getIngredients().add(new Ingredient("sugar", 1));
        mvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto4))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(recipeDto4.getName()))
                .andExpect(jsonPath("$.price").value(recipeDto4.getPrice()))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'coffee')].amount").value(3))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'milk')].amount").value(1))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'sugar')].amount").value(1));
        
        mvc.perform(get("/api/recipes/Coffee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(recipeDto3.getName()))
                .andExpect(jsonPath("$.price").value(recipeDto3.getPrice()))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'coffee')].amount").value(3))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'sugar')].amount").value(1));
        
        mvc.perform(get("/api/recipes/Latte"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(recipeDto4.getName()))
                .andExpect(jsonPath("$.price").value(recipeDto4.getPrice()))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'coffee')].amount").value(3))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'milk')].amount").value(1))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'sugar')].amount").value(1));
        
        /*
        mvc.perform(get("/api/recipes/Mocha"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(recipeDto1.getName()))
                .andExpect(jsonPath("$.price").value(recipeDto1.getPrice()))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'coffee')].amount").value(2))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'milk')].amount").value(1));
                
         */
	    
    }
	
	/**
	 * tests the RecipeController.updateRecipe()
	 * @throws Exception of mvc fails
	 */
	@Test
	@Transactional
	public void testUpdateRecipe() throws Exception {
        RecipeDto recipeDto1 = new RecipeDto(1L, "Coffee", 50);
        recipeDto1.getIngredients().add(new Ingredient("coffee", 3));
        recipeDto1.getIngredients().add(new Ingredient("sugar", 1));
        
        
        Long id1 = TestUtils.parseIdFromJson(mvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto1))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(recipeDto1.getName()))
                .andExpect(jsonPath("$.price").value(recipeDto1.getPrice()))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'coffee')].amount").value(3))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'sugar')].amount").value(1))
                .andReturn().getResponse().getContentAsString());
                
                
        RecipeDto recipeDto2 = new RecipeDto(2L, "Latte", 100);
        recipeDto2.getIngredients().add(new Ingredient("coffee", 3));
        recipeDto2.getIngredients().add(new Ingredient("milk", 1));
        recipeDto2.getIngredients().add(new Ingredient("sugar", 1));
        TestUtils.parseIdFromJson(mvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto2))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(recipeDto2.getName()))
                .andExpect(jsonPath("$.price").value(recipeDto2.getPrice()))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'coffee')].amount").value(3))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'milk')].amount").value(1))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'sugar')].amount").value(1))
                .andReturn().getResponse().getContentAsString());
        
        RecipeDto recipeDto3 = new RecipeDto(0L, "air", 200);
        
        mvc.perform(put("/api/recipes/" + id1)                
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto3))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        
        RecipeDto recipeDto4 = new RecipeDto(0L, "Two Coffees", 200);
        recipeDto4.addIngredient(new Ingredient("coffee", 4));
        recipeDto4.addIngredient(new Ingredient("coffee", 3));
        mvc.perform(put("/api/recipes/" + id1)                
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto4))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        

        mvc.perform(put("/api/recipes/" + id1)                
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto2))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        
        
        RecipeDto recipeDto5 = new RecipeDto(0L, "Mocha", -100);
        recipeDto5.addIngredient(new Ingredient("coffee", 4));
        recipeDto5.addIngredient(new Ingredient("milk", 1));
        
        mvc.perform(put("/api/recipes/" + id1)                
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto5))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        recipeDto5.setPrice(200);
        
        mvc.perform(put("/api/recipes/" + 0L)                
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto5))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isGone());
        
        RecipeDto recipeDto6 = new RecipeDto(0L, "potion", 100);
        recipeDto6.addIngredient(new Ingredient("space dust", 100));
        
        mvc.perform(get("/api/recipes/Coffee"))
        .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(recipeDto1.getName()))
                .andExpect(jsonPath("$.price").value(recipeDto1.getPrice()))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'coffee')].amount").value(3))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'sugar')].amount").value(1));
        
        mvc.perform(put("/api/recipes/" + id1)                
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto5))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        mvc.perform(get("/api/recipes/Mocha"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(recipeDto5.getName()))
                .andExpect(jsonPath("$.price").value(recipeDto5.getPrice()))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'coffee')].amount").value(4))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'milk')].amount").value(1));
        
        mvc.perform(get("/api/recipes/Coffee"))
                .andExpect(status().isNotFound());
        
        recipeDto5.setPrice(54);
        
        mvc.perform(put("/api/recipes/" + id1)                
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto5))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        mvc.perform(get("/api/recipes/Mocha"))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$.name").value(recipeDto5.getName()))
		        .andExpect(jsonPath("$.price").value(recipeDto5.getPrice()))
		        .andExpect(jsonPath("$.ingredients[?(@.name == 'coffee')].amount").value(4))
		        .andExpect(jsonPath("$.ingredients[?(@.name == 'milk')].amount").value(1));
	}
	
	/**
	 * tests the RecipeController.deleteRecipe()
	 * @throws Exception if setup fails
	 */
	@Test
	@Transactional
	public void testDeleteRecipe() throws Exception {
	    // First, create and save a recipe
        RecipeDto recipeDto3 = new RecipeDto(1L, "Coffee", 50);
        recipeDto3.getIngredients().add(new Ingredient("coffee", 3));
        recipeDto3.getIngredients().add(new Ingredient("sugar", 1));
        String response = mvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto3))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(recipeDto3.getName()))
                .andExpect(jsonPath("$.price").value(recipeDto3.getPrice()))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'coffee')].amount").value(3))
                .andExpect(jsonPath("$.ingredients[?(@.name == 'sugar')].amount").value(1))
                .andReturn().getResponse().getContentAsString();

	    // Extract the recipe ID from the response
	    Long recipeId = TestUtils.parseIdFromJson(response);

	    // Then, delete the recipe by its ID
	    mvc.perform(delete("/api/recipes/" + recipeId)
	            .accept(MediaType.APPLICATION_JSON))
	            .andDo(print())
	            .andExpect(status().isOk())
	            .andExpect(content().string("Recipe deleted successfully."));
	    
        String recipe = mvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        
        assertFalse(recipe.contains("Mocha"));
	}


}