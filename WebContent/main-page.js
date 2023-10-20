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
        gnreHrefstr = " <a href=\"MovieList.html?genre="+ resultData[i] +"\">" + resultData[i] + "</a>";
        GenreLinkElem.append(gnreHrefstr);

    }

    //insert search alpha
    for(let j = 0; j < SearchAlpabetParam.length;j++){
        console.log("alpha " + SearchAlpabetParam[j] );
        let alphaSStr = " <a href=\"MovieList.html?char="+ SearchAlpabetParam[j] +"\">" + SearchAlpabetParam[j] + "</a>";
        SearchAlphaElem.append(alphaSStr)
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


//call the api to get list of genres
$.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/main-page", // Setting request url, which is mapped by MovieListServlet
    success: (resultData) => handleGenreListResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});



//bind the submit
myform.submit(handleBrowseInfo);