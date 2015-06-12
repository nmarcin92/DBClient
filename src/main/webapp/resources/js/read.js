function compare(a1,a2) {
    if (a1.attributeName < a2.attributeName)
        return -1;
    if (a1.attributeName > a2.attributeName)
        return 1;
    return 0;
}

objs.sort(compare);

function performRead($scope, $http){
    var data = {
            "parameters": {},
            "context": "RECORD",
            "entityName": $scope.tableName,
            "userSession": {
                "connectionType": $scope.databaseType,
                "dbCredentials": {
                    "username": $scope.userName,
                    "password": $scope.password,
                    "url": $scope.hostName + ":" + $scope.port,
                    "databaseName": $scope.databaseName
                }
            },
            "attributeNames": []
        };

    //add column names
    if($scope.columnNames == "" || $scope.columnNames == undefined){
        data.parameters.ENTIRE_RECORD = null;
    }
    else {
        var columns = $scope.columnNames.split(",");

        for(var i = 0; i < columns.length; i++){
            columns[i] = columns[i].trim();
        }

        data.attributeNames = columns;
    }

    $http.post(serverUrl + "/read", data).
        success(function(data, status, headers, config) {
            if(data.success){
                //mongo db
                if($scope.databaseType == "MONGODB"){
                    $scope.mongoOutput = true;

                    $scope.documents = [];

                    for(var i = 0; i < data.entity.rows.length; i++){
                        $scope.documents[$scope.documents.length] = JSON.stringify(data.entity.rows[i].attributes,null,"    ");
                    }
                }
                //normal output
                else {
                    $scope.mongoOutput = false;

                    //pass attributes names
                    $scope.attributes = data.entity.attributes;
                    $scope.attributes.sort(compare);
                    $scope.rows = data.entity.rows;

                    $scope.history[$scope.history.length] = new historyItem("Read", true);
                }
            } else {
                $scope.history[$scope.history.length] = new historyItem("Read", false);
                alert("Server error");
                console.log(data.errors)
            }
        }).
        error(function(data, status, headers, config) {
            alert("error")
            $scope.history[$scope.history.length] = new historyItem("Read", false);
        });
}