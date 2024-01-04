# Project : SnapNote

## Overview
A Note taking Application - designed to simplify the way you capture, 
organize, and share your ideas. Whether you're a student, professional, or creative individual, SnapNote provides an intuitive 
and collaborative platform for managing your notes effectively.

## Technical Requirements
#### User Authentication
- Login/Signup: Users can create an account or log in securely.
#### Note Management
- Create Note: Users can create new notes.
- Get Note: Retrieve individual notes.
- Update Note: Modify existing notes with updated information.
- Delete Note: Remove unwanted notes.
#### Collaboration
- Share Notes: Enable users to share notes with others.
#### Search Functionality
- Search Notes: Implement a robust search feature for quick note retrieval.
## Functional Features
- [X] Correctness - code meets all the above technical requirements as described covering all edge cases.
- [X] Performance - Using Bucket4j Java rate-limiting library to implement rate limiting to handle high traffic.
- [X] Security - JWT authentication ensures secure endpoints, authorization to all resources. 
- [X] Quality - Well organized, maintainable code using MVC architecture, proper java docs for readability.
- [X] Completeness - Extensive unit tests for all the classes and methods using Junit.
- [X] Search Functionality - Text indexing and Label search functionality to search notes. 
## Tech Stack 
- Java : The backbone of SnapNote's robust backend.
- Spring Boot : Empowering the creation of scalable and efficient applications.
- MongoDB : A flexible NoSQL database for efficient data storage.
- Bucket4j : Ensuring system stability through effective rate limiting.
- Aspecj : Implementing custom annotations and aspect-oriented programming.
- Junit : Used for extensive unit testing to ensure code reliability and correctness.

## Getting Started

1. Clone the Repository:

```bash
git clone https://github.com/KaurManjot0432/SnapNote.git
```

2. Navigate to Project Directory:
```bash
cd snapnote
```

3. Build the Project:
```bash
./mvnw clean install
```

4. Run the Application:
```bash
./mvnw spring-boot:run
```

5. Access the API Endpoints:

- The application will be running, and you can access the API endpoints through a tool like Postman or curl.
6. Run Unit Tests:
```bash 
./mvnw test
```

## API Documentation

### Signup
- Endpoint POST : http://localhost:8080/api/auth/signup
- Request Body
```json
{
  "username": "test",
  "email" : "test@test.com",
  "password" : "12345678"
}
```
- Response
```json
{
  "message": "User registered successfully!"
}
```

### Signin
- Endpoint POST : http://localhost:8080/api/auth/login
- Request Body
```json
{
  "username": "test",
  "password" : "12345678"
}
```
- Response Body
```json
{
  "id": 1,
  "username": "test",
  "email": "test@test.com",
  "roles": [
    "ROLE_USER"
  ],
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYW5qb3QiLCJpYXQiOjE3MDM4NDE1MzksImV4cCI6MTcwMzkyNzkzOX0.el59jHdPbhbqX5MPDCAidZQWpd2Y0FfGdb2gwmIzMzs",
  "tokenType": "Bearer"
}
```


### Create Note for Authenticated User
- Endpoint POST : http://localhost:8080/api/notes
- Request Header : Authorization Bearer {accessToken}
- Request Body
```json
{
  "title" : "Example Note",
  "content": "Todo -> Acid-Base topics and periodicity topics",
  "labelList": ["Chemistry", "pending"]
}
```
- Response Body
```json
{
  "id": "659691c911c37912214a8ad3",
  "userName": "mandeep",
  "title": "Example Note",
  "content": "Todo -> Acid-Base topics and periodicity topics",
  "labelList": [
    "Chemistry",
    "pending"
  ],
  "createdAt": "2024-01-04T16:38:57.811785098"
}
```

### Get Note By ID for Authenticated User
- Endpoint GET : http://localhost:8080/api/notes/:id
- Request Header : Authorization Bearer {accessToken}

- Response Body
```json
{
  "id": "659691c911c37912214a8ad3",
  "userName": "mandeep",
  "title": "Example Note",
  "content": "Todo -> Acid-Base topics and periodicity topics",
  "labelList": [
    "Chemistry",
    "pending"
  ],
  "createdAt": "2024-01-04T16:38:57.811"
}
```

### Get All Notes for Authenticated User
- Endpoint GET : http://localhost:8080/api/notes
- Request Header : Authorization Bearer {accessToken}

- Response Body
```json
[
  {
    "id": "6595404b11a06e3841cd9309",
    "userName": "mandeep",
    "title": "Chemistry notes",
    "content": "Todo -> Acid-Base topics and periodicity topics",
    "labelList": [
      "Chemistry",
      "pending"
    ],
    "createdAt": "2024-01-03T16:38:59.419"
  },
  {
    "id": "6595407711a06e3841cd930a",
    "userName": "mandeep",
    "title": "Math notes",
    "content": "Todo -> Integration topics and probability topics",
    "labelList": [
      "math",
      "pending"
    ],
    "createdAt": "2024-01-03T16:39:43.864"
  }
]
```

### Update Note By ID for Authenticated User
- Endpoint PUT : http://localhost:8080/api/notes/:id
- Request Header : Authorization Bearer {accessToken}
- Request Body
```json
{
  "title" : "updated successfully",
  "content": "Todo -> Acid-Base topics and periodicity topics",
  "labelList": ["Updated", "pending"]
}
```
- Response Body
```json
{
  "id": "659691c911c37912214a8ad3",
  "userName": "mandeep",
  "title": "updated successfully",
  "content": "Todo -> Acid-Base topics and periodicity topics",
  "labelList": [
    "Updated",
    "pending"
  ],
  "createdAt": "2024-01-04T16:38:57.811"
}
```

### Delete Note By ID for Authenticated User
- Endpoint DELETE : http://localhost:8080/api/notes/:id
- Request Header : Authorization Bearer {accessToken}

- Response : Status: 204 No Content

### Share Note with other user
- Endpoint POST : http://localhost:8080/api/notes/:id/share?recipientUsername={name}
- Request Header : Authorization Bearer {accessToken}

- Response Body
```json
Note shared successfully.
```

### Search Note By Content Keywords for Authenticated User
- Endpoint GET : http://localhost:8080/api/notes/search?q=Todo&queryType=CONTENT
- Request Header : Authorization Bearer {accessToken}

- Response Body
```json
[
  {
    "id": "6595404b11a06e3841cd9309",
    "title": "Chemistry notes",
    "content": "Todo -> Acid-Base topics and periodicity topics",
    "labelList": [
      "Chemistry",
      "pending"
    ],
    "userName": "mandeep",
    "createdAt": "2024-01-03T16:38:59.419"
  },
  {
    "id": "6595407711a06e3841cd930a",
    "title": "Math notes",
    "content": "Todo -> Integration topics and probability topics",
    "labelList": [
      "math",
      "pending"
    ],
    "userName": "mandeep",
    "createdAt": "2024-01-03T16:39:43.864"
  }
]
```

### Search Note By Label Keywords for Authenticated User
- Endpoint GET : http://localhost:8080/api/notes/search?q=math&queryType=LABEL
- Request Header : Authorization Bearer {accessToken}

- Response Body
```json
[
  {
    "id": "6595407711a06e3841cd930a",
    "title": "Math notes",
    "content": "Todo -> Integration topics and probability topics",
    "labelList": [
      "math",
      "pending"
    ],
    "userName": "mandeep",
    "createdAt": "2024-01-03T16:39:43.864"
  }
]
```