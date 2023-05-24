# Online Shopping Application with EJBs and Microservices

## Architecture
The application follows a microservices architecture with the following components:

1. Account and User Microservice - Runs on port 16957 and handles account and user-related functionalities.
2. Product and Order Microservice - Runs on port 9314 and manages product-related operations and order processing.
3. Shipping Microservice - Runs on port 8080 and handles shipping requests and notifications.
4. Shopping App (Frontend) - Developed using Angular and runs on port 4200. It provides the user interface for the application.

## Bean Types Used
The following bean types are used in the application:

1. Stateless Beans - Used for every other service in the application.
2. Stateful Beans - Used for user session management.
3. Singleton Beans - Used for the purchase service to create orders.
4. Message Driven Beans - Used for shipping notifications to accept or reject shipping requests.

## Functional Requirements
**Admin Functionality:**

Admins in the system have the following abilities:

1. Creation of Product Selling Companies Representative Accounts:
   - Admins can create representative accounts for companies by providing a range of unique company names.
   - Passwords for each representative account are automatically generated.
2. Creation of Shipping Companies:
   - Admins can create shipping companies that cover specific geographic regions.
   - Customers can only request shipping from companies that cover their geographic location.
3. Listing of Customer Accounts:
    - Admins can view a list of customer accounts in the system.
4. Listing of Shipping Companies:
   - Admins can view a list of shipping companies registered in the system.
5. Listing of Selling Companies Representative Accounts:
   - Admins can view a list of representative accounts for product selling companies.

**Shipping Company Functionality:**

As a shipping company, you have the following abilities:

1. Process Shipping Requests:
   - You can process shipping requests from customers.
   - The shipping company can only process requests if the customer falls within its supported geographic region(s).
   - Once a shipping request is processed, customers are notified.

**Customer Functionality:**

As a customer, you have the following abilities:

1. Register as a New Customer:
   - You can register as a new customer through the system. 
2. Login into the System:
   - You can log in using the credentials used during registration.
3. View Current and Past Purchase Orders:
   - You can view your current and past purchase orders.
4. Make New Purchase Orders:
   - You can place new purchase orders.
   - Special handling is implemented to avoid situations of server failure.
5. Confirmation of Orders Processing and Shipping:
   - Customers receive confirmation of their order processing and shipping details.


## Technologies Used
The application is built using the following technologies:

1. Jakarta EE (Enterprise Edition)
2. Angular
3. Microservices
4. EJBs (Enterprise JavaBeans)