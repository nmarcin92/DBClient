function historyItem(type, success, $scope){
    this.type = type;
    this.success = success;
    this.scope = $scope;

    var savedFields = {
        tableName: $scope.tableName,
        createQueryAttributes: [],

        columnNames: $scope.columnNames,
        updatePreconditions : $scope.updatePreconditions,
        updateQueryAttributes: [],

        deletePreconditions: $scope.deletePreconditions,

        query: $scope.query,
        hasResult: $scope.hasResult
    };

    for(var i = 0; i < $scope.createQueryAttributes.length; i++){
        savedFields.createQueryAttributes[i] = JSON.parse(JSON.stringify($scope.createQueryAttributes[i]));
    }

    for(var i = 0; i < $scope.updateQueryAttributes.length; i++){
        savedFields.updateQueryAttributes[i] = JSON.parse(JSON.stringify($scope.updateQueryAttributes[i]));
    }

    this.name = type + ": " + $scope.tableName;

    this.restore = function(){
        $scope.tableName = savedFields.tableName;
        $scope.createQueryAttributes = savedFields.createQueryAttributes;
        $scope.columnNames = savedFields.columnNames;
        $scope.updatePreconditions = savedFields.updatePreconditions;
        $scope.updateQueryAttributes = savedFields.updateQueryAttributes;
        $scope.deletePreconditions = savedFields.deletePreconditions;
        $scope.query = savedFields.query;
        $scope.hasResult = savedFields.hasResult;
    };
}
