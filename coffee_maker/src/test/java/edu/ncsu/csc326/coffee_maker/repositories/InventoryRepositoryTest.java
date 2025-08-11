package edu.ncsu.csc326.coffee_maker.repositories;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.entity.Inventory;

/**
 * Tests InventoryRepository.  Uses the real database - not an embedded one.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class InventoryRepositoryTest {

    /** Reference to inventory repository */
    @Autowired
    private InventoryRepository inventoryRepository;


    /**
     * Test saving the inventory and retrieving from the repository.
     */
    @Test
    public void testSaveAndGetInventory() {
        final Inventory newInventory = new Inventory();
        inventoryRepository.save(newInventory);

        // Fetch the newly saved inventory using the generated ID
        final Long savedId = newInventory.getId();
        final Inventory fetchedInventory = inventoryRepository.findById(savedId)
                .orElseThrow(() -> new AssertionError("Inventory with ID " + savedId + " not found"));

        assertAll("Inventory contents",
                () -> assertEquals(savedId, fetchedInventory.getId()));
        inventoryRepository.deleteById(savedId);
    }

    /**
     * Tests updating the inventory
     */
    @Test
    public void testUpdateInventory() {

        final Inventory newInventory = new Inventory();
        inventoryRepository.save(newInventory);

        // Fetch the newly saved inventory using the generated ID
        final Long savedId = newInventory.getId();
        // Fetch the inventory (get the first available one)
        final Inventory fetchedInventory = inventoryRepository.findAll().get(inventoryRepository.findAll().size() - 1);

        final Ingredient ingredient = new Ingredient();
        ingredient.setName("Coffee");
        ingredient.setAmount(50);
        fetchedInventory.addIngredient(ingredient);

        final Inventory updatedInventory = inventoryRepository.save(fetchedInventory);

        final Inventory fetchedUpdatedInventory = inventoryRepository.findById(updatedInventory.getId()).get();

        assertAll("Updated Inventory contents",
                () -> assertEquals(updatedInventory.getId(), fetchedUpdatedInventory.getId()),
                () -> assertEquals(1, fetchedUpdatedInventory.getIngredients().size()),
                () -> assertEquals("Coffee", fetchedUpdatedInventory.getIngredients().get(0).getName()),
                () -> assertEquals(50, fetchedUpdatedInventory.getIngredients().get(0).getAmount()));
        inventoryRepository.deleteById(savedId);
    }

}
