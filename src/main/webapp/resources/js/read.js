var app = angular.module('dbClient', []);
app.controller('dbClientController', function ($scope, $http) {

    $scope.performRead = function () {
        var data = {
            "parameters": {"ENTIRE_RECORD": null},
            "context": "RECORD",
            "entityName": $scope.tableName,
            "userSession": {
                "connectionType": "POSTGRESQL",
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
            success(function (data, status, headers, config) {
                if (data.success) {
                    //pass attributes names
                    $scope.attributes = data.entity.attributes;
                    $scope.rows = data.entity.rows;
                } else {
                    alert("Server error");
                    console.log(data.errors)
                }
            }).
            error(function (data, status, headers, config) {
                alert("error")
            });
    };
});

function performRead() {

}