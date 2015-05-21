
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
        "entityName": "nowa_tabela",
        "userSession": {
            "connectionType": "POSTGRESQL",
            "dbCredentials": {
                "username": "postgres",
                "password": "postgres",
                "url": "localhost:5432",
                "databaseName": "postgres"
            }
        },
        "toAdd": [],
        "toDelete": [],
        "toModify": [],
        "id": $scope.rowId,
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
                //pass attributes names
                $scope.attributes = [{"attributeName" : "Status"}];
                $scope.rows = [{"attributes" : {"Status" : "Success"}}]

                $scope.history[$scope.history.length] = new historyItem("Create", true);
            } else {
                $scope.history[$scope.history.length] = new historyItem("Create", false);
                alert("Server error");
                console.log(data.errors)
            }
        }).
        error(function(data, status, headers, config) {
            alert("error")
            $scope.history[$scope.history.length] = new historyItem("Create", false);
        });
}