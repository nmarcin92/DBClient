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
                    //pass attributes names
                    $scope.attributes = data.entity.attributes;
                    $scope.rows = data.entity.rows;
                    console.log($scope.rows);
                    $scope.history[$scope.history.length] = new historyItem("Read", true);
                } else {
                    $scope.history[$scope.history.length] = new historyItem("Read", false);
                    alert("Server error");
                    console.log(data.errors)
                }
            }).
            error(function(data, status, headers, config) {
                alert("error")
                $scope.history[$scope.history.length] = new historyItem("Query", false);
            });
}