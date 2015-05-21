var app = angular.module('dbClient', []);
app.controller('dbClientController', function($scope, $http) {
    //establishing connection
    $scope.connected = false;
    $scope.$on('$viewContentLoaded', getConnectionFromCookie($scope));
    $scope.setConnection = function() {setConnection($scope)};

    //history
    $scope.history = [];

    //CRUD
    $scope.createQueryAttributes = [new attributeField(0)];
    $scope.addCreateAttribute = function(){addCreateAttribute($scope)};
    $scope.removeCreateAttribute = function(){removeCreateAttribute($scope)};
    $scope.performCreate = function() {performCreate($scope, $http)};


    $scope.updateQueryAttributes = [new attributeField(0)];
    $scope.addUpdateAttribute = function(){addUpdateAttribute($scope)};
    $scope.removeUpdateAttribute = function(){removeUpdateAttribute($scope)};
    $scope.performUpdate = function() {performUpdate($scope, $http)};

    $scope.performRead = function() { performRead($scope, $http)};
    $scope.performDelete = function() {performDelete($scope, $http)};

    //query
    $scope.runQuery = function() {runQuery($scope, $http)};
});
