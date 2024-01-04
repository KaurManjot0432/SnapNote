# Project : SnapNote

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