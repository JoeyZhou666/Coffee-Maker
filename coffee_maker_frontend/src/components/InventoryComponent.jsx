import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getInventory, updateInventory } from '../services/inventoryService'; 

const InventoryComponent = () => {
    const [coffee, setCoffee] = useState("");
    const [milk, setMilk] = useState("");
    const [whippedCream, setWhippedCream] = useState(""); 
    const [currentInventory, setCurrentInventory] = useState([]);

    const navigate = useNavigate();

    // Fetch the inventory when the component mounts
    useEffect(() => {
        fetchInventory();
    }, []);

    const fetchInventory = async () => {
        try {
            const response = await getInventory(); // Use imported getInventory function
            setCurrentInventory(response.data.ingredients);

            
            const coffeeItem = response.data.ingredients.find(item => item.name.toLowerCase() === 'coffee');
            const milkItem = response.data.ingredients.find(item => item.name.toLowerCase() === 'milk');
            const whippedCreamItem = response.data.ingredients.find(item => item.name.toLowerCase() === 'whipped cream');

            setCoffee(coffeeItem ? coffeeItem.amount : "");
            setMilk(milkItem ? milkItem.amount : "");
            setWhippedCream(whippedCreamItem ? whippedCreamItem.amount : "");
        } catch (error) {
            console.error("Error fetching inventory:", error);
            alert("Failed to load current inventory.");
        }
    };

    const handleUpdateInventory = async (event) => {
        event.preventDefault();

        // Input validation: check for positive integers
        if (!isValidAmount(coffee) || !isValidAmount(milk) || !isValidAmount(whippedCream)) {
            alert("Please enter positive integer values for all ingredients.");
            return;
        }

        // Prepare inventory data for update
        const inventoryData = {
            id: 1, // Assuming the inventory ID is 1
            ingredients: [
                { name: "Coffee", amount: parseInt(coffee, 10) },
                { name: "Milk", amount: parseInt(milk, 10) },
                { name: "Whipped Cream", amount: parseInt(whippedCream, 10) }
            ]
        };

        try {
            const response = await updateInventory(inventoryData); // Use imported updateInventory function

            if (response.status === 200) {
                alert("Inventory updated successfully!");
                fetchInventory(); // Refresh the inventory data
            } else {
                alert("Failed to update inventory.");
            }
        } catch (error) {
            console.error("Error updating inventory:", error);
            alert("An error occurred while updating the inventory.");
        }
    };

    const isValidAmount = (value) => {
        const number = parseInt(value, 10);
        return Number.isInteger(number) && number > 0;
    };

    return (
        <div className="container">
            <br /><br />
            <div className="row">
                <div className="card col-md-6 offset-md-3">
                    <h2 className="text-center">Inventory</h2>

                    <div className="current-inventory mb-3">
                        <h4>Current Inventory</h4>
                        <ul>
                            {currentInventory.map((ingredient, index) => (
                                <li key={index}>
                                    {ingredient.name}: {ingredient.amount}
                                </li>
                            ))}
                        </ul>
                    </div>

                    <div className="card-body">
                        <form onSubmit={handleUpdateInventory}>
                            <div className="form-group mb-2">
                                <label className="form-label">Amount Coffee</label>
                                <input
                                    type="text"
                                    placeholder="Enter Amount Coffee"
                                    value={coffee}
                                    onChange={(e) => setCoffee(e.target.value)}
                                    className="form-control"
                                />
                            </div>

                            <div className="form-group mb-2">
                                <label className="form-label">Amount Milk</label>
                                <input
                                    type="text"
                                    placeholder="Enter Amount Milk"
                                    value={milk}
                                    onChange={(e) => setMilk(e.target.value)}
                                    className="form-control"
                                />
                            </div>

                            <div className="form-group mb-2">
                                <label className="form-label">Amount Whipped Cream</label>
                                <input
                                    type="text"
                                    placeholder="Enter Amount Whipped Cream"
                                    value={whippedCream}
                                    onChange={(e) => setWhippedCream(e.target.value)}
                                    className="form-control"
                                />
                            </div>

                            {/* Centered Buttons section */}
                            <div className="d-flex justify-content-center">
                                <button 
                                    className="btn btn-success me-2" 
                                    type="button" 
                                    onClick={() => navigate('/add-ingredient')}>
                                    Add Ingredient
                                </button>
                                <button className="btn btn-success" type="submit">Update Inventory</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default InventoryComponent;
