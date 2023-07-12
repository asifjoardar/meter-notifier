# Meter Notifier

Meter Notifier is a Spring Boot project that provides meter reading notifications to customers. It utilizes Spring Boot and various dependencies to handle data persistence, email notifications, web interface, and testing.

## Features

- Data persistence with Spring Data JPA
- Email notifications using Spring Mail
- Web interface built with Thymeleaf
- Testing with JUnit and Spring Boot testing utilities

## Prerequisites

Before running the Meter Notifier project, make sure you have the following prerequisites installed:

- Java Development Kit (JDK) 17 or a compatible version
- PostgreSQL database (configured with appropriate credentials)

## Getting Started

Follow these steps to set up and run the Meter Notifier project:

1. Clone the repository:

   ```shell
   git clone https://github.com/your-username/meter-notifier.git
   ```

2. Configure the database:

    - Create a PostgreSQL database.
    - Update the database configurations in the `src/main/resources/application.properties` file according to your environment.

3. Build the project:

   ```shell
   cd meter-notifier
   mvn clean install
   ```

4. Run the project:

   ```shell
   mvn spring-boot:run
   ```

5. Access the Meter Notifier web interface:

   Open your web browser and navigate to `http://localhost:8080`.

## Configuration

The configuration file `src/main/resources/application.properties` contains various properties that can be modified to suit your needs. Update the database connection details, email server settings, and other properties as required.

## Testing

The Meter Notifier project includes unit tests implemented with JUnit and Spring Boot's testing utilities. You can run the tests using the following command:

```shell
mvn test
```

## Contributing

Contributions to the Meter Notifier project are welcome! If you find any issues or have suggestions for improvement, please open an issue or submit a pull request.

## License

The Meter Notifier project is licensed under the [MIT License](LICENSE).

## Acknowledgements

- This project was developed using [Spring Boot](https://spring.io/projects/spring-boot).
- It utilizes various open-source libraries and frameworks.

## Contact

For any inquiries or feedback, please contact with [me](mailto:mdasifjoardar@gmail.com).