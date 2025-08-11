package edu.ncsu.csc326.coffee_maker.repositories;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import jakarta.transaction.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class IngredientRepositoryTest {
	
    /** the repository to test */
	@Autowired
	private IngredientRepository ingredientRepository;
	
	/** a first ingredient to test */
	private Long ingredient1Id;
	
	/** a second ingredient to test */
	private Long ingredient2Id;

	@BeforeEach
	public void setUp() throws Exception {
		ingredientRepository.deleteAll();
		
		Ingredient ingredient1 = new Ingredient("Coffee", 5);
		Ingredient ingredient2 = new Ingredient("Pumpkin Spice", 10);
		
		ingredient1Id = ingredientRepository.save(ingredient1).getId();
		ingredient2Id = ingredientRepository.save(ingredient2).getId();
		
		System.out.println(ingredient1Id + " " + ingredient2Id);
	}

	@Test
	@Transactional
	public void testAddIngredients() {
		Ingredient i1 = ingredientRepository.findById(ingredient1Id).get();
		assertAll("Ingredient contents",
				() -> assertEquals(ingredient1Id, i1.getId()),
				() -> assertEquals("Coffee", i1.getName()),
				() -> assertEquals(5, i1.getAmount()));
		
		Ingredient i2 = ingredientRepository.findById(ingredient2Id).get();
		assertAll("Ingredient contents",
				() -> assertEquals(ingredient2Id, i2.getId()),
				() -> assertEquals("Pumpkin Spice", i2.getName()),
				() -> assertEquals(10, i2.getAmount()));
	}
	
	/**
	 * tests the Ingredient class
	 */
	@Test
	@Transactional
	public void testIngredient() {
	    Ingredient i1 = new Ingredient("Coffee", 5);
	    Ingredient i2 = new Ingredient("Coffee", 4);
	    Ingredient i3 = new Ingredient("Milk", 5);
	    
	    assertEquals(i1, i1);
	    assertEquals(i2, i2);
	    assertEquals(i3, i3);
	    assertEquals(i1, i2);
	    assertEquals(i2, i1);
	    
	    assertNotEquals(i1, i3);
	    assertNotEquals(i2, i3);
	    assertNotEquals(i3, i1);
	    assertNotEquals(i3, i2);
	    
	    assertNotEquals(i1, null);
	    assertNotEquals(i1, 8);
	    
        assertEquals(i1.hashCode(), i1.hashCode());
        assertEquals(i2.hashCode(), i2.hashCode());
        assertEquals(i3.hashCode(), i3.hashCode());
	    
        assertEquals(i1.hashCode(), i2.hashCode());
        assertEquals(i2.hashCode(), i1.hashCode());
        
        assertNotEquals(i1.hashCode(), i3.hashCode());
        assertNotEquals(i2.hashCode(), i3.hashCode());
        assertNotEquals(i3.hashCode(), i1.hashCode());
        assertNotEquals(i3.hashCode(), i2.hashCode());
	}

}