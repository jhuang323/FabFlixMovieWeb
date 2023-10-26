function handleIncrementtoCart(movieId){
    //prevent default

    console.log(" increment button clicked" + movieId + " ");

    //use post to make back end query
    $.ajax("api/shopping-cart", {
        method: "POST",
        data: {
            action:"add",
            movieid:movieId
        },
        success: (resultData) => {
            window.location.reload()
            jQuery.ajax({
                dataType: "json",  // Setting return data type
                method: "GET",// Setting request method
                url: "api/shopping-cart?action=view", // Setting request url, which is mapped by StarsServlet in Stars.java
                success: (resultArray) => {
                    handleCartArray(resultArray)
                } // Setting callback function to handle data returned successfully by the SingleStarServlet
            });
        }
    });



}

function handleDecrementtoCart(movieId){
    //prevent default

    console.log(" decrement button clicked" + movieId + " ");

    //use post to make back end query
    $.ajax("api/shopping-cart", {
        method: "POST",
        data: {
            action:"subtract",
            movieid:movieId
        },
        success: (resultData) => {
            window.location.reload()
            jQuery.ajax({
                dataType: "json",  // Setting return data type
                method: "GET",// Setting request method
                url: "api/shopping-cart?action=view", // Setting request url, which is mapped by StarsServlet in Stars.java
                success: (resultArray) => {
                    handleCartArray(resultArray)
                } // Setting callback function to handle data returned successfully by the SingleStarServlet
            });
        }
    });
}

function handleDeletetoCart(movieId){
    //prevent default

    let curMovieTitle = $("#"+movieId).attr("data-movtitle");
    console.log(" delete button clicked" + movieId + " ");

    //use post to make back end query
    $.ajax("api/shopping-cart", {
        method: "POST",
        data: {
            action:"delete",
            movieid:movieId
        },
        success: (resultData) => {
            window.location.reload()
            jQuery.ajax({
                dataType: "json",  // Setting return data type
                method: "GET",// Setting request method
                url: "api/shopping-cart?action=view", // Setting request url, which is mapped by StarsServlet in Stars.java
                success: (resultArray) => {
                    handleCartArray(resultArray)
                } // Setting callback function to handle data returned successfully by the SingleStarServlet
            });
        }
    });

    //move windows alert out side of ajax call since blocking
    window.alert("Successfully Deleted Movie: " + curMovieTitle);
}

function handleCartArray(resultArray) {
    console.log("Handling shopping cart array");

    let shoppingCartTable = jQuery("#shoppingCartTable");
    for (let i = 0; i < resultArray.length; i++) {
        let rowHTML = "";
        rowHTML += "<tr>";

        rowHTML += "<th>";
        rowHTML += resultArray[i].MovieTitle;
        rowHTML += "</th>";

        rowHTML += "<th>";

        let theMovid = resultArray[i].MovieID;

        let DecrementtoCartPost = "<button onclick=\"handleDecrementtoCart(this.id)\" id=\""+theMovid+"\" data-movtitle=\""+resultArray[i].MovieTitle+"\"> - </button>\n";
        //atempt to bind
        rowHTML += DecrementtoCartPost;

        rowHTML += resultArray[i].MovieQuantity;

        let IncrementtoCartPost = "<button onclick=\"handleIncrementtoCart(this.id)\" id=\""+theMovid+"\" data-movtitle=\""+resultArray[i].MovieTitle+"\"> + </button>\n";
        //atempt to bind
        rowHTML += IncrementtoCartPost;


        rowHTML += "</th>";

        rowHTML += "<th>";
        let DeletetoCartPost = "<button onclick=\"handleDeletetoCart(this.id)\" id=\""+theMovid+"\" data-movtitle=\""+resultArray[i].MovieTitle+"\"> X </button>\n";
        //atempt to bind
        rowHTML += DeletetoCartPost;
        rowHTML += "</th>";

        rowHTML += "<th>";
        rowHTML += resultArray[i].MoviePrice;
        rowHTML += "</th>";

        rowHTML += "<th>";
        let total = parseFloat(resultArray[i].MoviePrice) * parseFloat(resultArray[i].MovieQuantity);
        rowHTML += total.toString();
        rowHTML += "</th>";

        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        shoppingCartTable.append(rowHTML);
    }
}

jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/shopping-cart?action=view", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultArray) => handleCartArray(resultArray) // Setting callback function to handle data returned successfully by the SingleStarServlet
});
