# Meter Notifier

Meter Notifier is a web application built with Spring Boot and Thymeleaf, designed to help users manage their energy consumption efficiently. With Meter Notifier, users can view real-time meter details, set personalized alerts, and monitor their energy usage effectively.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Technologies](#technologies)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Introduction

Meter Notifier is a feature-rich web application that empowers users to make informed decisions about their energy consumption. The project leverages Spring Boot for backend development and Thymeleaf for server-side templating, providing a seamless user experience.

## Features

- Real-time meter details and consumption metrics
- Personalized email alerts when meter balance is low
- Multi-meter management from a single user account
- Insights and trends to optimize energy consumption
- User-friendly interface with intuitive navigation

## Technologies

- Java 17
- Spring Boot 2.x
- Thymeleaf 3.x
- PostgreSQL (or any other supported database)
- Spring Data JPA for database interactions
- Spring Mail for email notifications
- Lombok for boilerplate code reduction
- Spring Boot Test for unit and integration testing


## Getting Started

Follow these steps to set up and run the Meter Notifier project on your local machine:

1. **Prerequisites:**

   - Java 17 or above
   - PostgreSQL (or any other supported database)
   - Maven

2. **Clone the Repository:**

   ```shell
   git clone https://github.com/asifjoardar/meter-notifier.git
   cd meter-notifier
   ```

3. **Database Configuration:**

    - Create a new PostgreSQL database (or use an existing one).
    - Update the database connection properties in the `application.properties` file located in `src/main/resources`.

4. **Build and Run:**

   ```shell
   mvn clean install
   mvn spring-boot:run
   ```

5. Access the Meter Notifier web interface:

   Open your web browser and navigate to `http://localhost:8080`.

## Configuration

The configuration file `src/main/resources/application.properties` contains various properties that can be modified to suit your needs. Update the database connection details, email server settings, and other properties as required.

## Usage

1. **Sign Up or Sign In:**

    - Create a new user account or sign in using your existing credentials.

2. **Add Meters:**

    - Add and manage multiple meters under your user account.

3. **View Meter Details:**

    - Access real-time meter details and consumption metrics.

4. **Set Alerts:**

    - Define a threshold for meter balance, and the system will send you personalized email alerts when the balance is low.

## Contributing

Contributions to the Meter Notifier project are welcome! If you find any bugs or have ideas for new features, please submit an issue or a pull request.

## License

The Meter Notifier project is licensed under the [MIT License](LICENSE).

## Contact

For any inquiries or feedback, please contact with [me](mailto:mdasifjoardar@gmail.com).