# Dallas-Police-Calls
Fetches the city's active call information and plots it to a static Google map.
<br><br>
Running with argument -h will provide you a full list of options.
<br><br>
-map must be run from the console after data has downloaded. This may not be fixed as I'd like to build a proper GUI for the data once I figure out how I want to be able to sort it.
<br><br>
Map markers are color coded by priority as assigned by the police. Red(1) is most urgent grading down to Blue(5). Markers are labeled A-O and will correspond with the listed calls. The rest of the calls won't show on the map due to geocoding restrictions on Google Maps API until I fix my code.
<br><br><br>
Contains information from Dallas OpenData which is made available under the ODC Attribution License.
https://www.dallasopendata.com
