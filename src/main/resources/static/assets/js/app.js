var mapboxURL = 'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoiY29ud3lqaW0iLCJhIjoiY2lrMjhodTNiMDJ5a3hla29jNDM5OGt6diJ9.Hnszg8GyCdZnzBJT3R0uFg';

var wheatpasteID = 'conwyjim.p1g507fk';
var piratesID = 'conwyjim.p1g4ia0m';
var pencilID ='conwyjim.p1defe37';
var normalID = 'lemoncrap.oph51mg4';


var newMarker = L.Icon.extend({
    options: {
        iconSize:     [25, 41],
        shadowSize:   [50, 64],
        popupAnchor:  [-0, -20]
    }
});

var greenMarker = new newMarker({iconUrl: '/geotagged/images/green.png'}),
redMarker = new newMarker({iconUrl: '/geotagged/images/red.png'});

var attributionText = 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
'photory © <a href="http://mapbox.com">Mapbox</a>';

var wheatpasteLayer = L.tileLayer(mapboxURL, {id: wheatpasteID, attribution: attributionText});
var piratesLayer = L.tileLayer(mapboxURL, {id: piratesID, attribution: attributionText});
var pencilLayer = L.tileLayer(mapboxURL, {id: pencilID, attribution: attributionText});
var normalLayer = L.tileLayer(mapboxURL, {id: normalID, attribution: attributionText});
var markers = new L.FeatureGroup();

var southWest = L.latLng(49.000000, 5.000000),
northEast = L.latLng(60.000000, -13.000000),
bounds = L.latLngBounds(southWest, northEast);

var map = L.map('map', {
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
	
var geotagModule = angular.module('geotagApp', []);


/*
	Create the controller for the angular js scope in which http get requests can be used easily.
*/
geotagModule.controller('geotagController', function ($scope,$http) {
	 var urlBase="";
	 var markers = [];
	 $http.defaults.headers.post["Content-Type"] = "application/json";
	 
		/*
			Get all the images from the url provided and plot the markers for all of them.
		*/
	 	function getDataAndPlot(url)
	 	{
	 		$http.get(urlBase + url).
            success(function (data) {
                $scope.photos = data._embedded.photos;
                    
                for (var i = 0; i < $scope.photos.length; i++) {
                        plotPoint($scope.photos[i],i);
                }
            });
	 	}
	 	
	 	/*
			Get all the events and add the events to the drop down boxes.
	 	 */
	 	function getEvents()
	 	{
	 		$http.get(urlBase + '/geotagged/events').
            success(function (data) {
                $scope.events = data._embedded.events;
                    
                for (var i = 0; i < $scope.events.length; i++) {
                        addOption($scope.events[i]);
                }
            });
	 	}
	    
	 	/*
			Plot the marker for the image provided.
	 	 */
	    function plotPoint(photo,id)
	    {
	    	var address = photo.address.replace(/ /g,'%20');
        	var desc = photo.desc;
        	var date = photo.date;
        	var event = photo.event;
        	var marker;
        	if(photo.status == 1)
        	{
        		marker = L.marker([photo.lat, photo.lon],{icon: greenMarker}).addTo(map);
        	}
        	else
        	{
        		marker = L.marker([photo.lat, photo.lon],{icon: redMarker}).addTo(map);
        	}
	    	var size = 150;
	    	marker.bindPopup("<figure><a href="+address+"><img src="+address+" height="+size+" width="+size+"/></a><br><figcaption>"+desc+"<br><br>"+event+"<br><br>"+date+"</figcaption></figure>");
	    	map.addLayer(marker);
	    	markers.push(marker);
	    }
	    
	    /*
			Loop through the list of added markers and remove them all.
	     */
	    function deleteMarkers() {
	    	
	    	for(i=0;i<markers.length;i++) {
	    	    	map.removeLayer(markers[i]);
	    	}
	    	markers =[];
	    }
	  	
	    /*
			When the sort button is clicked, the get the values from all the fields and sort based on them using the
			different url which is used for the custom repository queries
	    */
	    var sortButton = document.getElementById('sortButton');
	    sortButton.onclick = function(){ 
	    	
	    	deleteMarkers();
    		var date = document.getElementById("dateInput").value;
	    	var radios = document.getElementsByName('review');
	    	var e = document.getElementById("eventSort");
	    	var event = e.options[e.selectedIndex].text;
	    	
	    	if(Date.parse(date) == null)
	    	{
	    		var isDate = false;
	    	}
	    	else
	    	{
	    		var isDate = true;	
	    		date = Date.parse(date).toString("dd/MM/yyyy");
	    		
	    	}
	    	
	    	for (var i = 0, length = radios.length; i < length; i++) {
	    	    if (radios[i].checked) {
	    	        var status = (radios[i].value);
	    	        
	    	        break;
	    	    }
	    	}
	    	
	    	if(isDate && status != 0 && event == "All Events")
	    	{
		    	getDataAndPlot('/geotagged/photos/search/findByDateAndStatus?date='+date+'&status='+status);
	    	} 
	    	else if(!isDate && status != 0 && event == "All Events")
	    	{
	    		getDataAndPlot('/geotagged/photos/search/findByStatus?status='+status);
	    	} 
	    	else if(isDate && status == 0 && event == "All Events")
	    	{
	    		getDataAndPlot('/geotagged/photos/search/findByDate?date='+date);
	    	}
	    	else if(isDate && status != 0 && event != "All Events")
	    	{
	    		getDataAndPlot('/geotagged/photos/search/findByDateAndStatusAndEvent?date='+date+'&status='+status+'&event='+event);
	    	}
	    	else if(isDate && status == 0 && event != "All Events")
	    	{
	    		getDataAndPlot('/geotagged/photos/search/findByDateAndEvent?date='+date+'&event='+event);
	    	}
	    	else if(!isDate && status == 0 && event != "All Events")
	    	{
	    		getDataAndPlot('/geotagged/photos/search/findByEvent?event='+event);
	    	}
	    	else if(!isDate && status != 0 && event != "All Events")
	    	{
	    		getDataAndPlot('/geotagged/geotagged/photos/search/findByStatusAndEvent?status='+status+'&event='+event);
	    	}
	    	else
	    	{
	    		getDataAndPlot('/geotagged/photos');
	    	}
	    }

	    /*
			Add the event to all the drop down fields.
	     */
	    function addOption(event) {
	    	
	        var y = document.getElementById("eventSort");
	        var x = document.getElementById("eventSelection");
	        var optionX = document.createElement("option");
	        var optionY = document.createElement("option");
	        optionX.text = event.name;
	        optionY.text = event.name;
	        optionX.value = event.name;
	        optionY.value = event.name;
	        x.add(optionX);
	        y.add(optionY);
	    }
	    /*
			Plot the default markers.
	    */
	    getDataAndPlot('/geotagged/photos');
	    getEvents();
	    map.invalidateSize();
});