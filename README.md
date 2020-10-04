# Mongo Rest Query Builder [![Maven_Central](https://maven-badges.herokuapp.com/maven-central/io.github.codehookschannel/mongo-rest-query-builder/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.codehookschannel/mongo-rest-query-builder)

This is the Simple Java Library that make use of Mongo Query class to prepare the query builder. You will just pass in the request params from front end depending on that this utility helps you to query the data. You need not write another @Query or any methods related to Spring data's Repository methods.
**Cool, Right?**

Okay, enough talk lets discuss how we can achieve this with the help of library.

#### 1. First add the dependency to you pom.xml

Add the followwing lines of code to your pom.xml
```xml
<dependency>
    <groupId>io.github.codehookschannel</groupId>
    <artifactId>mongo-rest-query-builder</artifactId>
    <version>1.0.1</version>
</dependency>
```

If you face any issues related to the spring-mongo-data linrary then you can exlude that from the dependency.
```xml
<dependency>
    <groupId>io.github.codehookschannel</groupId>
    <artifactId>mongo-rest-query-builder</artifactId>
    <version>1.0.1</version>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
        </exclusion>
    </exlusions>
</dependency>
```

You can check the maven repo [here](https://mvnrepository.com/artifact/io.github.codehookschannel/mongo-rest-query-builder) for the latest version `<version>Latest Version Here</version>`

#### 2. Create Instance of QueryBuilder So that you can autowire that in your Spring Boot Applicaitons.

Just create Bean so that you can autowire the bean whenever needed.
```java
import com.codehooks.QueryBuilder;

@Configuration
public class QueryConfig {

    @Bean
    public QueryBuilder queryBuilder() {
        return new QueryBuilder();
    }
}
```

#### 3. Using QueryBuilder
Just go to your service (considering you have service layer in place or just use this bean from where you are making the database calls) and autowire the bean. and then pass the queries you are getting from request. `There are some rules we need to follow when making the frontend requests with query params. We will talk about them in next secion`

```java
import com.codehooks.QueryBuilder;

@Service
public class StudentServiceImpl {
    
    @Autowired
    private QueryBuilder queryBuilder;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Student> queryStudents(HashMap<String, Object> params) {
        Query query = queryBuilder.prepareQuery(params);
        List<Student> students = mongoTemplate.find(query, Student.class);
        return students;
    }

    // Remaining classes are skipped.

}
```

Here, you have controll over the Document type as you are using Mongo Template on your own you can further edit the query before executing it.

#### 4. Note on Passing Query Params.
While making the request we need to take care of the terms we are passing. Whenever we make request with params we need to keep the following points in mind.
1. Query Field (one we pass thorugh request) should be same as the column name (field name in mongo document).
2. Should provide the condition after column name separated by '.' (period).
3. Value should be provided after the '=' sign.

Lets discuss with an example.
Considering we have students collection and we want to filter the student whose name starts with 'V'. Then we prepare the query as follows.

> `http://{host}:{port}/{request-mapping}?name.startsWith=V`

What if you want to load the sudents whose marks are in between 50 and 80?

> `http://{host}:{port}/{request-mapping}?marks.between=50,80`

***Note: I am still adding the features to the library so, It will be unstable till the release of second version. Please let me know the issues / Suggestions though the issues section of this repo.***

#####Please refer following table for reference.

| Query Params | Similar Query |
|--------------|---------------|
 {columnName}.startsWith=value  | where {columnName} like '{value}%'
 {columnName}.endsWith=value         |              where {columnName} like '%{value}'
 {columnName}.contains=value       |                where {columnName} like '%{value}%'
 {columnName}.containsIc=value      |               where {columnName} like '%{value}%' (ignoreCase)
 {columnName}.eq=value               |              where {columnName} [like| =] 'value'
 {columnName}.like=value              |             where {columnName} [like| =] 'value'
 {columnName}.neq=value                |            where {columnName} != 'value'
 {columnName}.between=min,max           |           where {columnName} between 'min' and 'max'
 {columnName}.gt=value                   |          where {columnName} > value
 {columnName}.gte=value                   |         where {columnName} >= value
 {columnName}.lt=value                     |        where {columnName} < value
 {columnName}.lte=value                     |       where {columnName} <= value
 {columnName}.in=value1,value2....value-n    |      where {columnName} in (values)
 {columnName}.nin=value1,value2....value-n    |     where {columnName} not in (values)
 {columnName}.sort=a or d                      |    Sort by {columnName} d ? desc : asc
 pageable.props=pageNum,count                   |   creates the pageable by taking pageNum and count.