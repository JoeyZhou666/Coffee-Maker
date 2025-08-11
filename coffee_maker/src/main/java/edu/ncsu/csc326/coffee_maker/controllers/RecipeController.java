package edu.ncsu.csc326.coffee_maker.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.exception.ConflictException;
import edu.ncsu.csc326.coffee_maker.exception.ResourceNotFoundException;
import edu.ncsu.csc326.coffee_maker.services.IngredientService;
import edu.ncsu.csc326.coffee_maker.services.InventoryService;
import edu.ncsu.csc326.coffee_maker.services.RecipeService;

/**
 * Controller for Recipes.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    /** Connection to RecipeService */
    @Autowired
    private RecipeService recipeService;

    /** connection to inventory to verify ingredients */
    @Autowired
    private InventoryService inventoryService;

    /** connection to ingredient service to remove trailing ingredients when updating */
    @Autowired
    private IngredientService ingredientService;

    /**
     * REST API method to provide GET access to all recipes in the system
     *
     * @return JSON representation of all recipes
     */
    @GetMapping
    public List<RecipeDto> getRecipes () {
        return recipeService.getAllRecipes();
    }

    /**
     * REST API method to provide GET access to a specific recipe, as indicated
     * by the path variable provided (the name of the recipe desired)
     *
     * @param name
     *            recipe name
     * @return response to the request
     */
    @GetMapping("{name}")
    public ResponseEntity<RecipeDto> getRecipe ( @PathVariable("name") final String name ) {
        final RecipeDto recipeDto = recipeService.getRecipeByName(name);
        return ResponseEntity.ok(recipeDto);
    }

    /**
     * REST API method to provide POST access to the Recipe model.
     *
     * @param recipeDto
     *            The valid Recipe to be saved.
     * @return ResponseEntity indicating success if the Recipe could be saved to
     *         the inventory, or an error if it could not be
     */
    @PostMapping
    public ResponseEntity<RecipeDto> createRecipe(@RequestBody final RecipeDto recipeDto) {
        if (recipeDto.getPrice() <= 0) {
            return new ResponseEntity<>(recipeDto, HttpStatus.BAD_REQUEST);
        }
        if (recipeService.isDuplicateName(recipeDto.getName())) {
            return new ResponseEntity<>(recipeDto, HttpStatus.CONFLICT);
        }
        if (recipeService.getAllRecipes().size() < 3) {
            try {
                inventoryService.checkIngredients(recipeDto.getIngredients());
            } catch (final ConflictException e) {
                return new ResponseEntity<>(recipeDto, HttpStatus.CONFLICT);
            } catch (final IllegalArgumentException e) {
                return new ResponseEntity<>(recipeDto, HttpStatus.BAD_REQUEST);
            }
            final RecipeDto savedRecipeDto = recipeService.createRecipe(recipeDto);
            return ResponseEntity.ok(savedRecipeDto);
        } else {
            return new ResponseEntity<>(recipeDto, HttpStatus.INSUFFICIENT_STORAGE);
        }
    }

    /**
     * rest API method to provide put access to the Recipe model.
     *
     * @param recipeId
     *          the id of the recipe
     *
     * @param recipeDto
     *          the recipe to be editSed (found via name)
     * @return ResponseEntity indicating whether it saved or not
     */
    @PutMapping("{id}")
    public ResponseEntity<RecipeDto> updateRecipe(@PathVariable("id") final Long recipeId, @RequestBody final RecipeDto recipeDto) {
        if (recipeDto.getPrice() <= 0) {
            return new ResponseEntity<>(recipeDto, HttpStatus.BAD_REQUEST);
        }
        if (recipeService.isDuplicateName(recipeDto.getName()) && !recipeService.getRecipeByName(recipeDto.getName()).getId().equals(recipeId)) {
            return new ResponseEntity<>(recipeDto, HttpStatus.CONFLICT);
        }
        try {
            inventoryService.checkIngredients(recipeDto.getIngredients());
        } catch (final ConflictException e) {
            return new ResponseEntity<>(recipeDto, HttpStatus.CONFLICT);
        } catch (final IllegalArgumentException e) {
            return new ResponseEntity<>(recipeDto, HttpStatus.BAD_REQUEST);
        }
        try {

            final List<Long> oldIds = new ArrayList<Long>();
            for (final Ingredient ingredient : recipeService.getRecipeById(recipeId).getIngredients()) {
                oldIds.add( ingredient.getId() );
            }

            final RecipeDto savedRecipeDto = recipeService.updateRecipe(recipeId, recipeDto);

            for (final Long id: oldIds) {
                boolean stillThere = false;
                for (final Ingredient ingredient: savedRecipeDto.getIngredients()) {
                    if (id.equals(ingredient.getId())) {
                        stillThere = true;
                        break;
                    }
                }
                if (!stillThere) {
                    ingredientService.deleteIngredient( id );
                }
            }
            return new ResponseEntity<>( savedRecipeDto, HttpStatus.OK);
        } catch (final ResourceNotFoundException e) {
            return new ResponseEntity<>(recipeDto, HttpStatus.GONE);
        }


    }

    /**
     * REST API method to allow deleting a Recipe from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the recipe to delete (as a path variable)
     *
     * @param recipeId
     *            The id of the Recipe to delete
     * @return Success if the recipe could be deleted; an error if the recipe
     *         does not exist
     */
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable("id") final Long recipeId) {
        recipeService.deleteRecipe(recipeId);
        return ResponseEntity.ok("Recipe deleted successfully.");
    }
}