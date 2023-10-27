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

let TheSearchformElem = $("#search_form");

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

function handleAddtoCart(amovieId){
    //prevent default


    let thisButton = $("#"+amovieId);

    let curMovieTitle = $("#"+amovieId).attr("data-movtitle");
    console.log("button clicked" + amovieId + " ");

    //use post to make back end query
    $.ajax("api/shopping-cart", {
        method: "POST",
        data: {
            action:"add",
            movieid:amovieId
        }
    });

    //move windows alert out side of ajax call since blocking
    window.alert("Successfully Added Movie: " + curMovieTitle);
}

function handleMovieListResult(resultData) {
    console.log("handleMovieListResult: populating movies table from resultData");

    // Populate the top20 table
    // Find the empty table body by id "top20_table_body"
    let Top20TableBodyElement = jQuery("#top20_table_body");

    // Iterate through resultData, no more than 20 entries

    console.log(resultData.length)
    const urlParams = new URLSearchParams(window.location.search);
    //clean up url params
    urlParams.delete("genre");

    urlParams.delete("title");
    urlParams.delete("year");
    urlParams.delete("director");
    urlParams.delete("star_name");

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
            urlParams.set("genre",resultData[i].genre[j]);
            // console.log("the new url " + "single-movie.html?"+ urlParams.toString());
            //set the url
            rowHTML += "<li><a href=\"MovieList.html?"+ urlParams.toString() + "\">"+resultData[i].genre[j]+"</a></li>";
        }
        rowHTML += "</ul>";
        rowHTML += "</td>";


        //adding a add to cart button
        rowHTML += "<td>";

        let theMovid = resultData[i].id;

        let AddtoCartPost = "<button onclick=\"handleAddtoCart(this.id)\" id=\""+theMovid+"\" data-movtitle=\""+resultData[i].title+"\">Add to Cart</button>\n";

        //atempt to bind

        rowHTML += AddtoCartPost;

        // rowHTML +="<button type=\"button\" onclick=\"handleAddtoCart(this.id)\" data-movtitle=\""+resultData[i].title+"\" id=\"" +resultData[i].id + "\">Add to Cart</button>";

        rowHTML += "</td>";


        //add row end html
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        Top20TableBodyElement.append(rowHTML);
    }
}

function handleSearchInfo(SubmitEvent){
    console.log("handling Browse event")
    SubmitEvent.preventDefault();

    const urlParams = new URLSearchParams(window.location.search);

    //first clean the url params
    urlParams.delete("genre");

    urlParams.delete("title");
    urlParams.delete("year");
    urlParams.delete("director");
    urlParams.delete("star_name");
    //reset the page to 1
    urlParams.set("page",1)

    //get fields
    console.log("the title is:" + $("#title_field").val());

    let titleVal = $("#title_field").val();
    let yearVal = $("#year_field").val();
    let directorVal = $("#director_field").val();
    let starNameVal = $("#star_name_field").val();

    if(titleVal != ""){
        urlParams.set("title",titleVal);
    }
    if(yearVal != ""){
        urlParams.set("year",yearVal);
    }
    if(directorVal != ""){
        urlParams.set("director",directorVal);
    }
    if(starNameVal != ""){
        urlParams.set("star_name",starNameVal);
    }
    // urlParams.set("title",$("#title_field").val());
    // urlParams.set("year",$("#year_field").val());
    // urlParams.set("director",$("#director_field").val());
    // urlParams.set("star_name",$("#star_name_field").val());


    //update the query param in browser
    window.location.search = urlParams.toString();




}

function handleSortOption(cartEvent){
    console.log("handling sort");
    // cartEvent.preventDefault();
    //test set query string
    const urlParams = new URLSearchParams(window.location.search);

    let DropdownValSelectedArray = $("#sort_dd").children("option:selected").val().split("_");

    // console.log("the val " + $("#sort_dd").children("option:selected").val() + " " + DropdownValSelectedArray[0]);

    //change query param
    urlParams.set("sortfirst",DropdownValSelectedArray[0]);
    urlParams.set("sorttype1",DropdownValSelectedArray[1]);
    urlParams.set("sorttype2",DropdownValSelectedArray[2]);

    //update the query param in browser
    window.location.search = urlParams.toString();


}

