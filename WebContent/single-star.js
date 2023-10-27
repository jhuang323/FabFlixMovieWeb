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

//binding
$("#nav_search").submit(handlenavsearch)