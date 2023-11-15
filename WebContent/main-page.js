/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */


/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */


let MPSearchform = $("#search_form");

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

function handleMovieListResult(resultData) {
    console.log("handleMovieListResult: populating movies table from resultData");

}

function  handleGenreListResult(resultData){
    //modify the search alphabet below
    let SearchAlpabetParam = '0123456789abcdefghijklmnopqrstuvwxyz*'.split('');

    //handles result data from the api
    console.log("In handle genre list function");

    //find the div
    let GenreLinkElem = $("#genre_div");
    let SearchAlphaElem = $("#searchAlpha_div");

    // populate the div with links
    let gnreHrefstr = "";
    for(let i = 0;i < resultData.length; i++){
        //appending  a link
        console.log('adding ' + resultData[i]);
        gnreHrefstr = " <a href=\"MovieList.html?genre="+ resultData[i] + "&" + DefaultQueryParams +"\">" + resultData[i] +  "</a>";
        GenreLinkElem.append(gnreHrefstr);

    }

    //insert search alpha
    for(let j = 0; j < SearchAlpabetParam.length;j++){
        console.log("alpha " + SearchAlpabetParam[j] );
        let alphaSStr = " <a href=\"MovieList.html?char="+ SearchAlpabetParam[j] + "&" + DefaultQueryParams +"\">" + SearchAlpabetParam[j] + "</a>";
        SearchAlphaElem.append(alphaSStr)
    }



}

function  handlenavsearch(aparam){
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

function handleSearchInfo(cartEvent){
    cartEvent.preventDefault();

    //define final search param str
    let FinalSearchParamStr = "";


    //get the search form fields
    let titleVal = $("#title_form").val();
    let yearVal = $("#year_form").val();
    let directorVal = $("#director_form").val();
    let starNameVal = $("#star_name_form").val();


    let paramCount = 0;

    //checking of any of the vals are null
    if(titleVal != ''){
        FinalSearchParamStr += "title=" + titleVal;
        paramCount++;
    }
    if(yearVal != ''){
        FinalSearchParamStr += "&year=" + yearVal;
        paramCount++;
    }
    if(directorVal != ''){
        FinalSearchParamStr += "&director=" + directorVal;
        paramCount++;
    }
    if(starNameVal != ''){
        FinalSearchParamStr += "&star_name=" + starNameVal;
        paramCount++;
    }



    //add on the default
    FinalSearchParamStr += "&" + DefaultQueryParams;


    // redirect user to new movielist url
    if(paramCount > 0){
        location.href = "MovieList.html?" + FinalSearchParamStr;
    }
    else{
        $("#search_error_message").text("ERROR: You must enter at least one search parameter")
    }



}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */


//call the api to get list of genres
$.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/main-page", // Setting request url, which is mapped by MovieListServlet
    success: (resultData) => handleGenreListResult(resultData) // Setting callback function to handle data returned successfully by the MainPage servlet
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
});


/*
 * do normal full text search if no suggestion is selected
 */
function handleNormalSearch(query) {
    console.log("doing normal search with query: " + query);
    // TODO: you should do normal search here
}



//bind the submit
MPSearchform.submit(handleSearchInfo);

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