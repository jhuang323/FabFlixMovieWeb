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

let myform = $("#search_form");

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

function handleBrowseInfo(cartEvent){
    // cartEvent.preventDefault();
    $(this)
        .find('input[name]')
        .filter(function () {
            return !this.value;
        })
        .prop('name', '');


}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

const queryString = window.location.search;
console.log(queryString);
console.log("hello")

// Makes the HTTP GET request and registers on success callback function handleMovieListResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/main-page" + queryString, // Setting request url, which is mapped by MovieListServlet
    success: (resultData) => handleMovieListResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});

let title_name = getParameterByName('title');
$("#title_form").attr("value", title_name);
let year_name = getParameterByName('year');
$("#year_form").attr("value", year_name);
let director_name = getParameterByName('director');
$("#director_form").attr("value", director_name);
let star_name = getParameterByName('star_name');
$("#star_name_form").attr("value", star_name);

//bind the submit
myform.submit(handleBrowseInfo);