# Welcome to the CS122B Web Development Project
Group: Team mips
Members:
1. Justin K Huang (huangjk2)
2. Jose M Ruvalcaba (jmruval1)

## URLS:
URL for Website on AWS: 
URL for video: 

Note: To connect you must be on UCI VPN FULL!

## Part 1: Full Text search
We implemented the full text (replaced with fuzzy search) according to 
specifications ex font end cache, limit 3 characters, etc.
On autocomplete and full text search page(movie list).

## Part 2: Android App
Our android app uses the back end api and recieves the data in json format.
Parses the data and displays it to the user. We implement the login page without
recaptcha, movie list, single movie, and main page

## EC: Fuzzy Search
We implement the fuzzy search according to specification using the 
flamingo library. We normalized each result so it is a number between 0 and 1.


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
