status
------
the http status code (really)

httpHeader
---------
http header name value pairs

docType
-------
doctype spec, allows a template to set the doctype

htmlTag
-------
html tag, allows a template to set the html tag for language or whatever else it wants


htmlHeader
----------
html header, will probably be added before all of the css includes and libraries but after
any sort of meta or http-equiv tags

title
-----
the title tag for the page

cssIncludes
-----------
css files that should be included as part of the header
the way this works is the url is the key and any value is assumed
to be a media query

headerLibraries
---------------
javascript files that should be included as part of the header

headerScript
------------
javascript code that should be included as part of the header, will
probably come after the javascript includes

headerStyle
-----------
styles that are not included in stylesheets and are specific to this page
this is a way to get a selected menu item or or section to high light

bodyOpen
--------
allows the template to set the body element in case it needs a class declaration

bodyPre
-------
allows a template or handler to insert some elements before the primary body starts

body
----
the main content of the page

footerLibraries
---------------
javascript files that should be included at the very bottom of the body
this is the case for something like JQuery that can be loaded after the
page content and is being used improve the functionality

footerScript
------------
javascript code that should be included after the footer libraries to enhance
the functionality of the page

bodyPost
--------
allows a template or handler to insert some elements before the primary body stops

docClose
--------
the closing body and html tags


render order
------------
the order in which the template should render the regions.  Mostly it won't matter,
but it will be better to get all of an outer template's rendering done before 
we hit the inner one in the body
This has nothing to do with the order they are used in the final document.  That is 
determined by a class like HtmlDocRespondent
status,httpHeader,docType,htmlTag,htmlHeader,title,cssIncludes,headerLibraries,headerScript,headerStyle,bodyOpen,bodyPre,body,footerLibraries,footerScript,bodyPost,docClose

