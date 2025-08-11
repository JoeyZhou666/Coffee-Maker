import React, { useEffect, useState } from 'react'
import { listRecipes, deleteRecipe } from '../services/RecipesService'
import { useNavigate } from 'react-router-dom'
import { getInventory } from '../services/InventoryService'

/** Lists all the recipes and provide the option to create a new recipe
 * and delete an existing recipe.
 */
const ListRecipesComponent = () => {

    const [recipes, setRecipes] = useState([])
    const [inventory, setInventory] = useState([])

    const navigator = useNavigate();

    useEffect(() => {
        getAllRecipes()
    }, [])

    function getAllRecipes() {
        listRecipes().then((response) => {
            setRecipes(response.data)
            getInventory().then((response) => {
              setInventory(response.data.ingredients)
            }).catch(error => {
              console.error(error)
            })
        }).catch(error => {
            console.error(error)
        })
    }

    function addNewRecipe() {
        navigator('/add-recipe')
    }

	function editRecipe(name) {
			console.log(name)
			navigator(`/edit-recipe-ws/${name}`)
	}
	
    function removeRecipe(id) {
        console.log(id)

        deleteRecipe(id).then((response) => {
            getAllRecipes()
        }).catch(error => {
            console.error(error)
        })
    }

    function getAmountIngredient(name, recipeIngredients) {
      for (let i = 0; i < recipeIngredients.length; i++) {
        if (name == recipeIngredients[i].name) {
          return recipeIngredients[i].amount;
        }
      }
      return 0;
    }

    return (
        <div className="container">
            <h2 className="text-center">List of Recipes</h2>
            <button className="btn btn-primary mb-2" onClick={ addNewRecipe }>Add Recipe</button>
            <table className="table table-striped table-bordered">
                <thead>
                    <tr>
                        <th>Recipe Name</th>
                        <th>Recipe Price</th>
                        {inventory.map(ingredient =>
                            <th key = {ingredient.id}>{ingredient.name}</th>
                        )}
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        recipes.map(recipe => 
                        <tr key={recipe.id}>
                            <td>{recipe.name}</td>
                            <td>{recipe.price}</td>
                            {inventory.map(ingredient =>
                                <td key = {ingredient.id}>{getAmountIngredient(ingredient.name, recipe.ingredients)}</td>
                            )}
                            <td>
								<button className='btn btn-info' onClick={() => editRecipe(recipe.name)}>Edit</button>
                                <button className="btn btn-danger" onClick={() => removeRecipe(recipe.id)}
                                    style={{marginLeft: '10px'}}
                                >Delete</button>
                            </td>
                        </tr>)
                    }
                </tbody>
            </table>
        </div>
    )

}

export default ListRecipesComponent