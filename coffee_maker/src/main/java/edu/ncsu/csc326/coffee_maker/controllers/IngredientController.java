package edu.ncsu.csc326.coffee_maker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.mapper.IngredientMapper;
import edu.ncsu.csc326.coffee_maker.services.IngredientService;
import edu.ncsu.csc326.coffee_maker.services.InventoryService;
import edu.ncsu.csc326.coffee_maker.services.RecipeService;

/**
 * controller for recipes
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    /**
     * connection to IngredientService
     */
    @Autowired
    private IngredientService ingredientService;

    /** connection to InventoryService */
    @Autowired
    private InventoryService inventoryService;

    /** connection to RecipeService used when deleting recipes */
    @Autowired
    private RecipeService recipeService;


    /*
    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> getIngredient(@PathVariable("id") Long id) {
        IngredientDto ingredientDto = ingredientService.getIngredientById(id);
        return ResponseEntity.ok(ingredientDto);
    }
     */

    /**
     * REST API endpoint ot provide GET access to ingredients
     * @return a list of all ingredients in the repository
     */
    @GetMapping
    public ResponseEntity<List<IngredientDto>> getAllIngredients() {
        final List<IngredientDto> ingredients = ingredientService.getAllIngredients();
        return ResponseEntity.ok(ingredients);
    }

    /**
     * REST API endpoint to provide POST access to ingredients to add a new ingredient
     * @param ingredientDto the ingredient to be created
     * @return the created ingredient
     */
    @PostMapping
    public ResponseEntity<IngredientDto> createIngredient(@RequestBody final IngredientDto ingredientDto) {
        final InventoryDto inventory = inventoryService.getInventory();
        final List<Ingredient> ingredients = inventory.getIngredients();
        if (ingredients.contains(IngredientMapper.mapToIngredient(ingredientDto))) {
            return new ResponseEntity<IngredientDto>(ingredientDto, HttpStatus.CONFLICT);
        }
        if (ingredientDto.getAmount() <= 0) {
            return new ResponseEntity<IngredientDto>(ingredientDto, HttpStatus.BAD_REQUEST);
        }

        final IngredientDto savedIngredientDto = ingredientService.createIngredient(ingredientDto);
        inventory.addIngredient(IngredientMapper.mapToIngredient(savedIngredientDto));
        inventoryService.updateInventory(inventory);
        return ResponseEntity.ok(savedIngredientDto);
    }

    /*
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable("id") Long id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }
     */

    /**
     * REST API endpoint to provide DELETE access to ingredients. As a biproduct, will have to destroy all
     * of inventory and recipes
     * @return the response for deleting in ingredients
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAllIngredients() {
        inventoryService.getInventory();
        inventoryService.updateInventory(new InventoryDto());
        recipeService.deleteAllRecipes();
        ingredientService.deleteAllIngredients();
        return ResponseEntity.noContent().build();
    }
}