package edu.ncsu.csc326.coffee_maker.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.exception.ConflictException;
import edu.ncsu.csc326.coffee_maker.services.IngredientService;
import edu.ncsu.csc326.coffee_maker.services.InventoryService;

/**
 * Controller for CoffeeMaker's inventory.  The inventory is a singleton; there's
 * only one row in the database that contains the current inventory for the system.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    /**
     * Connection to inventory service for manipulating the Inventory model.
     */
    @Autowired
    private InventoryService inventoryService;

    /**
     * connection to ingredient service for removing trailing ingredients when updating
     */
    @Autowired
    private IngredientService ingredientService;

    /**
     * REST API endpoint to provide GET access to the CoffeeMaker's singleton
     * Inventory.
     *
     * @return response to the request
     */
    @GetMapping
    public ResponseEntity<InventoryDto> getInventory() {
        final InventoryDto inventoryDto = inventoryService.getInventory();
        return ResponseEntity.ok(inventoryDto);
    }

    /**
     * REST API endpoint to provide update access to the CoffeeMaker's singleton
     * Inventory.
     * @param inventoryDto
     *            amounts to add to inventory
     * @return response to the request
     */
    @PutMapping
    public ResponseEntity<InventoryDto> updateInventory(@RequestBody final InventoryDto inventoryDto) {
        try {
            inventoryService.checkIngredients(inventoryDto.getIngredients());
        } catch (final ConflictException e) {
            return new ResponseEntity<>(inventoryDto, HttpStatus.CONFLICT);
        } catch (final IllegalArgumentException e) {
            return new ResponseEntity<>(inventoryDto, HttpStatus.BAD_REQUEST);
        }

        final List<Long> oldIds = new ArrayList<Long>();
        for (final Ingredient ingredient : inventoryService.getInventory().getIngredients()) {
            oldIds.add( ingredient.getId() );
        }

        final InventoryDto savedInventoryDto = inventoryService.updateInventory(inventoryDto);

        for (final Long id: oldIds) {
            boolean stillThere = false;
            for (final Ingredient ingredient: savedInventoryDto.getIngredients()) {
                if (id.equals(ingredient.getId())) {
                    stillThere = true;
                    break;
                }
            }
            if (!stillThere) {
                ingredientService.deleteIngredient( id );
            }
        }

        return ResponseEntity.ok(savedInventoryDto);
    }


}
