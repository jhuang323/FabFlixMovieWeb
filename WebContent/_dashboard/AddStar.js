let add_star_form = $("#add_star_form");
let add_starmeg = $("#add_star_error_message");

/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleAddStarResult(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    if (resultDataJson["status"] === "success") {

        console.log("success");

    } else {
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#add_star_error_message").text(resultDataJson["message"]);
    }

    add_starmeg.text(resultDataJson["message"]);


}
function submitStarForm(formSubmitEvent) {
    console.log("submit info for new Star");
    formSubmitEvent.preventDefault();
    $.ajax(
        "api/AddStar", {
            method: "POST",
            // Serialize the login form to the data sent by POST request
            data: add_star_form.serialize(),
            success: handleAddStarResult
        }
    );
}

// Bind the submit action of the form to a handler function
add_star_form.submit(submitStarForm);

