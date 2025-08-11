import { useState } from 'react'
import { useParams } from 'react-router-dom'
import React, { useEffect } from 'react';
import { getRecipe, updateRecipe } from '../services/RecipesService';
import { getInventory } from '../services/InventoryService';
import { useNavigate } from 'react-router-dom'



const EditRecipeComponent = () => {
	
  const [id, setId] = useState(1)
	const [name, setName] = useState("")
	const [price, setPrice] = useState("")
	const { incomingName } = useParams()
	const [selectedIngredient, setSelectedIngredient] = useState(''); 
  const [ingredients, setIngredients] = useState([]);
  const [ingredientAmounts, setIngredientAmounts] = useState([]);
  const [editAmountDisp, setEditAmountDisp] = useState([]);
  const [addIngredientDisp, setAddIngredientDisp] = useState([]);
  const [outOfIngredients, setOutOfIngredients] = useState(true);
  const navigator = useNavigate();

  const [errors, setErrors] = useState({
    general: "",
    price: "",
    ingredients: ""
  })

  // 
  const [ingredientErrors, setIngredientErrors] = useState([])
	
  // setting up the state
	useEffect(() => {
		// console.log(incomingName) 
		if(incomingName) {
			getRecipe(incomingName).then((response) => {
				// console.log("here")
				// console.log(response.data)
        setId(response.data.id)
				setName(response.data.name)
				setPrice(response.data.price)
        setIngredients((ingredients) => []);
        setIngredientAmounts((ingredientAmounts) => []);
        const errorsCopy = {
          general: "",
          price: "",
          ingredients: ""
        };
        setIngredientErrors(ingredientErrors => []);
        for (let i = 0; i < response.data.ingredients.length; i++) {
          // console.log("in")
          setIngredients((ingredients) => [...ingredients, response.data.ingredients[i].name]);
          ingredients[i] = response.data.ingredients[i].name;
          // console.log(response.data.ingredients[i].amount);
          setIngredientAmounts((ingredientAmounts) => [
            ...ingredientAmounts,
            response.data.ingredients[i].amount.toString()
          ]);
          ingredientAmounts[i] = response.data.ingredients[i].amount.toString();
          setIngredientErrors((ingredientErrors) => [
            ...ingredientErrors,
            ""
          ]);
          ingredientErrors[i] = "";

          // console.log(ingredients[i]);
          // console.log(ingredients);
          // console.log(ingredientAmounts);
        } 
        setErrors(errorsCopy)
        resetTextFields();
        resetDropList();
			}).catch(error => {
				console.error(error)
        navigator("/recipes")
			})
		} else {
			console.log("No name detected")
		}
	}, [incomingName])

  // adds a new ingredient into the recipe
	function addNewIngredient() {
    const name = selectedIngredient;
    if (outOfIngredients || ingredients.includes(selectedIngredient)) {
      //console.log(outOfIngredients);
      return;
    }

    // console.log("name updated:", name);
    // console.log("ingredients updated:", ingredients);
    // console.log("amounts updated:", ingredientAmounts);
    length = ingredients.length;
    //setIngredients((ingredients) => [...ingredients, name]);
    ingredients[length] = name;
    //setIngredientAmounts((ingredientAmounts) => [...ingredientAmounts, 0]);
    ingredientAmounts[length] = 0;

    //console.log(errors)
    const ingredientErrorsCopy = [...ingredientErrors];
    ingredientErrorsCopy.push("");
    setIngredientErrors(ingredientErrorsCopy);
    resetDropList();
    
	}

  // resets the drop list that displays ingredients that can be added
  function resetDropList() {
    getInventory().then((response) => {
      let inventoryIngredients = response.data.ingredients;
      let ingredientsNotInRecipe = []
      for (let i = 0; i < inventoryIngredients.length; i++) {
        if (!ingredients.includes(inventoryIngredients[i].name)) {
          ingredientsNotInRecipe.push(inventoryIngredients[i].name);
        }
      }

      
      setAddIngredientDisp((addIngredientDisp) => []);
      addIngredientDisp.splice(0, addIngredientDisp.length);
      
      
      
      for (let i = 0; i < ingredientsNotInRecipe.length; i++) {
        const item = (<option key={ingredientsNotInRecipe[i]} value = {ingredientsNotInRecipe[i]}>{ingredientsNotInRecipe[i]}</option>);
        setAddIngredientDisp((addIngredientDisp) => [...addIngredientDisp, item]);
        addIngredientDisp[i] = item;
      }

      if (ingredientsNotInRecipe.length == 0) {
        setOutOfIngredients(true);
      } else {
        setOutOfIngredients(false);
        setSelectedIngredient(ingredientsNotInRecipe[0]);
        //console.log("default ingredient, ", selectedIngredient, ingredientsNotInRecipe);
      }
      
      resetTextFields();
      
    }).catch(error => {
      console.error(error)
    })

  }


  // gets a block to allow editing of ingredeints in recipe
  function dispAmountBlock(index) {
    return (
      <div key={index} className="form-group mb-2">
      <label className="form-label">{"Amount " + ingredients[index]}</label>
      <button className="btn btn-danger" onClick={() => deleteIngredient(index)}
                                    style={{marginLeft: '10px'}}
                                >Remove Ingredient</button>
      <input
          type="text"
          placeholder={"Enter Amount " + ingredients[index]}
          value={ingredientAmounts[index]}
          onChange={(e) => updateState(e.target.value, index)}
          className={`form-control ${ingredientErrors[index] ? "is-invalid":""}`}
      >
      </input>
      {ingredientErrors[index] && <div className="invalid-feedback">{ingredientErrors[index]}</div>}
  </div>)
  }

  // updates state of text boxes for ingredient amount
  function updateState(amount, index) {
    ingredientAmounts[index] = amount;
    setIngredientAmounts(ingredientAmounts);
    resetTextFields();
  }

  // deletes an ingredient from the recipe
  function deleteIngredient(index) {
    setEditAmountDisp(editAmountDisp => []);
    editAmountDisp.splice(0, editAmountDisp.length);

    const newIngredients = [];
    const newIngredientAmounts = []
    const newIngredientErrors = []
    for (let i = 0; i < ingredients.length; i++) {
      if (i < index) {
        newIngredients[i] = ingredients[i];
        newIngredientAmounts[i] = ingredientAmounts[i];
        newIngredientErrors[i] = ingredientErrors[i];
      } else if ( i > index) {
        newIngredients[i - 1] = ingredients[i];
        newIngredientAmounts[i - 1] = ingredientAmounts[i];
        newIngredientErrors[i - 1] = ingredientErrors[i]
      }
    }
    setIngredients(ingredients => newIngredients);
    ingredients.splice(index, 1)
    setIngredientAmounts(ingredientAmounts => newIngredientAmounts);
    ingredientAmounts.splice(index, 1)
    //setIngredientAmounts(ingredientAmounts);

    setIngredientErrors(newIngredientErrors)
    ingredientErrors.splice(index, 1)

    resetDropList();

  }


  // re-displays the parts for ingredients in the recipe
  function resetTextFields() {
    // console.log("enter", ingredients);
    setEditAmountDisp((editAmountDisp) => []);
    //editAmountDisp.splice(0, editAmountDisp.length);
    let newDisp = []

    for (let i = 0; i < ingredients.length; i++) {
      newDisp[i] = dispAmountBlock(i);
      editAmountDisp[i] = newDisp[i];
      /*
      setEditAmountDisp((editAmountDisp) =>
      [...editAmountDisp, <div className="form-group mb-2">
      <label className="form-label">{"Amount " + ingredients[i]}</label>
      <input
          type="text"
          className="form-control"
          placeholder={"Enter Amount " + ingredients[i]}
          value={ingredientAmounts[i]}
          onChange={(e) => updateState(e.target.value, i)}
      />
      
  </div>]);
      */
    }
    
    setEditAmountDisp((editAmountDisp) => newDisp);

  }

  // save the edit to the recipe
  function saveRecipe(e) {
    // console.log("Started")
    e.preventDefault()
    
    if (validateForm()) {
      const fullIngredients = [];
      for (let i = 0; i < ingredients.length; i++) {
        fullIngredients[i] = {name: ingredients[i], amount: ingredientAmounts[i]};
      }
      const recipe = {
        name: name,
        price: price,
        ingredients: fullIngredients
      }
      // console.log("close")
      
      updateRecipe(id, recipe).then((response) => {
        navigator("/recipes")
      }).catch(error => {
        console.error(error);
        const errorsCopy = {...errors}
        if (error.response.status == 410) {
          errorsCopy.general = "Recipe was deleted"
        }
        if (error.response.status == 409) {
          errorsCopy.general = "Recipe name was modified by somebody else and another recipe now has it. Navigate to another page."
        }
        setErrors(errorsCopy)
        resetTextFields();
      })
      
    } else {
      resetTextFields();
    }
    // console.log("errors after save", errors)
  }

  //check that the values are valid
  function validateForm() {
    let valid = true;
    const errorsCopy = {...errors};

    errorsCopy.general = "";

    if (isNaN(price) || price <= 0) {
      errorsCopy.price = "Price must be a positive integer."
      valid = false;
    } else {
      errorsCopy.price = "";
    }

    if (ingredients.length <= 0) {
      errorsCopy.ingredients = "Must have at least one ingredient."
      valid = false;
    } else {
      errorsCopy.ingredients = ""
    }

    setErrors(errorsCopy)
    setIngredientErrors(ingredientErrors => []);
    const newErrors = []
    for (let i = 0; i < ingredients.length; i++) {
      // console.log("ingredients", ingredients[i], ingredientAmounts)
      // console.log("status", isNaN(ingredientAmounts[i]), ingredientAmounts[i] <= 0);
      if (isNaN(ingredientAmounts[i]) || ingredientAmounts[i] <= 0) {
        newErrors[i] = ingredients[i] + " amount must be a positive integer."
        valid = false;
        // console.log("error occurred");
      } else {
        newErrors[i] = ""
      }
      ingredientErrors[i] = newErrors[i]
    }

    setIngredientErrors(ingredientErrors => newErrors)
    
    //errors.price = errorsCopy.price;

    return valid;
    

  }

  // get the general type of errors
  function getGeneralErrors() {
    if (errors.general) {
        return <div className="p-3 mb-2 bg-danger text-white">{errors.general}</div>
    }
  } 

  // console.log("id", id);
  // console.log("errors", errors, ingredientErrors);
  // console.log("ingredients", ingredients);
  //resetTextFields();
	return (
	        <div className='container'>
	            <div className='row'>
	                <div className='card col-md-6 offset-md-3 offset-md-3'>
	                    <h2 className="text-center">Edit Recipe {incomingName}</h2>
	                    <div className="card-body">
                          { getGeneralErrors() }
	                        <form>
	                            <div className="form-group mb-2">
	                                <label className="form-label">Recipe Price</label>
	                                <input
	                                    type="text"
	                                    placeholder="Enter Recipe Price"
	                                    value={price}
	                                    onChange={(e) => setPrice(e.target.value)}
                                      className={`form-control ${errors.price ? "is-invalid":""}`}
	                                />
                                  {errors.price && <div className="invalid-feedback">{errors.price}</div>}
	                            </div>
                              {editAmountDisp}
	                            <div className="form-group mb-2">
	                                <label className="form-label">Select Ingredient to Add</label>
	                                <select
	                                    value={selectedIngredient}
	                                    onChange={(e) => setSelectedIngredient(e.target.value)}
                                      className={`form-control ${errors.ingredients ? "is-invalid":""}`}
	                                >
	                                    {addIngredientDisp}
	                                </select>
                                  {errors.ingredients && <div className="invalid-feedback">{errors.ingredients}</div>}
	                            </div>
                              <div className="form-group mb-2">
                                <button
                                    type="button"
                                    className="btn btn-success"
                                    onClick={addNewIngredient}
                                >
                                    Add Ingredient
                                </button>
                              </div>
	                            <button
	                                type="button"
	                                className="btn btn-success"
	                                onClick={(e) => saveRecipe(e)}
	                            >
	                                Submit
	                            </button>
	                        </form>
	                    </div>
	                </div>
	            </div>  
	        </div>
	    );
	};

export default EditRecipeComponent