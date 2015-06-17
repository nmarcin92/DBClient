function performDelete($scope, $http){
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
        "preconditions": [$scope.deletePreconditions]
    };

    $http.post(serverUrl + "/delete", data).
        success(function(data, status, headers, config) {
            if(data.success){
                if($scope.databaseType == "MONGODB"){
                    $scope.mongoOutput = true;
                    $scope.documents = ["Success"];
                }
                else {
                    $scope.mongoOutput = false;
                    //pass attributes names
                    $scope.attributes = [{"attributeName": "Status"}];
                    $scope.rows = [{"attributes": {"Status": "Success"}}]
                }
                $scope.history[$scope.history.length] = new historyItem("Delete", true, $scope);
            } else {
                $scope.history[$scope.history.length] = new historyItem("Delete", false, $scope);
                alert("Server error");
                console.log(data.errors)
            }
        }).
        error(function(data, status, headers, config) {
            alert("error")
            $scope.history[$scope.history.length] = new historyItem("Delete", false, $scope);
        });
}