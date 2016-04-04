var map;
var markers = [];

function initialize() {
  var haightAshbury = new google.maps.LatLng(37.7699298, -122.4469157);
  var mapOptions = {
    zoom: 12,
    center: haightAshbury,
    mapTypeId: google.maps.MapTypeId.TERRAIN
  };
  map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);

  google.maps.event.addListener(map, 'click', function(event) {
    addMarker(event.latLng);
  });
}

// Add a marker to the map and push to the array.
function addMarker(location) {
  var marker = new google.maps.Marker({
    position: location,
    map: map
  });
  markers.push(marker);
}

// Sets the map on all markers in the array.
function setAllMap(map) {
  for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(map);
  }
}

// Removes the overlays from the map, but keeps them in the array.
function clearOverlays() {
  setAllMap(null);
}

// Shows any overlays currently in the array.
function showOverlays() {
  setAllMap(map);
}

// Deletes all markers in the array by removing references to them.
function deleteOverlays() {
  clearOverlays();
  markers = [];
}

google.maps.event.addDomListener(window, 'load', initialize);