package edu.ncsu.csc326.coffee_maker.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.exception.ResourceNotFoundException;
import edu.ncsu.csc326.coffee_maker.mapper.IngredientMapper;
import edu.ncsu.csc326.coffee_maker.repositories.IngredientRepository;
import edu.ncsu.csc326.coffee_maker.services.IngredientService;

/**
 * Implementation of the IngredientService interface.
 */
@Service
public class IngredientServiceImpl implements IngredientService {

    /** Connection to the repository to work with the DAO + database */
    @Autowired
    private IngredientRepository ingredientRepository;

    /**
     * Creates a ingredient with the given information.
     * @param ingredientDto ingredient to create
     * @return created ingredient
     */
    @Override
    public IngredientDto createIngredient(final IngredientDto ingredientDto) {
        final Ingredient ingredient = IngredientMapper.mapToIngredient(ingredientDto);
        final Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return IngredientMapper.mapToIngredientDto(savedIngredient);
    }

    /*
     * Returns the ingredient with the given id.
     * @param ingredientId ingredient's id
     * @return the ingredient with the given id
     * @throws ResourceNotFoundException if the ingredient doesn't exist
     *
	public IngredientDto getIngredientById(Long ingredientId) {
		Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow(
				() -> new ResourceNotFoundException("Ingredient does not exist with id " + ingredientId)
		);
		return IngredientMapper.mapToIngredientDto(ingredient);
	}
     */


    /**
     * Returns a list of all the ingredients
     * @return all the ingredients
     */
    @Override
    public List<IngredientDto> getAllIngredients() {
        final List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredients.stream().map((ingredient) -> IngredientMapper.mapToIngredientDto(ingredient)).collect(Collectors.toList());
    }

    /**
     * Deletes the ingredient with the given id
     * @param ingredientId recipe's id
     * @throws ResourceNotFoundException if the ingredient doesn't exist
     */
    @Override
    public void deleteIngredient(final Long ingredientId) {
        final Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow(
                () -> new ResourceNotFoundException("Recipe does not exist with id " + ingredientId)
                );

        ingredientRepository.delete(ingredient);
    }


    @Override
    public void deleteAllIngredients() {
        ingredientRepository.deleteAll();
    }
}
