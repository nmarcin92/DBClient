/**
 * Created by Marcin Nowak on 2015-05-02.
 */
angular.module('DBClient.services', []).
    factory('readService', function ($http) {

        var readService = {};

        readService.performRead = function (readOp) {
            return $http({
                method: 'POST',
                body: readOp,
                url: 'http://127.0.0.1:8080/DBClient/read'
            });
        }

        return readService;
    });