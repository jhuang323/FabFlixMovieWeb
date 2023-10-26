

/**
 * Handle the data returned by IndexServlet
 * @param resultDataString jsonObject, consists of session info
 */
function handleSessionData(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle session response");
    console.log(resultDataJson);
    console.log(resultDataJson["sessionID"]);

    // show the session information 
    $("#sessionID").text("Session ID: " + resultDataJson["sessionID"]);
    $("#lastAccessTime").text("Last access time: " + resultDataJson["lastAccessTime"]);

    // show cart information
    handleCartArray(resultDataJson["previousItems"]);
}

/**
 * Handle the items in item list
 * @param resultArray jsonObject, needs to be parsed to html
 */
function handleCartArray(resultArray) {
    console.log(resultArray);
    let item_list = $("#item_list");
    // change it to html list
    let res = "<ul>";
    for (let i = 0; i < resultArray.length; i++) {
        // each item will be in a bullet point
        res += "<li>" + resultArray[i] + "</li>";
    }
    res += "</ul>";

    // clear the old array and show the new array in the frontend
    item_list.html("");
    item_list.append(res);
}

//handle the checkout response
function handleCheckoutResponse(ajsonobj){
    let resultDataJson = JSON.parse(ajsonobj);

    if(resultDataJson.success == 0){
        window.alert("failed")
    }
    else {
        window.alert("success")
        //redirect to success page
    }
}

/**
 * Submit form content with POST method
 * @param cartEvent
 */
function handleCheckoutInfo(cartEvent) {
    console.log("submit cart form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    cartEvent.preventDefault();

    $.ajax("api/checkout", {
        method: "POST",
        data: CheckoutForm.serialize(),
        success: resultDataString => {
            handleCheckoutResponse(resultDataString);
        }
    });

    // clear input form
    cart[0].reset();
}


// Bind the submit action of the form to a event handler function

