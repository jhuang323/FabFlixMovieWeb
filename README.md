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

## Contributions:
Jose:
1. Created Servlet for Single Star Page
2. Completed Front and Back end of Single Star Page along with color and css for the extra credit
3. Completed Shopping Cart
4. Added navbar
5. Completed Search
6. Extended Single Casts and Star
Justin:
1. Created Servlet for Single Casts Page
2. Completed Front and Back end of Single Casts Page along with color and css for the extra credit
3. Beautified the website
4. Completed task 1
5. Compelted Browse
6. Pagination and Sorting
Jose/Justin:
1. Created Servlet for Casts List Page
2. Completed Front and Back end of Casts List Page along with color and css for the extra credit
3. Completed Casts List updates
4. Completed Shopping Cart front and back


## Instructions for Use:
1. Git clone project
2. Make sure all dependencies are installed ex tomcat,maven java11 ...
3. make sure a mysql user is created with username: mytestuser password: My6$Password
4. Make sure the database is imported into mysql
