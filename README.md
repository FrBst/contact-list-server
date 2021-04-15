# contact-list-server
Server-based phonebook for multiple users.\
Swagger documentation in api-docs.yaml, you can open and view it on https://editor.swagger.io
# Launching the server
Go to project folder in command line and execute `mvnw spring-boot:run`\
The server is running on localhost, port 8080.
# Examples
* To add new user called Ray:\
`curl -X POST -H "Content-Type: application/json" -d "{ ""Name"": ""Ray"" }" -i http://localhost:8080/users`
* Ray wants to save his friend's phone number:\
`curl -X POST -H "Content-Type: application/json" -d "{ ""UserId"": ""1"", ""Name"": ""Jin"", ""PhoneNumber"": { ""Number"": ""123-456-789"" } }" http://localhost:8080/contacts`
* Ray now wants to be called XX_destroyer_XX\
`curl -X PATCH -H "Content-Type: application/json" -d"{ ""Name"": ""XX_destroyer_XX"" }" -i http://localhost:8080/users/1`
* Ray doesn't want to have an account on this boring server:\
`curl -X DELETE http://localhost:8080/users/1`
