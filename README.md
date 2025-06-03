# User Information Service

## Overview

The **User Information Service** is a Django-based API designed to manage user data. The service supports basic CRUD operations (create and retrieve) for user information such as `user_id`, `first_name`, `last_name`, `email`, and `phone_number`. The system is also integrated with Apache Kafka to listen for updates to user data in real-time.

This README covers the architecture, implementation details, setup instructions, example usage, and licensing for the project.

---

## Table of Contents

1. [Overview](#overview)
2. [Architecture](#architecture)

   * [High-Level Architecture](#high-level-architecture)
   * [Components](#components)
3. [Theory & Implementation](#theory--implementation)

   * [Django Model: `UserInfo`](#django-model-userinfo)
   * [Serializer: `UserInfoSerializer`](#serializer-userinfoserializer)
   * [API Views: `UserView`](#api-views-userview)
   * [Kafka Consumer: `KafkaConsumerCommand`](#kafka-consumer-kafkaconsumercommand)
4. [Setup Instructions](#setup-instructions)
5. [Example Usage](#example-usage)
6. [License](#license)

---

## Architecture

### High-Level Architecture

1. **Django Models**: The project uses Django's ORM to define the `UserInfo` model, which stores information about users.
2. **REST API**: The application exposes a REST API using Django REST Framework (DRF) to create and retrieve user information.
3. **Kafka Integration**: A Kafka consumer listens to a Kafka topic (`user_service`) and updates or creates user information in the database based on the incoming messages.
4. **Docker**: The project uses Docker for containerization, allowing easy deployment and scalability.

### Components

1. **UserInfo Model**: This defines the structure of the user data and is used for interacting with the database.
2. **UserInfoSerializer**: A DRF serializer for validating and serializing `UserInfo` data to and from JSON format.
3. **UserView API**: The `UserView` class defines the endpoints for creating and retrieving user information via HTTP GET and POST requests.
4. **Kafka Consumer**: A Kafka consumer runs as a Django management command to listen to the Kafka topic and update the database with new or updated user data.

![](images/UML.png)

---

## Theory & Implementation

### Django Model: `UserInfo`

The `UserInfo` model defines the schema for user data in the database. It includes the following fields:

* `user_id`: A unique identifier (UUID) for each user, which serves as the primary key.
* `first_name`: The user's first name, which can be left blank.
* `last_name`: The user's last name, which can also be left blank.
* `email`: The user's email address, which can be blank.
* `phone_number`: The user's phone number, which can be left blank.

These fields represent the basic information associated with a user. The model allows for flexible data storage, with all fields being optional.

### Serializer: `UserInfoSerializer`

The `UserInfoSerializer` is a class that serializes and deserializes `UserInfo` model data to and from JSON format. It validates the incoming data when creating or updating a user and ensures that the data adheres to the expected format.

By using Django REST Framework’s `ModelSerializer`, the `UserInfoSerializer` automatically maps the model fields to their corresponding JSON representation. This makes it easy to work with user data in a REST API context.

### API Views: `UserView`

The `UserView` class defines two main API endpoints:

1. **GET /user**: This endpoint allows clients to retrieve user information by `user_id`. If the user exists in the database, it returns the user’s data; otherwise, it returns a 404 Not Found error.
2. **POST /user**: This endpoint allows clients to create a new user or update an existing user based on the data sent in the request body. The system automatically validates and saves the data to the database.

These views are implemented using Django REST Framework's `APIView`, which provides a structured way to handle HTTP methods (GET, POST, etc.).

### Kafka Consumer: `KafkaConsumerCommand`

The Kafka consumer listens to messages on the Kafka topic `user_service`. When a new message arrives, it contains updated or new user information. The consumer processes this message and either creates or updates the corresponding user record in the database.

This component is crucial for real-time updates, allowing external systems to push updates to the user data. It uses the `confluent_kafka` Python library to consume messages from Kafka and process the data accordingly.

---

## Setup Instructions

### Prerequisites

* Python 3.11+
* Docker (for containerization)
* Kafka (for message consumption)
* Redis (if used for caching in other parts of your system, though not explicitly mentioned here)

### Steps

1. **Clone the repository**:

   ```
   git clone <repository-url>
   cd <project-directory>
   ```

2. **Set up the environment**:
   Install required dependencies:

   ```
   pip install -r requirements.txt
   ```

3. **Docker Setup**:
   Build the Docker image and start the container:

   ```
   docker build -t user_info_service .
   docker run -p 9810:9810 user_info_service
   ```

4. **Database Setup**:
   Run migrations to set up the database schema:

   ```
   python manage.py migrate
   ```

5. **Start the Kafka Consumer**:
   Run the Kafka consumer to start listening to the `user_service` topic:

   ```
   python manage.py kafka_consumer
   ```

6. **Access the API**:
   The API is available at `http://localhost:9810`. You can interact with the user data via HTTP GET and POST requests.

---

## Example Usage

### 1. **Create a New User (POST Request)**

To create a new user, send a POST request to the `/user` endpoint with the following JSON body:

```json
{
  "user_id": "e4b5f7fe-0937-4d7b-b6b4-e8d23262b3f1",
  "first_name": "John",
  "last_name": "Doe",
  "email": "john.doe@example.com",
  "phone_number": "+1234567890"
}
```

**Response**:

```json
{
  "user_id": "e4b5f7fe-0937-4d7b-b6b4-e8d23262b3f1",
  "first_name": "John",
  "last_name": "Doe",
  "email": "john.doe@example.com",
  "phone_number": "+1234567890"
}
```

### 2. **Retrieve an Existing User (GET Request)**

To retrieve a user's information, send a GET request to `/user?user_id=e4b5f7fe-0937-4d7b-b6b4-e8d23262b3f1`:

**Response**:

```json
{
  "user_id": "e4b5f7fe-0937-4d7b-b6b4-e8d23262b3f1",
  "first_name": "John",
  "last_name": "Doe",
  "email": "john.doe@example.com",
  "phone_number": "+1234567890"
}
```

### 3. **Kafka Consumer for Real-Time Updates**

The Kafka consumer automatically listens to the `user_service` Kafka topic. When a message containing user information is published to the topic, it will automatically update or create the corresponding user in the database.

Example message format:

```json
{
  "user_id": "e4b5f7fe-0937-4d7b-b6b4-e8d23262b3f1",
  "first_name": "John",
  "last_name": "Doe",
  "email": "new.email@example.com",
  "phone_number": "+1987654321"
}
```

The Kafka consumer updates the user's information based on the `user_id`.

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.
