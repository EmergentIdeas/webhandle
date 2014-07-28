Configuring Objects
===================

==@Resource
Any field which has a Resource annotation will be wired by the name and type specified in
the annotation or, if they are not specified, by the name and type of the field.

An method which has a Resource annotation will be called with the parameter assumed to be
the name specified in the Resource annotation or named according to the regular rules if
no name is specified in the annotation.

Any class which has a resource annotation will be added as a service with the name and type
specified in the annotation or the simple name of the class (first letter lower case) if no
name is specified and with a type of the type of the class if no type is specified.

==@Wire
Any method with a Wire annotation will be called and parameters used based on the normal
parameter naming investigators.

If a class has a wire method it is a shorthand way of putting the wire annotation on all
the setters of that class.


Handling Web Requests
=====================

==@Handle

@Handle is the webhandle specific annotation that let's you declare path, path variables
and request type

==@Inject

@Inject says that the request parameters should be injected into the object based on parameter
name.

==@NoInject

@NoInject when applied to a setter says that auto injection methods should not try to use the
setter.

==@NotNull

@NotNull says that if an instance of the object does not already exist, one should be created.

==@ClearBooleans

@ClearBooleans says that the boolean setters should be called so that boolean values can 
be cleared. This is done for objects with boolean members which correspond to check boxes
which will not transmit any value if they are not checked.

==@Db

@Db is used to load an object from the entity manager using a specified parameter from the
request as the id.

==J2EE Annotations

J2EE6 Annotations can also be used.
@Path will declare the path and path variables.  An example is: @Path("/to/resource/{id:\\d+}")

J2EE6 also uses the annotations @GET, @POST, @HEAD, @PUT, @DELETE, @OPTIONS.  Using one or more of
these on a method in combination with a @Path will restrict the request to only those HTTP methods.
If no HTTP method is defined, then all methods will be assumed to be acceptable.


Responding to Requests
======================

==@Template

Indicates that the response should be assumed to be a templated name that should be executed
with the location information

==@Wrap

Indicates that the template should be wrapped by another template unless the caller requests
that it not be wrapped but adding a request parameter

> response-wrapper=none

Including the page's chrome in this way allows plugin pieces of applications to reference the type
of chrome they'd like (app_page, public_page, etc.) without having to know anything about how those
chunks of chrome are constructed

==@ResponsePackage

Specifies how the output should be turned into a line level response. Currently, the only meaningful
value is

> body-only

This has the same effect as requesting with the parameter

> request-package=body-only

Either of these methods will keep the output from generating the html element, the head and body elements and their
associated interior code and just write the contents of the body to the line. This is needed for sending back ajax
html or json.