# Project : SnapNote

## API Documentation

### Signup

1. Role = user
- Endpoint POST : http://localhost:8080/api/auth/signup
- Request
```json
{
  "username": "test",
  "email" : "test@test.com",
  "password" : "12345678"
}
```

2. Role = admin
- Endpoint POST : http://localhost:8080/api/auth/signup
- Request
```json
{
  "username": "ManjotKaur",
  "email" : "manjot@test.com",
  "password" : "12345678",
  "role": ["admin"]
}
```
- Response
```json
{
  "message": "User registered successfully!"
}
```

### Signin
- Endpoint POST : http://localhost:8080/api/auth/signin
- Request
```json
{
  "username": "test",
  "password" : "12345678"
}
```
- Response
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