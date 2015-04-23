var url = "localhost:8080/DBClient/read";

function loadData(){
    displayData(json);
    return;

    $.post("demo_test_post.asp",
        {
            "parameters": {"ENTIRE_RECORD": null},
            "context": "RECORD",
            "entityName": "tabela_2",
            "userSession": {
                "connectionType": "POSTGRESQL",
                "dbCredentials": {
                    "username": "postgres",
                    "password": "postgres",
                    "url": "localhost:5432",
                    "databaseName": "postgres"
                }
            },
            "attributeNames": []
        },
        function(data, status){
            var data = JSON.parse(data);
            displayData(data);
        });
}

function displayData(data){
    var table = document.getElementById("result");

    //fill the title of the table
    var header = table.createTHead();
    var row = header.insertRow(0);
    var attributes = data.entity.attributes;

    for(var i = 0; i < attributes.length; i++){
        var cell = row.insertCell(row.length);
        cell.innerHTML = '<b>' + attributes[i].attributeName + '</b>';
    }

    //fill the table
    var rows = data.entity.rows;

    for(var i = 0; i < rows.length; i++){
        var row = table.insertRow();

        for(var j = 0; j < attributes.length; j++){
            var cell = row.insertCell(row.length);
            var value = rows[i].attributes[attributes[j].attributeName];
            cell.innerHTML = value;
        }
    }

    var rowCount = document.getElementById("rows-cnt");
    rowCount.innerHTML = "rows: " + rows.length;

}