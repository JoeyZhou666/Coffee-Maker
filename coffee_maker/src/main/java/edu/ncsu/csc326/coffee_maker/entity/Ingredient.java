package edu.ncsu.csc326.coffee_maker.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * represents an ingredient. Is stored in the database
 * @author William Walton
 */
@Entity
@Table(name = "ingredients")
public class Ingredient {

    /** the id of where the ingredient is stored in the table */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** the name of the ingredient */
    private String name;

    /** the amount of the ingredient, doesn't make much sense, but is only referenced in terms of inventory and recipe, which both need an amount */
    private Integer amount;

    // Constructors, getters, and setters
    /**
     * constructs a new ingredient
     */
    public Ingredient() {
        // does nothing
    }

    /**
     * constructor with name and amount
     * @param name the name of the ingredient
     * @param amount the initial amount of the ingredient
     */
    public Ingredient(final String name, final Integer amount) {
        this.name = name;
        this.amount = amount;
    }

    // Getters and Setters
    /**
     * getter method for id
     * @return the id of the ingredient
     */
    public Long getId() {
        return id;
    }

    /**
     * setter method for id
     * @param id the id of the ingredient to set
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * getter method for name
     * @return the name of the ingredient
     */
    public String getName() {
        return name;
    }

    /**
     * setter method for name
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * gets the amount of the ingredient
     * @return the amount of the ingredient
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * setter method for amount
     * @param amount the amount of the ingredient
     */
    public void setAmount(final Integer amount) {
        this.amount = amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Ingredient other = (Ingredient) obj;
        return Objects.equals(name, other.name);
    }


}
