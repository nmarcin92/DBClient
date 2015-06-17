function runQuery($scope, $http){
    var data = {
        "parameters": {},
        "context": null,
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
        "query" : $scope.query,
        "hasResult" : $scope.hasResult == "true"
    };

    $http.post(serverUrl + "/command", data).
        success(function(data, status, headers, config) {
            if(data.success){
                if($scope.hasResult == "true") {
                    if($scope.databaseType == "MONGODB"){
                        $scope.mongoOutput = true;
                        $scope.documents = [];

                        for(var i = 0; i < data.entity.rows.length; i++){
                            $scope.documents[$scope.documents.length] = JSON.stringify(data.entity.rows[i].attributes,null,"    ");
                        }
                    }
                    else {
                        $scope.mongoOutput = false;
                        //pass attributes names
                        $scope.attributes = data.entity.attributes;
                        $scope.rows = data.entity.rows;
                    }
                }
                else {
                    $scope.attributes = [{"attributeName" : "Status"}];
                    $scope.rows = [{"attributes" : {"Status" : "Success"}}]
                }

                $scope.history[$scope.history.length] = new historyItem("Query", true);
            } else {
                $scope.history[$scope.history.length] = new historyItem("Query", false);
                alert("Server error");
                console.log(data.errors)
            }
        }).
        error(function(data, status, headers, config) {
            alert("error")
            $scope.history[$scope.history.length] = new historyItem("Query", false);
        });
}