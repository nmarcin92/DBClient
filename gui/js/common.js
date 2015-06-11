var serverUrl = "http://172.17.84.87:8080/DBClient";

function attributeField(id){
    this.id = id;
    this.name = "";
    this.value = "";
}


var app = angular.module('dbClient', []);
app.controller('dbClientController', function($scope, $httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    $httpProvider.defaults.withCredentials = true;
    delete $httpProvider.defaults.headers.common["X-Requested-With"];
    $httpProvider.defaults.headers.common["Accept"] = "application/json";
    $httpProvider.defaults.headers.common["Content-Type"] = "application/json";
});