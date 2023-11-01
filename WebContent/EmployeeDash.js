function  handleEmployeeDash(resultData){

}

$.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "/_dashboard/api/employee-dash", // Setting request url, which is mapped by
    // MovieListServlet
    success: (resultData) => handleEmployeeDash(resultData) // Setting callback function to handle data returned
    // successfully by the MainPage servlet
});
