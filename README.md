# Welcome to the CS122B Web Development Project
Group: Team mips
Members:
1. Justin K Huang (huangjk2)
2. Jose M Ruvalcaba (jmruval1)

## URLS:
URL for Website on AWS: 
URL for video: 

Note: To connect you must be on UCI VPN FULL!

## List of Files with Prepared Statements
1. LoginEmployeeServlet.java 
2. EmployeeDashServlet.java 
3. SingleMovieServlet.java 
4. ShoppingCart.java 
5. MovieListServlet.java 
6. MainPageServlet.java 
7. LoginServlet.java 
8. InsertMainsAndCasts.java 
9. CheckoutServlet.java 
10. SingleStarServlet.java


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
   Increases runtime by rewriting the statements into a more efficient form. The run time is now
   reduced by around 1/4 of original.
2. Filtered out any malformed objects if missing any crucial info(e.g No movie title, no actor 
   name, etc) before inserting. Increases runtime and memory moderately since we will store less 
   objects before insertion. Run time is reduced by 1/2 of original since less things to insert.

## Inconsistent Data found in BadFormat.txt in the root dir, BadFormat is written to after parsing

## Contributions:
Jose:
1. Worked on Android Application
2. Made sure the activities were connected and front end works

Justin:
1. Worked on part 1 the full text search
2. Implement fuzzy search using the library (flamingo) LEDA algorithm tested it works on android



## Instructions for Use:
1. Git clone project
2. Make sure all dependencies are installed ex tomcat,maven java11 ...
3. make sure a mysql user is created with username: mytestuser password: My6$Password
4. Make sure the database is imported into mysql
