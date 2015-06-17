
function addCreateAttribute($scope){
    $scope.createQueryAttributes[$scope.createQueryAttributes.length] = new attributeField($scope.createQueryAttributes.length);
}

function removeCreateAttribute($scope){
    $scope.createQueryAttributes.splice($scope.createQueryAttributes.length - 1, 1);
}

function performCreate($scope, $http){
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
        "attributes": {}
    };

    //add attributes
    for(var i = 0; i < $scope.createQueryAttributes.length; i++){
        var value = $scope.createQueryAttributes[i].value;
        data.attributes[$scope.createQueryAttributes[i].name] = $scope.createQueryAttributes[i].value;
    }

    $http.post(serverUrl + "/create", data).
        success(function(data, status, headers, config) {
            if(data.success){
                if($scope.databaseType == "MONGODB"){
                    $scope.mongoOutput = true;
                    $scope.documents = ["Success"];
                }
                else {
                    $scope.mongoOutput = false;

                    //pass attributes names
                    $scope.attributes = [{"attributeName" : "Status"}];
                    $scope.rows = [{"attributes" : {"Status" : "Success"}}]

                }
                $scope.history[$scope.history.length] = new historyItem("Create", true, $scope);
            } else {
                $scope.history[$scope.history.length] = new historyItem("Create", false, $scope);
                alert("Server error");
                console.log(data.errors)
            }
        }).
        error(function(data, status, headers, config) {
            alert("error")
            $scope.history[$scope.history.length] = new historyItem("Create", false, $scope);
        });
}