/**
 * 
 */
package edu.ncsu.csc326.coffee_maker.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.repositories.IngredientRepository;
import edu.ncsu.csc326.coffee_maker.services.InventoryService;

/**
 * tests Inventory Controller class
 */
@SpringBootTest
@AutoConfigureMockMvc
public class InventoryControllerTest {
		
	/** Mock MVC for testing controller */
	@Autowired
	private MockMvc mvc;
	
	/** reference to the inventory repository */
	@Autowired
	private InventoryService inventoryService;
	
    /** reference to the ingredient repository */
    @Autowired
    private IngredientRepository ingredientRepository;

	/**
	 * Sets up the test case.  We assume only one inventory row. Because inventory is
	 * treated as a singleton (only one row), we must truncate for 
	 * auto increment on the id to work correctly.
	 * @throws java.lang.Exception if error
	 */
	@BeforeEach
	public void setUp() throws Exception {
	    inventoryService.getInventory();
	    inventoryService.updateInventory(new InventoryDto());
	    ingredientRepository.deleteAll();
	}

	/**
	 * Tests the GET /api/inventory endpoint.
	 * @throws Exception if issue when running the test.
	 */
	@Test
	//@Transactional
	public void testGetInventory() throws Exception {
	    mvc.perform(get("/api/inventory"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.ingredients").isArray())
	            .andExpect(jsonPath("$.ingredients").isEmpty());
	}

	/**
	 * Tests the PUT /api/inventory endpoint.
	 * @throws Exception if issue when running the test.
	 */
	@Test
	public void testUpdateInventory() throws Exception {
	    
	    mvc.perform(post("/api/ingredients")               
	            .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(new Ingredient("coffee", 10)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
	    
       mvc.perform(post("/api/ingredients")               
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(new Ingredient("milk", 20)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
	    
	    /*
	    mvc.perform(put("/api/inventory")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(TestUtils.asJsonString(initialInventory))
	            .accept(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk());	            .contentType(MediaType.APPLICATION_JSON)
	            .content(TestUtils.asJsonString(initialInventory))
	            .accept(MediaType.APPLICATION_JSON)
        */

	    // Verify that the inventory has been initialized
	    mvc.perform(get("/api/inventory"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.ingredients").isArray());

	    // Update the inventory with new ingredients
	    InventoryDto updatedInventory = new InventoryDto();
	    updatedInventory.addIngredient(new Ingredient("coffee", 5));
	    updatedInventory.addIngredient(new Ingredient("milk", 10));

	    mvc.perform(put("/api/inventory")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(TestUtils.asJsonString(updatedInventory))
	            .accept(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.ingredients[?(@.name == 'coffee')].amount").value(5))
	            .andExpect(jsonPath("$.ingredients[?(@.name == 'milk')].amount").value(10));
	    
        // Prepare the JSON list of ingredients for the test
        String ingredient1 = "{\"ingredients\":[{\"name\":\"coffee\", \"amount\":100}, {\"name\":\"Milk\", \"amount\":50}]}";

        mvc.perform(put("/api/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ingredient1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        
        String ingredient2 = "{\"ingredients\":[{\"name\":\"coffee\", \"amount\":50}, {\"name\":\"coffee\", \"amount\":30}]}";

        mvc.perform(put("/api/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ingredient2)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        
        String ingredient3 = "{\"ingredients\":[]}";

        mvc.perform(put("/api/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ingredient3)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
	}

	
}