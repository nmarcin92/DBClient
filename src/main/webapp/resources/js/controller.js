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

    //schema display
    $scope.displaySchema = false;
    $scope.setDisplaySchema = function(displaySchema) {$scope.displaySchema = displaySchema };

    //schema
    $scope.displayTable = function() {displayTable($scope, $http)};
    $scope.addEnityAttribute = function(newAttributeName, newAttributeType) {
        $scope.newAttributeName = newAttributeName;
        $scope.newAttributeType = newAttributeType;
        addEnityAttribute($scope, $http)
    };
    $scope.deleteEnityAttribute = function (attributeName) {deleteEnityAttribute($scope, $http, attributeName)};

    $scope.startAttributeUpdate = function(attributeName, dataType){
        $scope.updatedAttributeName = attributeName;
        $scope.newUpdatedAttributeName = attributeName;
        $scope.newUpdatedAttributeType = dataType;
    };

    $scope.cancelAttributeUpdate = function(attributeName){
        $scope.updatedAttributeName = undefined;
    };

    $scope.updateEntityAttribute = function(attributeName, newUpdatedAttributeName, newUpdatedAttributeType){
        $scope.newUpdatedAttributeName = newUpdatedAttributeName;
        $scope.newUpdatedAttributeType = newUpdatedAttributeType;
        updateEntityAttribute($scope, $http, attributeName);
    };

    $scope.getEntities = function(){getEntities($scope, $http)};
});
