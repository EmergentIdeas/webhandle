Adding Errors in the Handler
============================

Adding errors in the handler is easy. Just add a parameter in the handle method signature 
with a type of RequestMessages like:

  public Object handle(Location location, RequestMessages messages)

Showing the Errors
==================

Of course, you can show these messages however you want. The RequestMessages object is available in
the location by the name "messages".

If you're using the oak standard templates 
(ivy dep <dependency org="com.emergentideas" name="oak_stdtemplates" rev="latest.integration" conf="appdep" /> )
you can just call a template to show all the errors like:

__::messages__

This template shows the messages as one div per message where the div has the classes 
alert and alert-error/alert-info etc. These classes are also used by Bootstrap to format
alert areas.
