// flickrAPI.js
// Programmers: Nathan Vanos, Mason Dellutri, Joshua Go
'use strict';

    function JavaScriptFetch() {
        var url = document.createElement('script');
        url.src = "https://api.flickr.com/services/feeds/photos_public.gne?format=json&tags=" + document.getElementById("searchFlickr").value;
        document.querySelector('head').appendChild(url);
    }

    function jsonFlickrFeed(data) {
        var imageURL = "";
        var imagesPerRow = 0;
        
        imageURL += "<div class='container'>";
        data.items.forEach(function (element) {
            if (imagesPerRow === 0) {
                imageURL += "<div class='row justify-content-center'>";
            }
            imageURL += "<div class='col-md'>";
            imageURL += "<a href='comments_page.html'>";
            imageURL += "<img src=\"" + element.media.m + "\" />";
            imageURL += "</a>";
            imageURL += "<div id='example'></div>";
            imageURL += "</div>";
            imagesPerRow++;
            if (imagesPerRow === 4) {
                imageURL += "</div>";
                imagesPerRow = 0;
            }
        });
        imageURL += "</div>"
    document.getElementById("searchResults").innerHTML = imageURL;
    }