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



// Define the default parameters for movielist page
const DefaultQueryParams = "sortfirst=title&sorttype=a&page=1&numlimit=10"

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



//bind the submit
MPSearchform.submit(handleSearchInfo);