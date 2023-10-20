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

let myform = $("#myform");

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

    // Populate the top20 table
    // Find the empty table body by id "top20_table_body"
    let Top20TableBodyElement = jQuery("#top20_table_body");

    // Iterate through resultData, no more than 20 entries

    console.log(resultData.length)

    for (let i = 0; i < resultData.length; i++) {

        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<td>"+
            '<a href="single-movie.html?id=' + resultData[i].id + '">' +
            resultData[i].title +"</td>";
        rowHTML += "<td>"+ resultData[i].year +"</td>";
        rowHTML += "<td>"+ resultData[i].director +"</td>";
        rowHTML += "<td>"+ resultData[i].rating +"</td>";
        rowHTML += "<td>";
        rowHTML += "<ul>";
        for(let j = 0; j < resultData[i].star.length; j++){
            rowHTML += "<li>" +
                '<a href="single-star.html?id=' + resultData[i].star[j].id + '">' +
                resultData[i].star[j].name + "</li>";
        }
        rowHTML += "</ul>";
        rowHTML += "</td>";
        rowHTML += "<td>";
        rowHTML += "<ul>";
        for(let j = 0; j < resultData[i].genre.length; j++){
            rowHTML += "<li>" + resultData[i].genre[j] + "</li>";
        }
        rowHTML += "</ul>";
        rowHTML += "</td>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        Top20TableBodyElement.append(rowHTML);
    }
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
    url: "api/movie-list" + queryString, // Setting request url, which is mapped by MovieListServlet
    success: (resultData) => handleMovieListResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});

let genreName = getParameterByName('genre');

$("#gnre").attr("value", genreName);

//bind the submit
myform.submit(handleBrowseInfo);