-Serializing JSON

Serializing an Object to JSON is easy. For any handler which should return JSON, mark it with
the annotation

@JSON

This will cause webhandle to transform the object to JSON and configure the headers
to their correct values.

Sometimes, though, different handlers will want to return different representations of
an object. For example, one url might result in the information for a node in a tree.
Another url might return the node and all of it's decedent nodes. To change which 
representation, add one or more serialization profiles to the JSON annoation like:

@JSON("deep")

or

@JSON("shallow")

or

@JSON({"deep", "full"})

Specifying profiles will cause webhandle to look for serializers in addition to the default set. 
First it will look for a "deep" serializer for the
object, then a "full" serializer, then use the default serializer.

Obviously the framework doesn't know a thing about specific objects or what "deep" or "full" would mean.
Any type of serialization other than including all simple properties (the "default") has to be written
for the application.

To write a custom serializer is also really easy. Just write a class which implements 
com.emergentideas.webhandle.json.ObjectSerializer and mark it with a @JSONSerializer annotation. Then
reference the class as part of the configuration. It will get integrated into the serializer framework.
Of course, all of the usual injection will happen, so you can use other services that have been loaded.

The easiest way to write a new JSON serializer may be to extend from JavaObjectSerializer. It does some
basic tasks with reflection in choosing the simple data members. Since many customizations will just
be adding complex objects to be serialized for a deeper look at an object or removing some parameters,
this class will probably already be doing 90+% of the work that needs to be done.

As you might expect, if two classes handling the same object types and profiles are registered, the later
one will be used in preference to the earlier. This means you can redefine how anything is handled by default.

--A Note on Binary and Date Formats

I couldn't find a standard for sending dates and binary information by JSON, so I implemented several profiles.
By default, dates will get sent like:

"propName": new Date("2013-11-15T23:23:06.841Z")

This preserves the type as much as is possible in Javascript. If you'd like a plain string instead of a date object,
you can specify the "date-as-string" profile like:

@JSON("date-as-string")

This will output dates that look like:

"2013-11-15T23:23:06.841Z"

I chose to transfer dates as strings instead of milliseconds since the epoch because it is both more human readable 
and expresses the infinite range of time values well. However, since some applications will want milliseconds, the
profile "date-as-millis" will do that.

Binary data is by default transferred as Base64 encoded text. I chose this because it is much more compact than an 
array of numbers and it is often the format that's desired if the binary data represents and image that will be
set in the src attribute or something similar. However, should the binary data need to be transferred as an array
of bytes, there's a profile "bytes-as-array".

--Multiple Profiles

Just to be explicit, multiple profiles can be included in the JSON annotation. If I wanted dates transferred as 
millis and binary as bytes, I could construct an annotation like:

@JSON({"bytes-as-array", "date-as-millis"})
