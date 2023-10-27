/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs three steps:
 *      1. Get parameter from request URL so it know which id to look for
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

function handleAddtoCart(movieId){
    //prevent default

    let curMovieTitle = $("#"+movieId).attr("data-movtitle");
    console.log("button clicked" + movieId + " ");

    //use post to make back end query
    $.ajax("api/shopping-cart", {
        method: "POST",
        data: {
            action:"add",
            movieid:movieId
        }
    });

    //move windows alert out side of ajax call since blocking
    window.alert("Successfully Added Movie: " + curMovieTitle);
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function handleResult(resultData) {

    console.log("handleResult: populating movie info from resultData");

    //populate the title
    let movieTitleElement = jQuery("#movie_title");

    movieTitleElement.append("<b>" + resultData["title"] + "</b>");

    console.log("result data" + resultData["title"]);

    //populate year


    movieTitleElement.append(" (" + resultData["year"] + ")");

    console.log("result data" + resultData["year"]);

    //populate movie detail page
    let movieDetailTableElement = jQuery("#movie_table_detail_body");

    //create genre string
    let genreListData = resultData["genre"];

    let genreHtml = "";

    for (let j = 0; j < genreListData.length; j++){
        genreHtml += "<a href=" + resultData.genreLink + "&genre=" + genreListData[j] + ">";
        if (j === (genreListData.length-1) ){

            genreHtml += genreListData[j] + "</a>";
        }
        else{
            genreHtml += genreListData[j] + "</a>" + ", ";

        }

    }


    let movieDetailStr = "<td>";
    movieDetailStr += resultData["director"];
    movieDetailStr += "</td>";

    movieDetailStr += "<td>";
    movieDetailStr += resultData["rating"];
    movieDetailStr += "</td>";

    movieDetailStr += "<td>";
    movieDetailStr += genreHtml;
    movieDetailStr += "</td>";

    movieDetailStr += "<td>";
    let movieId = getParameterByName('id');
    let AddtoCartPost = "<button onclick=\"handleAddtoCart(this.id)\" " +
        "class='btn btn-danger' id=\""+movieId+"\" data-movtitle=\""+resultData["title"]+"\">Add to Cart</button>\n";
    //atempt to bind
    movieDetailStr += AddtoCartPost;
    movieDetailStr += "</td>";

    movieDetailTableElement.append(movieDetailStr);



    //table of stars
    let starsUListElement = jQuery("#stars-table");

    let starListData = resultData["star"];

    for (let n = 0; n < starListData.length; n++){
        let starULHtml = "";
        starULHtml += "<li class=\"list-group-item list-group-item-secondary\">" +'<a href=single-star.html?id='+ starListData[n]["id"] + ">" + starListData[n]["name"] + "</a></li>";

        //append to UL
        starsUListElement.append(starULHtml)
    }

    //Access button within div and fill in the sessioned MovieListUrl
    $("#ButtonTop20ML-div").find("#backHomeButton").click(function(){
        window.location.href= resultData.movieListUrl;
    })

}

/**
 * Once this .js is loaded, following scripts will be executed by the browser\
 */

// Get id from URL
let movieId = getParameterByName('id');

// Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/single-movie?id=" + movieId, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});