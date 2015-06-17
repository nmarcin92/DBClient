
function addUpdateAttribute($scope){
    $scope.updateQueryAttributes[$scope.updateQueryAttributes.length] = new attributeField($scope.updateQueryAttributes.length);
}

function removeUpdateAttribute($scope){
    $scope.updateQueryAttributes.splice($scope.updateQueryAttributes.length - 1, 1);
}

function performUpdate($scope, $http){
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
        "toAdd": [],
        "toDelete": [],
        "toModify": [],
        "preconditions": [$scope.updatePreconditions],
        "updated": {
            "attributes": {}
        }
    };

    //add attributes
    for(var i = 0; i < $scope.updateQueryAttributes.length; i++){
        var value = $scope.updateQueryAttributes[i].value;
        data.updated.attributes[$scope.updateQueryAttributes[i].name] = $scope.updateQueryAttributes[i].value;
    }

    $http.post(serverUrl + "/update", data).
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

                $scope.history[$scope.history.length] = new historyItem("Update", true, $scope);
            } else {
                $scope.history[$scope.history.length] = new historyItem("Update", false, $scope);
                alert("Server error");
                console.log(data.errors)
            }
        }).
        error(function(data, status, headers, config) {
            alert("error")
            $scope.history[$scope.history.length] = new historyItem("Update", false, $scope);
        });
}