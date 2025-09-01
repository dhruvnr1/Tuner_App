# Guitar Tuner Web Application

This is a web-based guitar tuner application with a Spring Boot backend and a React frontend.

## How to Run

### Prerequisites

- Java 21 or later
- Node.js 18 or later
- Maven 3.6 or later

### Development Mode

In development mode, you run the backend and frontend separately.

1.  **Run the Backend:**
    Open a terminal in the `backend` directory and run:
    ```sh
    ./mvnw spring-boot:run
    ```
    The backend will start on port 8080.

2.  **Run the Frontend:**
    Open another terminal in the `frontend` directory and run:
    ```sh
    npm install
    npm start
    ```
    The frontend development server will start on port 3000. Open your browser to [http://localhost:3000](http://localhost:3000) to see the application.

### Production Mode

In production mode, the application is packaged as a single JAR file.

1.  **Build the Frontend:**
    In the `frontend` directory, run:
    ```sh
    npm install
    npm run build
    ```

2.  **Build the Backend:**
    In the `backend` directory, run:
    ```sh
    ./mvnw package
    ```
    This will create a JAR file in the `backend/target` directory.

3.  **Run the Application:**
    Run the application from the JAR file:
    ```sh
    java -jar backend/target/tuner-0.0.1-SNAPSHOT.jar
    ```
    The application will be available at [http://localhost:8080](http://localhost:8080).
