package edu.ncsu.csc326.coffee_maker.mapper;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;

/**
 * Converts between IngredientDto and Ingredient entity.
 * @author William Walton
 */
public class IngredientMapper {

    /**
     * Converts an Ingredient entity to IngredientDto
     * @param ingredient Ingredient to convert
     * @return IngredientDto object
     */
    public static IngredientDto mapToIngredientDto(Ingredient ingredient) {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(ingredient.getId());
        ingredientDto.setName(ingredient.getName());
        ingredientDto.setAmount(ingredient.getAmount());
        return ingredientDto;
    }

    /**
     * Converts an IngredientDto to an Ingredient entity
     * @param ingredientDto IngredientDto to convert
     * @return Ingredient entity
     */
    public static Ingredient mapToIngredient(IngredientDto ingredientDto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientDto.getId());
        ingredient.setName(ingredientDto.getName());
        ingredient.setAmount(ingredientDto.getAmount());
        return ingredient;
    }
}
