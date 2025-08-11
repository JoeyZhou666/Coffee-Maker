import './App.css'
import {BrowserRouter, Routes, Route} from 'react-router-dom'
import ListRecipesComponent from './components/ListRecipesComponent'
import HeaderComponent from './components/HeaderComponent'
import FooterComponent from './components/FooterComponent'
import RecipeComponent from './components/RecipeComponent'
import InventoryComponent from './components/InventoryComponent'
import MakeRecipeComponent from './components/MakeRecipeComponent'
import AddIngredientComponent from './components/AddIngredientComponent'
import EditRecipeComponent from './components/EditRecipeComponent'

/**
 * Top level of the App.  Any new paths and the corresponding Components
 * are listed here. 
 */
function App() {
    return (
    <>
    <BrowserRouter>
      <HeaderComponent />
      <Routes>
        <Route path='/' element = { <ListRecipesComponent /> }></Route>
        <Route path='/recipes' element = { <ListRecipesComponent /> }></Route>
        <Route path='/add-recipe' element = { <RecipeComponent /> }></Route>
        <Route path='/inventory' element = { <InventoryComponent /> }></Route>
        <Route path='/make-recipe' element = { <MakeRecipeComponent /> }></Route>
        <Route path='/add-ingredient' element = { <AddIngredientComponent /> }></Route>
		<Route path='/edit-recipe-ws/:incomingName' element = { <EditRecipeComponent /> }></Route>
      </Routes>
      <FooterComponent />
    </BrowserRouter>
    </>
  )
}

export default App