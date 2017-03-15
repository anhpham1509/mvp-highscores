var app = angular.module('challenge', []);

app.controller('registerCtl', ['$scope', '$http', function ($scope, $http) {
    $scope.register = function () {
        if ($scope.playerName) {
            var req = {
                method: 'POST',
                url: '/register',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: {playerName: $scope.playerName}
            };

            $http(req).then(function (response) {
                $scope.code = response.status;
                $scope.content = response.data;
            }, function (response) {
                $scope.code = response.status;
                $scope.content = response.data;
            });
        }
    };
}]);

app.controller('scoreCtl', ['$scope', '$http', function ($scope, $http) {
    $scope.submit = function () {
        if ($scope.submitPlayerId && $scope.submitGameTitle && $scope.submitScore) {
            var req = {
                method: 'POST',
                url: '/score',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: {
                    playerId: $scope.submitPlayerId,
                    gameTitle: $scope.submitGameTitle,
                    score: $scope.submitScore
                }
            };

            $http(req).then(function (response) {
                $scope.submitCode = response.status;
                $scope.submitContent = response.data;
            }, function (response) {
                $scope.submitCode = response.status;
                $scope.submitContent = response.data;
            });
        }
    };

    $scope.delete = function () {
        if ($scope.deletePlayerId && $scope.deleteGameTitle) {
            var req = {
                method: 'DELETE',
                url: '/score',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: {
                    playerId: $scope.deletePlayerId,
                    gameTitle: $scope.deleteGameTitle
                }
            };

            $http(req).then(function (response) {
                $scope.deleteCode = response.status;
                $scope.deleteContent = response.data;
            }, function (response) {
                $scope.deleteCode = response.status;
                $scope.deleteContent = response.data;
            });
        }
    };

    $scope.getByGame = function () {
        if ($scope.getGameTitle) {
            var req = {
                method: 'GET',
                url: '/score/game/' + $scope.getGameTitle
            };

            $http(req).then(function (response) {
                $scope.gameCode = response.status;
                $scope.gameScores = response.data;
            }, function (response) {
                $scope.gameCode = response.status;
                $scope.gameScores = response.data;
            });
        }
    };

    $scope.getByPlayer = function () {
        if ($scope.getPlayerId) {
            var req = {
                method: 'GET',
                url: '/score/player/' + $scope.getPlayerId
            };

            $http(req).then(function (response) {
                $scope.playerCode = response.status;
                $scope.playerScores = response.data;
            }, function (response) {
                $scope.playerCode = response.status;
                $scope.playerScores = response.data;
            });
        }
    };
}]);

app.controller('searchCtl', ['$scope', '$http', function ($scope, $http) {
    $scope.searchGame = function () {
        if ($scope.searchGameTitle) {
            var req = {
                method: 'GET',
                url: '/game?search=' + $scope.searchGameTitle
            };

            $http(req).then(function (response) {
                $scope.searchGameCode = response.status;
                $scope.searchGameContent = response.data;
            }, function (response) {
                $scope.searchGameCode = response.status;
                $scope.searchGameContent = response.data;
            });
        }
    };

    $scope.searchPlayer = function () {
        if ($scope.searchPlayerName) {
            var req = {
                method: 'GET',
                url: '/player?search=' + $scope.searchPlayerName
            };

            $http(req).then(function (response) {
                $scope.searchPlayersCode = response.status;
                $scope.searchPlayersContent = response.data;
            }, function (response) {
                $scope.searchPlayersCode = response.status;
                $scope.searchPlayersContent = response.data;
            });
        }
    };
}]);