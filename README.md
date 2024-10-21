JDK 22
1. Start docker compose 
2. spring boot application required jdk 17
3. To start spring boot you can build jar file with  java -jar jarname.jar
4. access application localhost:8084/api/user


for save
localhost:8084/api/user

{
"name":"Ratan",
"age":39,
"address": {
"city":"Inodre",
"state":"MP",
"country":"India"
}

}

//delete
curl --location --request DELETE 'localhost:8084/api/user/16'

//put

curl --location --request PUT 'localhost:8084/api/user/14' \
--header 'Content-Type: application/json' \
--data '{
"name":"Rajiv",
"age":26,
"address": {
"city":"Mumbai",
"state":"MH",
"country":"India"
}

}'

//get

curl --location 'localhost:8084/api/user/5'