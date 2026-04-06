curl -i -X POST http://localhost:8080/users \
-H "Content-Type: application/json" \
-d "{\"name\":\"Ivan\",\"surname\":\"Grib\",\"email\":\"ivan@gmail.com\"}"

curl -i -X POST http://localhost:8080/users \
-H "Content-Type: application/json" \
-d "{\"name\":\"Ivan2\",\"surname\":\"Test\",\"email\":\"ivan@gmail.com\"}"

curl -i -X PUT http://localhost:8080/users/1 \
-H "Content-Type: application/json" \
-d "{\"name\":\"Updated Ivan\",\"surname\":\"Updated Grib\",\"email\":\"updated@gmail.com\"}"

curl -i -X PUT http://localhost:8080/users/999 \
-H "Content-Type: application/json" \
-d "{\"name\":\"X\",\"surname\":\"Y\",\"email\":\"x@y.com\"}"

curl -i -X PATCH http://localhost:8080/users/1 \
-H "Content-Type: application/json" \
-d "{\"name\":\"Patched Ivan\"}"

curl -i -X PATCH http://localhost:8080/users/1 \
-H "Content-Type: application/json" \
-d "{\"email\":\"patched@gmail.com\"}"

curl -i -X POST http://localhost:8080/users \
-H "Content-Type: application/json" \
-d "{\"name\":\"Petr\",\"surname\":\"Petrov\",\"email\":\"petr@gmail.com\"}"

curl -i -X PATCH http://localhost:8080/users/1 \
-H "Content-Type: application/json" \
-d "{\"email\":\"petr@gmail.com\"}"

curl -i -X DELETE http://localhost:8080/users/1

curl -i -X DELETE http://localhost:8080/users/999

curl -i http://localhost:8080/users

curl -i http://localhost:8080/users
