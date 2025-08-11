import { useState } from 'react';
import axios from 'axios';

/** Base URL for the Inventory API */
const REST_API_BASE_URL = "http://localhost:8080/api/inventory";

/** Form to create a new ingredient. */
const AddIngredientComponent = () => {
    const [name, setName] = useState("");
    const [amount, setAmount] = useState("");

    // Function to handle form submission
    const handleSubmit = async (event) => {
        event.preventDefault(); // Prevent default form submission

        // Input validation
        if (name.trim() === "" || !isValidAmount(amount)) {
            alert("Please enter a valid ingredient name and a positive integer amount.");
            return;
        }

        const newIngredient = {
            name: name.trim(),
            amount: parseInt(amount, 10)
        };

        try {
            // Make a POST request to add the new ingredient
            const response = await axios.post(`${REST_API_BASE_URL}/add`, newIngredient);

            if (response.status === 200) {
                alert("Ingredient added successfully!");
                // Reset form fields
                setName("");
                setAmount("");
            } else {
                alert("Failed to add ingredient.");
            }
        } catch (error) {
            console.error("Error adding ingredient:", error);
            alert("An error occurred while adding the ingredient.");
        }
    };

    // Input validation for positive integer amount
    const isValidAmount = (value) => {
        const number = parseInt(value, 10);
        return Number.isInteger(number) && number > 0;
    };

    return (
        <div className="container">
            <br /><br />
            <div className="row">
                <div className="card col-md-6 offset-md-3">
                    <h2 className="text-center">Add Ingredient</h2>

                    <div className="card-body">
                        <form onSubmit={handleSubmit}>
                            <div className="form-group mb-2">
                                <label className="form-label">Ingredient Name</label>
                                <input 
                                    type="text"
                                    placeholder="Enter Ingredient Name"
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                    className="form-control"
                                />
                            </div>

                            <div className="form-group mb-2">
                                <label className="form-label">Initial Amount</label>
                                <input 
                                    type="text"
                                    placeholder="Enter Ingredient Amount"
                                    value={amount}
                                    onChange={(e) => setAmount(e.target.value)}
                                    className="form-control"
                                />
                            </div>

                            <button className="btn btn-success" type="submit">Submit</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AddIngredientComponent;
