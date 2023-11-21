/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs three steps:
 *      1. Get parameter from request URL so it knows which id to look for
 *      2. Use jQuery to talk to backend API to get the json data.
 *      3. Populate the data to correct html elements.
 */


/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
 */

let MPFullTextform = $("#autocomplete");

// Define the default parameters for movielist page
const DefaultQueryParams = "sortfirst=title&sorttype1=a&sorttype2=a&page=1&numlimit=10";


function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function handleResult(resultData) {

    console.log("handleResult: populating star info from resultData");

    // populate the star info h3
    // find the empty h3 body by id "star_info"
    let starInfoElement = jQuery("#star_info");


    // append two html <p> created to the h3 body, which will refresh the page
    starInfoElement.append("<p>Star Name: " + "<b>" + resultData.star_name + "</b>" + "</p>" +
        "<p>Date Of Birth: " + "<b>" + resultData.star_dob + "</b>" + "</p>");

    console.log("handleResult: populating star info from resultData");

    // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let movieTableBodyElement = jQuery("#movie_table_body");
    console.log("handleResult: populating star table from resultData.movies");
    // Concatenate the html tags with resultData jsonObject to create table rows
    for (let i = 0; i < resultData.movies.length; i++) {
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" +
            // Add a link to single-movie.html with id passed with GET url parameter
            '<a href="single-movie.html?id=' + resultData.movies[i]['movie_id'] + '">' +
            resultData.movies[i]["movie_title"] + "</th>";
        rowHTML += "<th>" + resultData.movies[i]["movie_director"] + "</th>";
        rowHTML += "<th>" + resultData.movies[i]["movie_year"] + "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }

    //Access button and fill in the sessioned MovieListUrl
    $('#backHomeButton').click(function(){
        window.location.href= resultData.movieListUrl;
    })
    console.log("handleResult: successful population of star table from resultData.movies");
}
function  handlenavsearch(aparam){
    // Define the default parameters for movielist page
    const DefaultQueryParams = "sortfirst=title&sorttype1=a&sorttype2=a&page=1&numlimit=10";
    aparam.preventDefault();

    let title = $("#title_field").val();
    let year = $("#year_field").val();
    let director = $("#director_field").val();
    let starname = $("#star_name_field").val();




    const urlParams = new URLSearchParams();



    if(title !== ""){
        urlParams.set("title",title);
    }
    if(year !== ""){
        urlParams.set("year",year);
    }
    if(director !== ""){
        urlParams.set("director",director);
    }
    if(starname !== ""){
        urlParams.set("starname",starname);
    }




    window.location.href="MovieList.html?"+ urlParams.toString() + "&" + DefaultQueryParams;
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser\
 */

// Get id from URL
let starId = getParameterByName('id');

// Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/single-star?id=" + starId, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});


//full text portion
/*
 * This function is called by the library when it needs to lookup a query.
 *
 * The parameter query is the query string.
 * The doneCallback is a callback function provided by the library, after you get the
 *   suggestion list from AJAX, you need to call this function to let the library know.
 */
function handleLookup(query, doneCallback) {
    console.log("autocomplete initiated")
    console.log("sending AJAX request to backend Java Servlet")

    // TODO: if you want to check past query results first, you can do it here

    //check the cache
    if(localStorage.getItem(query) != null){
        console.log("Cache Hit: Query: " + query);
        let cacheres = localStorage.getItem(query);

        handleLookupAjaxSuccess(cacheres, query, doneCallback);
    }
    else {
        console.log("Cache Miss: Query Backend: " + query);
        // sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
        // with the query data
        jQuery.ajax({
            "method": "GET",
            // generate the request url from the query.
            // escape the query string to avoid errors caused by special characters
            "url": "movie-suggestion?query=" + escape(query),
            "success": function(data) {
                // pass the data, query, and doneCallback function into the success handler
                handleLookupAjaxSuccess(data, query, doneCallback)
            },
            "error": function(errorData) {
                console.log("lookup ajax error")
                console.log(errorData)
            }
        })
    }


}


/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
 *
 * data is the JSON data string you get from your Java Servlet
 *
 */
function handleLookupAjaxSuccess(data, query, doneCallback) {
    console.log("lookup ajax successful")

    // parse the string into JSON
    var jsonData = JSON.parse(data);
    console.log(jsonData)

    // TODO: if you want to cache the result into a global variable you can do it here


    localStorage.setItem(query,JSON.stringify(jsonData));

    // for(let i = 0; i < jsonData.length; i++){
    //     console.log("json val: " + jsonData[i]["value"] + " value: " + jsonData[i]["data"]["movieID"]);
    //     localStorage.setItem(jsonData[i]["value"],jsonData[i]["data"]["movieID"]);
    // }



    // call the callback function provided by the autocomplete library
    // add "{suggestions: jsonData}" to satisfy the library response format according to
    //   the "Response Format" section in documentation
    doneCallback( { suggestions: jsonData } );
}


/*
 * This function is the select suggestion handler function.
 * When a suggestion is selected, this function is called by the library.
 *
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
    // TODO: jump to the specific result page based on the selected suggestion

    console.log("you select " + suggestion["value"] + " with ID " + suggestion["data"]["movieID"]);



    window.location.href = "single-movie.html?id=" + suggestion["data"]["movieID"];
}


/*
 * This statement binds the autocomplete library with the input box element and
 *   sets necessary parameters of the library.
 *
 * The library documentation can be find here:
 *   https://github.com/devbridge/jQuery-Autocomplete
 *   https://www.devbridge.com/sourcery/components/jquery-autocomplete/
 *
 */
// $('#autocomplete') is to find element by the ID "autocomplete"
MPFullTextform.autocomplete({
    // documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
        handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
        handleSelectSuggestion(suggestion)
    },
    // set delay time
    deferRequestBy: 300,
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as minimum characters

    //min chars param >= 3
    minChars: 3

});


/*
 * do normal full text search if no suggestion is selected
 */
function handleNormalSearch(query) {

    console.log("doing normal search with query: " + query);
    // TODO: you should do normal search here

    let redirFultextUrl = "MovieList.html?fulltext=true" +
        "&title=" + escape(query) +
        "&" + DefaultQueryParams;
    //redirect
    window.location.href = redirFultextUrl;

}


//binding
$("#nav_search").submit(handlenavsearch)


//full text search
// bind pressing enter key to a handler function
MPFullTextform.keypress(function(event) {
    // keyCode 13 is the enter key
    if (event.keyCode == 13) {
        // pass the value of the input box to the handler function
        handleNormalSearch($('#autocomplete').val())
    }
})