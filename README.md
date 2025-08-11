# Coffee Maker System

## Project Overview

Coffee Maker is a full-stack coffee recipe management system, consisting of a Spring Boot backend API and a React frontend application. The system allows users to:

* Manage coffee recipes (add, edit, delete)
* Manage inventory ingredients (coffee, milk, sugar, etc.)
* Make coffee and process payments
* View current inventory and recipe lists

## Tech Stack

### Backend

* **Framework**: Spring Boot 3.x
* **Database**: H2 (embedded) or MySQL (via JPA)
* **Persistence Layer**: Spring Data JPA
* **API Design**: RESTful
* **Build Tool**: Maven
* **Testing**: JUnit 5, Mockito

### Frontend

* **Framework**: React 18
* **State Management**: React Hooks
* **HTTP Client**: Axios
* **UI Library**: Bootstrap 5
* **Routing**: React Router

## System Architecture

### Backend Structure

```
src/main/java/edu/ncsu/csc326/coffee_maker/
├── controllers/        # Controller layer
├── dto/                # Data Transfer Objects
├── entity/             # Database entities
├── exception/          # Custom exceptions
├── mapper/             # Entity-DTO mappers
├── repositories/       # Data access layer
├── services/           # Business logic layer
└── CoffeeMakerApplication.java  # Application entry point
```

### Frontend Structure

```
src/
├── components/         # React components
├── services/           # API service calls
├── App.css
├── App.jsx
├── index.css
├── main.jsx
└── index.html
```

## Features

### Backend

1. **Recipe Management**

   * Create, read, update, delete coffee recipes
   * Recipes contain name, price, and a list of ingredients
   * Maximum of 3 recipes allowed
   * Ingredient validation supported

2. **Inventory Management**

   * View current ingredient inventory
   * Update ingredient quantities
   * Add new ingredients
   * Inventory validation

3. **Make Coffee**

   * Select a recipe and process payment
   * Calculate change
   * Check inventory availability
   * Validate payment amount

### Frontend

1. **Navigation Bar**

   * Inventory Management
   * Recipe Management
   * Make Coffee

2. **Inventory Page**

   * Display current inventory
   * Update ingredient quantities
   * Add new ingredients

3. **Recipe Page**

   * List all recipes
   * Add new recipe
   * Edit/delete existing recipes
   * View recipe details

4. **Make Coffee Page**

   * Select recipe
   * Input payment amount
   * Display change

## Installation & Running

### Backend

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/coffee-maker.git
   cd coffee-maker/coffee_maker
   ```

2. Configure database (optional):

   * Default uses H2 in-memory database
   * To use MySQL, modify `application.properties`

3. Run the application:

   ```bash
   mvn spring-boot:run
   ```

   * Backend will start at `http://localhost:8080`

### Frontend

1. Navigate to the frontend directory:

   ```bash
   cd ../coffee_maker_frontend
   ```

2. Install dependencies:

   ```bash
   npm install
   ```

3. Start the application:

   ```bash
   npm start
   ```

   * Frontend will start at `http://localhost:3000`

## API Documentation

### Main Endpoints

#### Recipe API (`/api/recipes`)

* `GET /` – Get all recipes
* `POST /` – Create new recipe
* `GET /{id}` – Get a recipe by ID
* `PUT /{id}` – Update a recipe
* `DELETE /{id}` – Delete a recipe

#### Inventory API (`/api/inventory`)

* `GET /` – Get current inventory
* `PUT /` – Update inventory
* `POST /add` – Add new ingredient

#### Make Coffee API (`/api/makerecipe`)

* `POST /{name}` – Make coffee with the specified recipe

#### Ingredient API (`/api/ingredients`)

* `GET /` – Get all ingredients
* `POST /` – Create new ingredient
* `DELETE /` – Delete all ingredients

## Database Design

### Main Entities

1. **Recipe**

   * `id`: Primary key
   * `name`: Recipe name
   * `price`: Price
   * `ingredients`: List of associated ingredients

2. **Ingredient**

   * `id`: Primary key
   * `name`: Ingredient name
   * `amount`: Quantity

3. **Inventory**

   * `id`: Primary key
   * `ingredients`: List of inventory ingredients

## Testing

### Backend Testing

Includes comprehensive unit and integration tests covering all main functionalities:

1. **Controller Tests**

   * `RecipeControllerTest` – Recipe management
   * `InventoryControllerTest` – Inventory management
   * `IngredientControllerTest` – Ingredient management

2. **Service Tests**

   * `RecipeServiceTest` – Recipe business logic
   * `InventoryServiceTest` – Inventory business logic
   * `IngredientServiceTest` – Ingredient business logic

3. **Repository Tests**

   * `RecipeRepositoryTest` – Recipe data access
   * `InventoryRepositoryTest` – Inventory data access
   * `IngredientRepositoryTest` – Ingredient data access

Run backend tests:

```bash
mvn test
```

### Frontend Testing

* Uses Jest and React Testing Library
* Run frontend tests:

  ```bash
  npm test
  ```

## Development Guide

### Code Style

* Follow Java/Spring and React best practices
* Consistent naming conventions
* Clear comments for each method

### Contribution Process

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit changes (`git commit -m 'Add some feature'`)
4. Push to branch (`git push origin feature/your-feature`)
5. Create a Pull Request

## Known Issues & Future Improvements

### Known Issues

* Frontend form validation could be more robust
* Error handling during coffee making could be more user-friendly

### Future Improvements

* Add user authentication system
* Support more payment methods
* Add recipe image upload
* Implement responsive design for mobile devices

## License

MIT License – see the LICENSE file in the repository.

---

**Project Status**: Completed
**Last Updated**: November 2024
