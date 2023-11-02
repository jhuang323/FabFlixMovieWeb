function  handleEmployeeDash(resultData){

    let metaDataTable = jQuery("#metaDataTableBody");


    for(let i=0; i < resultData.length;i++){
        let rowHTML = "";
        rowHTML += "<tr>";

        rowHTML +="<td>";
        rowHTML += resultData[i].table_name;
        rowHTML +="</td>";
        rowHTML +="<td>";
        for(let j=0;j<resultData[i].table_info.length;j++){
            rowHTML += resultData[i].table_info[j].column_name + "\n";
        }
        rowHTML +="</td>";
        rowHTML +="<td>";
        for(let j=0;j<resultData[i].table_info.length;j++){
            rowHTML += resultData[i].table_info[j].column_type + "\n";
        }
        rowHTML +="</td>";

        rowHTML += "</tr>";
        metaDataTable.append(rowHTML);
    }
}

$.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "/_dashboard/api/employee-dash", // Setting request url, which is mapped by
    // MovieListServlet
    success: (resultData) => handleEmployeeDash(resultData) // Setting callback function to handle data returned
    // successfully by the MainPage servlet
});
