# FinGaze Backend

The **FinGaze Backend** is a Spring Boot application for managing stock portfolios. It provides endpoints for stock CRUD operations, calculates portfolio metrics, and integrates with external APIs for real-time data.

## Service Integration

This backend communicates with:
- **StockDataAPI (Flask)**
  * Fetches 90-day historical stock data
  * Powers portfolio chart calculations  
  * Repository: [StockDataAPI](https://github.com/Preterno/StockDataAPI)
- **FinGaze Frontend**  
  * Provides the user interface
  * Repository: [FinGaze Frontend](https://github.com/Preterno/FinGaze)

## Features

- **Stock Management**: Add, update, delete, and view stock holdings
- **Top Performers**: Fetch the top 5 stocks based on percentage growth
- **Portfolio Growth Chart**: Fetch data to visualize portfolio growth over different time periods (7 days, 1 month, 3 months)

## Technologies and Libraries

- **Spring Boot**: Framework for building RESTful services
- **MySQL**: Database for storing stock holdings
- **Flask API**: Used to fetch 90-day stock data for portfolio chart calculations
- **Finnhub API**: For fetching real-time stock details
- **Maven**: Build and dependency management tool

## Setup and Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Preterno/FinGaze-Backend.git
   cd FinGaze-Backend
   ```

2. **Set Up Environment Variables**:  
   Create a `.env` file in the root directory with:
   ```plaintext
   FINNHUB_API_KEY=your_finnhub_api_key
   FLASK_API_BASE_URL=your_flask_url
   FLASK_API_KEY=your_api_key
   ```

3. **Build the Application**:
   ```bash
   mvn clean install
   ```

4. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

### Stock Management
- `GET /api/stocks/list` – View all stocks
- `POST /api/stocks/add` – Add a stock
- `PUT /api/stocks/update` – Update stock details
- `DELETE /api/stocks/{id}` – Delete a stock

### Top Performers
- `GET /api/stocks/top-performers` – Fetch top-performing stocks

### Portfolio Growth
- `GET /api/stocks/chart` – Fetch portfolio growth data

## Connect with Me

Feel free to connect with me on [LinkedIn](https://www.linkedin.com/in/aslam8483).
