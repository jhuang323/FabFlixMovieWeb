# Welcome to the CS122B Web Development Project
Group: Team mips
Members:
1. Justin K Huang (huangjk2)
2. Jose M Ruvalcaba (jmruval1)

## URLS:
URL for Website on AWS: http://54.153.82.70:8080/cs122b-project2/login.html
URL for video: https://youtu.be/1eo5zKPRlMQ

Note: To connect you must be on UCI VPN FULL!

## Design Choices for Searching
### Searching
We used the %SearchQuery% for searching because it is substring matching so it matches the query anywhere in the string. We used the lower() and LIKE for sql so it is case insensitive.
### Browsing
ACharacter% We used the percent after the character since according to the requirements it should begin with the Character to be matched. We also used the lower() and LIKE for sql so it is case insensitive.

### Files with Prepared Statements
1. src/SingleStarServlet.java
2. src/SingleMovieServlet.java
3. src/ShoppingCart.java
4. src/MovieListServlet.java
5. src/LoginServlet.java
6. src/LoginEmployeeServlet.java
7. src/EmployeeDashServlet.java
8. src/CheckoutServlet.java
9. src/InsertMainsAndCasts.java

### Parsing XML Optimizations:
1. Utilized executeBatch with our prepared statements to insert multiple entries at a time. 
   Increases runtime by a 
2. Filtered out any malformed objects if missing any crucial info(e.g No movie title, no actor 
   name, etc) before inserting. Increases runtime and memory moderately since we will store less 
   objects before insertion.

## Inconsistent Data found in BadFormat.txt in the root dir, BadFormat is written to after parsing

## Contributions:
Jose:
1. Created Servlet for Single Star Page
2. Completed Front and Back end of Single Star Page along with color and css for the extra credit
3. Completed Shopping Cart
4. Added navbar
5. Completed Search
6. Extended Single Movie and Star
7. Completed Task 2
8. Completed Task 4
9. Completed Extra credit
Justin:
1. Created Servlet for Single Movie Page
2. Completed Front and Back end of Single Movie Page along with color and css for the extra credit
3. Beautified the website
4. Completed task 1
5. Compelted Browse
6. Pagination and Sorting
7. Completed Task 5
8. Completed Task 1
9. Completed Task 3
Jose/Justin:
1. Created Servlet for Movie List Page
2. Completed Front and Back end of Movie List Page along with color and css for the extra credit
3. Completed Movie List updates
4. Completed Shopping Cart front and back
5. Completed Task 6


## Instructions for Use:
1. Git clone project
2. Make sure all dependencies are installed ex tomcat,maven java11 ...
3. make sure a mysql user is created with username: mytestuser password: My6$Password
4. Make sure the database is imported into mysql
