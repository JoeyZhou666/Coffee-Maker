package edu.ncsu.csc326.coffee_maker.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.services.IngredientService;
import edu.ncsu.csc326.coffee_maker.services.InventoryService;

/**
 * tests IngredientController
 */
@SpringBootTest
@AutoConfigureMockMvc
public class IngredientControllerTest {

    /** reference to inventory service */
    @Autowired
    private InventoryService inventoryService;

    /** reference to ingredient service */
    @Autowired
    private IngredientService ingredientService;

    /** the controller being tested */
    @Autowired
    private IngredientController ingredientController;

    /** the MVC being tested */
    @Autowired
    private MockMvc mockMvc;


    /**
     * setup that starts up MVC before each test
     */
    @BeforeEach
    public void setup() throws Exception {
        inventoryService.getInventory();
        inventoryService.updateInventory(new InventoryDto());
        ingredientController.deleteAllIngredients();
    }

    /*
    @Test
    public void testGetIngredientById() throws Exception {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(1L);
        ingredientDto.setIngredientType(IngredientType.COFFEE);
        ingredientDto.setAmount(5);

        when(ingredientService.getIngredientById(1L)).thenReturn(ingredientDto);

        mockMvc.perform(get("/api/ingredients/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.ingredientType").value("COFFEE"))
                .andExpect(jsonPath("$.amount").value(5));
    }
     */


    /**
     * tests getAllIngredients
     * @throws Exception if test goes wrong
     */
    @Test
    public void testGetAllIngredients() throws Exception {
        final Long id1 = ingredientService.createIngredient(new IngredientDto("Coffee", 5)).getId();
        final Long id2 = ingredientService.createIngredient(new IngredientDto("Milk", 21)).getId();

        //when(ingredientService.getAllIngredients()).thenReturn(Arrays.asList(ingredient1, ingredient2));

        mockMvc.perform(get("/api/ingredients")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(id1))
        .andExpect(jsonPath("$[0].name").value("Coffee"))
        .andExpect(jsonPath("$[0].amount").value(5))
        .andExpect(jsonPath("$[1].id").value(id2))
        .andExpect(jsonPath("$[1].name").value("Milk"))
        .andExpect(jsonPath("$[1].amount").value(21));
    }


    /**
     * tests creating an ingredient
     * @throws Exception if test goes wrong
     */
    @Test
    public void testCreateIngredient() throws Exception {
        final IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setName("Coffee");
        ingredientDto.setAmount(5);

        /*
        when(ingredientService.createIngredient(any(IngredientDto.class)))
            .thenAnswer(invocation -> {
                IngredientDto dto = invocation.getArgument(0);
                dto.setId(1L);
                return dto;
            });
         */
        mockMvc.perform(post("/api/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Coffee\",\"amount\":5}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Coffee"))
        .andExpect(jsonPath("$.amount").value(5));


        mockMvc.perform(post("/api/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Milk\",\"amount\":21}"))
        .andExpect(jsonPath("$.name").value("Milk"))
        .andExpect(jsonPath("$.amount").value(21));


        mockMvc.perform(post("/api/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Coffee\",\"amount\":5}"))
        .andExpect(status().is(HttpStatus.CONFLICT.value()))
        .andExpect(jsonPath("$.name").value("Coffee"))
        .andExpect(jsonPath("$.amount").value(5));

        mockMvc.perform(post("/api/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Walnut\",\"amount\":0}"))
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(jsonPath("$.name").value("Walnut"))
        .andExpect(jsonPath("$.amount").value(0));
    }

    /*
    @Test
    public void testDeleteIngredient() throws Exception {
        mockMvc.perform(delete("/api/ingredients/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
     */

    /**
     * tests deleting all ingredients
     * @throws Exception if test goes wrong
     */
    @Test
    public void testDeleteAllIngredients() throws Exception {

        mockMvc.perform(post("/api/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"coffee\",\"amount\":5}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("coffee"))
        .andExpect(jsonPath("$.amount").value(5));


        mockMvc.perform(post("/api/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"milk\",\"amount\":21}"))
        .andExpect(jsonPath("$.name").value("milk"))
        .andExpect(jsonPath("$.amount").value(21));

        final RecipeDto recipeDto1 = new RecipeDto(0L, "Mocha", 200);
        recipeDto1.getIngredients().add(new Ingredient("coffee", 2));
        recipeDto1.getIngredients().add(new Ingredient("milk", 1));

        mockMvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto1))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Mocha"))
        .andExpect(jsonPath("$.price").value(200))
        .andExpect(jsonPath("$.ingredients[?(@.name == 'coffee')].amount").value(2))
        .andExpect(jsonPath("$.ingredients[?(@.name == 'milk')].amount").value(1));

        mockMvc.perform(delete("/api/ingredients")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

        final String contents = mockMvc.perform(get("/api/ingredients"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals("[]", contents);
    }

}