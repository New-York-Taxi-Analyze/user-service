# user-service

## Create user:
``` bash
curl --request POST \
  --url http://localhost:8089/api/v1/createUser \
  --header 'Content-Type: application/json' \
  --data '{
      "username": "username",
      "email": "user@test.com",
      "password": "12345",
      "first_name": "user",
      "last_name": "test"
  }
'
```
