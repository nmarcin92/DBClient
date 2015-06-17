//displaying selected table
function displayTable($scope, $http){
    if($scope.databaseType == "MONGODB") return;
    var data = {
        "parameters": {},
        "context": "ENTITY",
        "entityName": $scope.schemaTableName,
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

    $http.post(serverUrl + "/read", data).
        success(function(data, status, headers, config) {
            if(data.success){
                $scope.attributes = data.entity.attributes;
            } else {
                alert("Server error");
                console.log(data.errors)
            }
        }).
        error(function(data, status, headers, config) {
            alert("error")
        });
}


//adding new attribute to entity
function addEnityAttribute($scope, $http){
    var data = {
        "parameters": {},
        "context": "ENTITY",
        "entityName": $scope.schemaTableName,
        "userSession": {
            "connectionType": $scope.databaseType,
            "dbCredentials": {
                "username": $scope.userName,
                "password": $scope.password,
                "url": $scope.hostName + ":" + $scope.port,
                "databaseName": $scope.databaseName
            }
        },
        "toAdd": [
            {
                "attributeName": $scope.newAttributeName,
                "dataType": $scope.newAttributeType
            }
        ],
        "toDelete": [],
        "toModify": [],
        "id": null,
        "updated": null
    };

    $http.post(serverUrl + "/update", data).
        success(function(data, status, headers, config) {
            if(data.success){
            } else {
                alert("Server error");
                console.log(data.errors)
            }
        }).
        error(function(data, status, headers, config) {
            alert("error")
        });

    displayTable($scope, $http);
}

//deleting an attribute
function deleteEnityAttribute($scope, $http, attributeName){
    var data = {
        "parameters": {},
        "context": "ENTITY",
        "entityName": $scope.schemaTableName,
        "userSession": {
            "connectionType": $scope.databaseType,
            "dbCredentials": {
                "username": $scope.userName,
                "password": $scope.password,
                "url": $scope.hostName + ":" + $scope.port,
                "databaseName": $scope.databaseName
            }
        },
        "toDelete": [
            {
                "attributeName": attributeName,
                "dataType": null
            }
        ],
        "toAdd": [],
        "toModify": [],
        "id": null,
        "updated": null
    };

    $http.post(serverUrl + "/update", data).
        success(function(data, status, headers, config) {
            if(data.success){
            } else {
                alert("Server error");
                console.log(data.errors)
            }
        }).
        error(function(data, status, headers, config) {
            alert("error")
        });

    displayTable($scope, $http);
}

//updating a column
function updateEntityAttribute($scope, $http, attributeName){

    var data = {
        "parameters": {},
        "context": "ENTITY",
        "entityName": $scope.schemaTableName,

        "toDelete": [],
        "toAdd": [],
        "toModify": [{
            "attributeName": attributeName,
            "dataType": $scope.newUpdatedAttributeType
        }],
        "toRename": [{
            "oldName": attributeName,
            "newName": $scope.newUpdatedAttributeName
        }],
        "updated": null,

        "userSession": {
            "connectionType": $scope.databaseType,
            "dbCredentials": {
                "username": $scope.userName,
                "password": $scope.password,
                "url": $scope.hostName + ":" + $scope.port,
                "databaseName": $scope.databaseName
            }
        }

    };


    $http.post(serverUrl + "/update", data).
        success(function(data, status, headers, config) {
            if(data.success){
                $scope.updatedAttributeName = undefined;
            } else {
                alert("Server error");
                console.log(data.errors)
            }
        }).
        error(function(data, status, headers, config) {
            alert("error")
        });

    displayTable($scope, $http);
}

//getting list of all tables
function getEntities($scope, $http){
    if(!$scope.connected) return;
    var data = {
        "parameters": {},
        "context": "DATABASE",
        "entityName": "*",
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

    $http.post(serverUrl + "/read", data).
        success(function(data, status, headers, config) {
            if(data.success){
                $scope.entities = data.entity.attributes;
            } else {
                alert("Server error");
                console.log(data.errors)
            }
        }).
        error(function(data, status, headers, config) {
            alert("error")
        });
}

//create a new table
function createEntity($scope, $http){
    var data = {
        "parameters": {},
        "context": "ENTITY",
        "entityName": $scope.newEntityName,
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

    $http.post(serverUrl + "/create", data).
        success(function(data, status, headers, config) {
            if(data.success){
                alert("Created new entity: " + $scope.newEntityName)
            } else {
                alert("Server error");
                console.log(data.errors)
            }
        }).
        error(function(data, status, headers, config) {
            alert("error")
        });

    getEntities($scope, $http);
}


//delete a table
function deleteEntity($scope, $http){
    var data = {
        "parameters": {},
        "context": "ENTITY",
        "entityName": $scope.schemaTableName,
        "userSession": {
            "connectionType": $scope.databaseType,
            "dbCredentials": {
                "username": $scope.userName,
                "password": $scope.password,
                "url": $scope.hostName + ":" + $scope.port,
                "databaseName": $scope.databaseName
            }
        },
        "preconditions": []
    };


    $http.post(serverUrl + "/delete", data).
        success(function(data, status, headers, config) {
            if(data.success){
                alert("Deleted entity: " + $scope.schemaTableName)
            } else {
                alert("Server error");
                console.log(data.errors)
            }
        }).
        error(function(data, status, headers, config) {
            alert("error")
        });

    getEntities($scope, $http);
}