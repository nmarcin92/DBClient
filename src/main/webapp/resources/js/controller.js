var app = angular.module('dbClient', []);
app.controller('dbClientController', function($scope, $http) {
    //establishing connection
    $scope.connected = false;
    $scope.$on('$viewContentLoaded', getConnectionFromCookie($scope));
    $scope.setConnection = function() {setConnection($scope)};

    //history
    $scope.history = [];

    //CRUD
    $scope.performRead = function() { performRead($scope, $http)};
    $scope.performDelete = function() {performDelete($scope, $http)};

    //query
    $scope.runQuery = function() {runQuery($scope, $http)};
});
