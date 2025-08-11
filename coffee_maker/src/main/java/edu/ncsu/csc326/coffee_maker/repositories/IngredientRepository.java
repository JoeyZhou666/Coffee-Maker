package edu.ncsu.csc326.coffee_maker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc326.coffee_maker.entity.Ingredient;

/**
 * repository for ingredients
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}