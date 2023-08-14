# Challenge 3: PostHistoryApi

The main goal of this project is to apply asynchronous programming to enhance the application's performance. The application is designed to save each state of a Post object and each post object contains a list of Comment objects and list ob History objects, generated in each state. The transitions between states are restricted, and implementing asynchronous logic for these transitions presents an interesting challenge. The project also utilizes the desing patterns STATE to control state transitions, ensuring they only move to allowed states. Moreover, the application leverages messaging with ActiveMQ to improve performance. Spring Security with CORS, CSRF, and JWT is also integrated, providing user registration, authentication, and authorization.

## Application Requirements:
- IntelliJ or Eclipse installed
- Java 17+ installed
- ActiveMQ Server

## Running the Project:

1. Clone the repository from the main branch or download the ZIP file and extract it to a directory on your computer.
2. Open the project using your preferred IDE.
3. Start the ActiveMQ server using one of the following methods:
    - Execute the `activemq.bat` file located in the directory `Compass-Uol-Challenge-3\apache-activemq-5.18.2\bin\win64`. If this doesn't work, try the `win32` directory or execute directly from the `bin` directory.
    - If Docker is installed on your machine, you can also run the ActiveMQ server using the provided Docker Compose file. Open a terminal in the directory `posthistoryapi/src/main/resources` and execute the command: `docker-compose up -d`. This will download the official ActiveMQ image and start the server in a container. To stop the server, use the command: `docker-compose down`.

4. Navigate to the directory `serverdiscovery/src/main/java/br/com/compasso/serverdiscovery` and run the Java file `ServerDiscoveryApplication.java`.
5. Similarly, navigate to the directory `serverdiscovery/src/main/java/br/com/compasso/posthistoryapi` and run the Java file `PostHistoryApiApplication.java`.

 ### ```Note: Do not confuse "posthistoryapi" with the "/posthistoryreactiveapi" directory, which contains WebFlux code that is not completed and not executable.```

6. After completing these steps, the application should be up and running.

## Making Requests:

Before making requests, you need to register and authenticate users.

1. Register a user by sending a POST request to the `/users` endpoint with the following JSON payload:
```json
{
	"username": "user",
	"password": "user",
	"role": "USER" 
}
```
The `role` field accepts either "USER" or "ADMIN".

2. Authenticate by sending a POST request to the `/auth/login` endpoint with the following JSON payload:
```json
{
	"username": "user",
	"password": "user"
}
```
The response will contain the user ID and a JWT token.

3. To perform authorized actions, use the JWT token in the Authorization header with the "Bearer" scheme. Also, include the X-XSRF-TOKEN header, which should match the XSRF-TOKEN cookie generated during authentication.

4. To log out, send a POST request to the `/auth/logout` endpoint, including the JWT token in the Authorization header and the X-XSRF-TOKEN header.

### Note: For testing efficiency and load, consider disabling Spring Security by following the suggestion in the README.

Follow these steps to ensure successful requests and authentication.

## Testing PostHistoryApi:

- To process new posts (with IDs between 1 and 100), send a POST request to the endpoint `/posts/{postId}` to generate a history record for that post in the database. Duplicate posts will not be processed.
- To reprocess existing posts, use a PUT request to the endpoint `/posts/{postId}` to generate new history records for the post with updated information. Only previously processed IDs will be accepted.
- To disable an existing post, send a DELETE request to the endpoint `/posts/{postId}` to generate a history record indicating the post's deactivation. Only previously processed IDs will be accepted.
- To retrieve all posts, including their histories and comments, send a GET request to the endpoint `/posts`.

## Disabling Security (For Testing Purposes)

For the purpose of testing efficiency and load, you may consider temporarily disabling Spring Security. Follow these steps:

1. Navigate to the directory `posthistoryapi/src/main/java/br/com/compasso/posthistoryapi/security/config`.
2. Comment out all the Java code in the file `WebSecurityConfig.java`.
3. Uncomment the code in the file `WebSecurityConfigDisable.java`.

By performing the above steps, any requests will be accepted without authentication. This allows you to test the flow and performance of the application more freely.

Remember to re-enable Spring Security before deploying the application to production environments.
