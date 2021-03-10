# gift-certificates-parent
## Project description https://github.com/mjc-school/MJC-School/blob/master/java/module%20%232.%20REST%20API%20Basics/rest_api_basics_task.md
### Version control:
- Java 8
- Spring 5.3.2
- Spring webmvc 5.3.2
- HikariCP 3.4.5
- MySql 8.0
- Spring jdbc 5.3.2
- Maven
-----------------------
### Recommendations for starting a project:
#### 1. Tag (start with Postman):
The tag has a base url : http://{server_url}/app_war/tags and push the button "send" . It is used to get all tags. For other operations, use the following steps:
- GET Tag by name. url : http://{server_url}/app_war/tags/query?name=extrem . Result 
{
    "id": 1,
    "name": "extrem"
}
- POST (for create Tag). url : http://{server_url}/app_war/tags , in the body we write the desired name tag (for example 
{
    "name": "java"
}) 
and push the button "send". Result 
{
    "id": 10,
    "name": "java"
} 
- PUT (for update Tag). url : http://{server_url}/app_war/tags/10 ,  in the body we write the desired name tag (for example 
{
    "name": "json"
}) 
and push the button "send". Result 
{
    "id": 10,
    "name": "json"
}
- DELETE (for delete Tag). url : http://{server_url}/app_war/tags/10 and push the button "send". Result HttpStatus.NO_CONTENT
#### jenkins
login kristina
password admin1234
