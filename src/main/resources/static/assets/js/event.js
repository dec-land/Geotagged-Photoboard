var geotagModule = angular.module('geotagApp', []);

geotagModule.controller('geotagController', function ($scope,$http) {
	 var urlBase="";
	 $scope.events = [];
	 $http.defaults.headers.post["Content-Type"] = "application/json";
	 
	 /*
		Get the events and store them in the $scope, this can then be used inside the thymleaf template to automatically update
		the event table.
	 */
 	 function getEventsList()
 	 {
 		$http.get(urlBase + '/geotagged/events').
        success(function (data) {
        	$scope.events = data._embedded.events;
        });
 	 }
 	 getEventsList();
});