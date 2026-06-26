# LCW Selenium Test Automation

Java, Selenium WebDriver and JUnit 5 based end-to-end test automation project for LCW e-commerce shopping cart flow.

## Project Context

This project was developed as a Selenium test automation case study during my software testing internship at Albaraka Teknoloji.

It automates an end-to-end shopping cart scenario on the public LCW website for learning and practice purposes. The project is not affiliated with LCW and does not include any internal company code, credentials, or private test environment data.

## Tech Stack

- Java
- Selenium WebDriver
- JUnit 5
- Maven
- Page Object Model
- JSON-based locator management

## Test Scenario

- Open LCW home page
- Close cookie popup if present
- Search for a product
- Load more products
- Select a random product
- Select available color / size / length combination
- Add product to cart
- Compare product detail price and cart price
- Validate product price + shipping fee equals cart total
- Increase product quantity
- Validate updated cart total
- Delete product from cart
- Verify cart is empty