function handleItemLimitOption(cartEvent){
    console.log("handling sort");
    // cartEvent.preventDefault();
    //test set query string
    const urlParams = new URLSearchParams(window.location.search);

    let ItemLimitSelectVal = $("#itemlimit_dd").children("option:selected").val();

    console.log("the item limit val " + $("#itemlimit_dd").children("option:selected").val());

    //change query param
    urlParams.set("numlimit",ItemLimitSelectVal);

    //reset the page to 1
    urlParams.set("page",1)

    //update the query param in browser
    window.location.search = urlParams.toString();

}

function InitsetURLParamtoDef(){
    //check if the sort and page options exists

    const urlParams = new URLSearchParams(window.location.search);
    console.log("check url params" + urlParams.has("sortfirst") + "sorttype" + urlParams.has("sorttype"));

    // console.log(urlParams.has("sortfirst"))
    //test for sort
    if(!urlParams.has("sortfirst") || !urlParams.has("sorttype")){
        //setting to default
        urlParams.set("sortfirst","title");
        urlParams.set("sorttype","a");
        urlParams.set("page","1");
        urlParams.set("numlimit","10");
        window.location.search = urlParams.toString();

    }

    //test for pagination url params
    InitSetButtonsAndBox()



}

function OnclickPrevious(){
    const urlParams = new URLSearchParams(window.location.search);
    //checking for page 1
    let prevPageNum = Number(urlParams.get("page"))-1;

    if(prevPageNum >= 1){
        urlParams.set("page",prevPageNum);
        window.location.search = urlParams.toString();
    }


}

function OnclickNext(){
    const urlParams = new URLSearchParams(window.location.search);

    urlParams.set("page",Number(urlParams.get("page"))+1);

    //check if next page is null
    const curqueryString = window.location.search;

    jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "GET", // Setting request method
        url: "api/movie-list?" + urlParams + "&checking=true", // Setting request url, which is mapped by MovieListServlet
        success: (resultData) => {
            // JSON.parse(resultData);
            // console.log("the checking value" + resultData.empty + urlParams);

            //go to next page if not empty
            if(!resultData.empty){
                window.location.search = urlParams.toString();
            }

        } // Setting callback function to handle data returned successfully by the StarsServlet
    });



}

// Function to set the buttons and search box to what ever value is in url
function InitSetButtonsAndBox(){
    const urlParams = new URLSearchParams(window.location.search);


    //change the form value for input

    $("#title_field").val(urlParams.get("title"));
    $("#year_field").val(urlParams.get("year"));
    $("#director_field").val(urlParams.get("director"));
    $("#star_name_field").val(urlParams.get("star_name"));




// change sort dropdown to what ever is url
    if(urlParams.has("sortfirst") && urlParams.has("sorttype1") && urlParams.has("sorttype2")){
        let ValStr = urlParams.get("sortfirst") + "_" + urlParams.get("sorttype1") + "_" + urlParams.get("sorttype2");
        console.log("setting: " + ValStr);
        $("#sort_dd").val(ValStr);
        $('#sort_dd').select;

    }

// change item limit dropdown to what ever is url
    if(urlParams.has("numlimit")){
        let ItemLimitnumUrl = urlParams.get("numlimit");
        console.log("setting: " + ItemLimitnumUrl);
        $("#itemlimit_dd").val(ItemLimitnumUrl);
        $('#itemlimit_dd').select;

    }
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

const queryString = window.location.search;
console.log(queryString);
console.log("hello")

//initialize textbox and buttons to the text in  url params
InitSetButtonsAndBox();

// Makes the HTTP GET request and registers on success callback function handleMovieListResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/movie-list" + queryString, // Setting request url, which is mapped by MovieListServlet
    success: (resultData) => handleMovieListResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});






//bind the submit
TheSearchformElem.submit(handleSearchInfo);