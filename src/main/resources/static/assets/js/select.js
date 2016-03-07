/*
	Create the map, using the mapbox url and other attributes, set the centre of the map, the zoom and the bounds.
*/

var mapboxURL = 'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoiY29ud3lqaW0iLCJhIjoiY2lrMjhodTNiMDJ5a3hla29jNDM5OGt6diJ9.Hnszg8GyCdZnzBJT3R0uFg';

var wheatpasteID = 'conwyjim.p1g507fk';
var piratesID = 'conwyjim.p1g4ia0m';
var pencilID ='conwyjim.p1defe37';
var normalID = 'lemoncrap.oph51mg4';

var attributionText = 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
'photory © <a href="http://mapbox.com">Mapbox</a>';

var wheatpasteLayer = L.tileLayer(mapboxURL, {id: wheatpasteID, attribution: attributionText});
var piratesLayer = L.tileLayer(mapboxURL, {id: piratesID, attribution: attributionText});
var pencilLayer = L.tileLayer(mapboxURL, {id: pencilID, attribution: attributionText});
var normalLayer = L.tileLayer(mapboxURL, {id: normalID, attribution: attributionText});

var southWest = L.latLng(49.000000, 5.000000),
northEast = L.latLng(60.000000, -13.000000),
bounds = L.latLngBounds(southWest, northEast);

var map = L.map('selectMap', {
	center: [52.589701, -3.224487],
	zoom: 7,
	minZoom: 8,
	maxBounds: bounds,
	layers: [normalLayer]
});

var baseMaps = {
	"Wheatpaste": wheatpasteLayer,
	"Pirates": piratesLayer,
	"Pencil": pencilLayer,
	"Normal": normalLayer
};

L.control.layers(baseMaps).addTo(map);

/*
	Create the controller for the angular js scope in which http get requests can be used easily.
*/
var geotagModule = angular.module('geotagApp', []);

geotagModule.controller('geotagController', function ($scope,$http) {
	 $http.defaults.headers.post["Content-Type"] = "application/json";
	  	
	 
	/*
		Get all the events from the database, as the repository maps these to urls you can get the events from /events
	*/ 
	function getEvents()
 	{
		// On success loop through the events and add them as options to the drop downs.
 		$http.get('/geotagged/events').
     success(function (data) {
         $scope.events = data._embedded.events;
             
         for (var i = 0; i < $scope.events.length; i++) {
                 addOption($scope.events[i]);
         }
     });
 	}
	 
	 /*
		Set the lat and long of the inputfields on map click
	 */
	 map.on('click', function(e) {
		    var position = e.latlng;
		    var lat = position.lat;
		    var lng = position.lng;
		    var latInput = document.getElementById('lat');
		    var lngInput = document.getElementById('lng');
		    latInput.value = lat;
		    lngInput.value = lng;
		});
	 
	 /*
		function to add the options to the drop down for the events.
	 */
	 function addOption(event) {
	    	
        var x = document.getElementById("eventSelection");
        var optionX = document.createElement("option");
        optionX.text = event.name;
        optionX.value = event.name;
        x.add(optionX);
    }
	
	getEvents();
	map.invalidateSize();
});