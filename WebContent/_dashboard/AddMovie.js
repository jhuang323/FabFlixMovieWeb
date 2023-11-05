let add_movie_form = $("#add_movie_form");
let add_moviemeg = $("#add_movie_error_message");

/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleAddMovieResult(resultDataString) {
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

    add_moviemeg.text(resultDataJson["message"]);


}
function submitMovieForm(formSubmitEvent) {
    console.log("submit info for new movie");
    formSubmitEvent.preventDefault();
    $.ajax(
        "api/AddMovie", {
            method: "POST",
            // Serialize the login form to the data sent by POST request
            data: add_movie_form.serialize(),
            success: handleAddMovieResult
        }
    );
}

// Bind the submit action of the form to a handler function
add_movie_form.submit(submitMovieForm);

