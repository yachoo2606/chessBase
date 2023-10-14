# ChessBase Backend

This repository contains the backend application built using Spring Boot for managing chess-related data including chess clubs, games, and titles.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup and Installation](#setup-and-installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)

## Introduction

This project is a backend application for a chess base system that stores information about chess clubs, games, and titles. It provides RESTful APIs to manage and retrieve data related to chess entities.

## Features

- CRUD operations for chess clubs, games, and titles.
- Authentication and authorization mechanisms for secure access to APIs.
- Persistent storage of data using a database.

## Technologies Used

- Spring Boot
- Spring Data JPA
- Postgres (or any preferred database)
- Spring Security
- Gradle (for dependency management)

## Setup and Installation

1. Clone the repository: `git clone https://github.com/yachoo2606/chessBase.git`
2. Navigate to the project directory: `cd chessBase`
3. Open the project in your favorite IDE (e.g., IntelliJ IDEA, Eclipse).
4. Configure the database in `application.yml`.
5. Run the application.

## Usage

1. Ensure the backend server is running.
2. Use the provided API endpoints (see [API Endpoints](#api-endpoints)) to interact with the system.

## API Endpoints

The API documentation can be found in the [API documentation file](API_DOCUMENTATION.md). It contains detailed information about available API endpoints, request/response formats, and authentication requirements.

## Contributing

We welcome contributions! Feel free to open issues or submit pull requests.

## License

This project is licensed under the [MIT License](LICENSE).
