Project Description

Hello, for this project, you need to use Postman, a Java-compatible IDE, and Java 17.

Postman collections will be provided in the attachment. Below is detailed information about the project's capacity and usage:

This application is developed for Stock Broker operations.

Application Features:

Adding Employees: Employees can deposit and withdraw money from customers' accounts.
Stock (Share) Trading: Employees can buy and sell stocks on behalf of customers.
Stock Data: Stock information is fetched from the Alpha Vantage API. The application includes 10 default accounts, which contain stocks from major companies like APPL, TSLA, COST, NVDA, IBM, MSFT, etc. This data can be updated via the service, and the last update timestamps are stored in the database.
API Limit: The API key has a daily limit of 25 requests.
Important Notes:

Employee Passwords: Employee passwords are stored in Base64 format in the database. Therefore, you cannot directly read passwords from the H2 database. To view passwords, you can use the getEmployeeDecoded service.
JWT Token: When employees log in, a JWT token is generated. This token remains valid throughout the login session and must be used for authorization-required methods like withdraw, deposit, and matchOrder. Each customer has a unique employeeId, and employees can only perform operations on their own customers' accounts.
Logging: The application uses logUtil to write logs to a .txt file.
Limited Time and Missing Features:

I intended to add a language selection feature, but due to time constraints, it was not added.
Unit tests could not be completed because the first week of every month is a transition week for me, and my workload is heavy.
I was in the process of writing a Global Error Handler, but it was not finished.
However, the application currently works correctly for deposit, withdraw, buyStock, sellStock, matchOrder, and the necessary data displays.
