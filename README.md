# PromoCodeManager

**PromoCodeManager** is a SpringBoot application that helps you manage discount codes (promo codes) for sales and promotions.

## **Prerequisites:**

* Java >= 19.0.1
* 100MB of storage space

## **Installation:**

1. **Download the application:**
    * Visit the Releases section on the project's main page and download the `.jar` file.
2. **Run the application:**
    * Navigate to the directory where you downloaded the `.jar` file.
    * Open your terminal and run the following command:

```bash
java -jar <file_name>.jar
```

**Replace <file_name> with the actual name of the downloaded .jar file.**

## REST API endpoints with sample queries

### **Context API Path:** `/api/v1`

* **Create new product (POST):** `/products`
    * Creates a new product
    * Requries specified `name` (String), `description` (String), `price` (Double), and `currency` (3 letter currency code String in ISO 4217 format) in the JSON request body.<br><br>

    ```bash
    curl --location 'http://localhost:8080/api/v1/products' \
    --header 'Content-Type: application/json' \
    --data '{
        "name" : "NikeShoes",
        "description" : "Clothes",
        "price" : 150.0,
        "currency" : "USD"
    }'
    ```
<br>

* **Get all products (GET):** `/products`
    * Retrieves a list of all available products.<br><br>

    ```bash
    curl --location 'http://localhost:8080/api/v1/products'
    ```
<br>

* **Get product discount price (GET):** `/products/discount-price`
    * Calculates the discounted price of a product based on a provided promo code.
    * Requires query parameters `productName` and `promoCodeText`.<br><br>

    ```bash
    curl --location 'http://localhost:8080/api/v1/products/discount-price?productName=NikeShoes&promoCodeText=SUMMER2024'
    ```
<br>

* **Update product data (PUT):** `/products`
    * Updates existing product data with new `name`, `description`, `price`, and `currency`.
    * Requires `name` of the existing product as a query parameter.
    * New product information (`name`, `description`, `price`, and `currency`) in the JSON request body are optional.<br><br>

    ```bash
    curl --location --request PUT 'http://localhost:8080/api/v1/products?name=NikeShoes' \
    --header 'Content-Type: application/json' \
    --data '{
        "description" : "Shoes"
    }'
    ```
<br>

* **Create new fixed-amount promo code (POST):** `/promo-codes/fixed-amount`
    * Creates a new promo code with a fixed discount amount.
    * Requires: `text` (String), `expiration date` (`YYYY-MM-DD` formatted String), `allowed usages` (Integer), `discount amount` (Double), and `currency` (3 letter currency code String in ISO 4217 format) in the JSON request body.<br><br>

    ```bash
    curl --location 'http://localhost:8080/api/v1/promo-codes/fixed-amount' \
    --header 'Content-Type: application/json' \
    --data '{
        "text" : "SUMMER2024",
        "expirationDate" : "2024-08-14",
        "usagesAllowed" : "5",
        "discountAmount" : 100,
        "discountCurrency" : "USD"
    }'
    ```
<br>

* **Create new percentage promo code (POST):** `/promo-codes/percentage`
    * Creates a new promo code with a percentage discount.
    * Requires: `text` (String), `expiration date` (`YYYY-MM-DD` formatted String), `allowed usages` (Integer), and `discount percentage` (Integer) in the JSON request body.<br><br>

    ```bash
    curl --location 'http://localhost:8080/api/v1/promo-codes/percentage' \
    --header 'Content-Type: application/json' \
    --data '{
        "text" : "EXTRA2024",
        "expirationDate" : "2024-08-14",
        "usagesAllowed" : "1",
        "discountPercentage" : 65
    }'
    ```
<br>

* **Get all promo codes (GET):** `/promo-codes`
    * Retrieves a list of all available promo codes.<br><br>

    ```bash
    curl --location 'http://localhost:8080/api/v1/promo-codes'
    ```

<br>

* **Get promo code details (GET):** `/promo-codes`
    * Retrieves details of a specific promo code.
    * Requires query parameter `text` (String).<br><br>      

    ```bash
    curl --location 'http://localhost:8080/api/v1/promo-codes?text=SUMMER2024'
    ```
<br>

* **Simulate purchase (POST):** `/purchases`
    * Simulates a purchase for a product with an optional promo code applied.
    * Requires query parameters `productName` (String) and `promoCodeText` (String) (optional).<br><br>

    ```bash
    curl --location --request POST 'http://localhost:8080/api/v1/purchases?productName=NikeShoes&promoCodeText=SUMMER2024'
    ```
