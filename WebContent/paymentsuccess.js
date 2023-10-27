function handleTotal(resultObj){
    let shoppingCartTable = jQuery("#shoppingCartTable");
    shoppingCartTable.append("<tr><td>" + "Total Amount: $" + resultObj.total + "</td></tr>")

    //clear the cart
    $.ajax(
        "api/shopping-cart", {
            method: "POST",
            // Serialize the login form to the data sent by POST request
            data: {
                action:"clearcart"
            }
        }
    );

}



function handleCartArray(resultArray) {
    console.log("Handling shopping cart array");

    let shoppingCartTable = jQuery("#shoppingCartTable");
    for (let i = 0; i < resultArray.length; i++) {
        let rowHTML = "";
        rowHTML += "<tr>";

        rowHTML += "<td>";
        rowHTML += resultArray[i].MovieTitle;
        rowHTML += "</td>";

        rowHTML += "<td>";


        rowHTML += resultArray[i].MovieQuantity;

        rowHTML += "</td>";



        rowHTML += "<td>";
        rowHTML += resultArray[i].MoviePrice;
        rowHTML += "</td>";

        rowHTML += "<td>";
        let total = parseFloat(parseFloat(resultArray[i].MoviePrice) * parseFloat(resultArray[i].MovieQuantity)).toFixed(2);
        rowHTML += total.toString();
        rowHTML += "</td>";

        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        shoppingCartTable.append(rowHTML);
    }
    jQuery.ajax({
        dataType: "json",  // Setting return data type
        method: "GET",// Setting request method
        url: "api/shopping-cart?action=total", // Setting request url, which is mapped by StarsServlet in Stars.java
        success: (resultObj) => handleTotal(resultObj) // Setting callback function to handle data returned successfully by the SingleStarServlet
    });
}


jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/shopping-cart?action=view", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultArray) => handleCartArray(resultArray) // Setting callback function to handle data returned successfully by the SingleStarServlet
});
