angular.module('challenge', [])
    .controller('playerCtl', function ($scope, $http) {
        $http.get('/player?search=a').
            then(function (response) {
                $scope.players = response.data;
                //console.log(response.data);
                //$scope.player = response.data[0];
                //console.log($scope.player);
            });
    });