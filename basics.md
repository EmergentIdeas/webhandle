Basics of Building a Simple Web App
===================================

Web apps are made up of:

1. Request Handlers - They DO something based on an HTTP operation (GET/POST/PUT request)
2. Templates - They show something, possibly including other templates
3. Infrastructure - Provides a pedestal on which your awesomeness can stand

Webhandle apps are defined by a .conf file.  You can read the [setup](setup.md) guide for a complete picture.  As a sketch, the configuration file
has individual lines that cause the webhandle infrastructure to:

1. Break that line into its informational parts
2. Try to create an object based on the configuration line
3. Try to integrate the configuration into the rest of the application


Infrastructure
==============

The infrastructure I've tried to make simple, helpful, and very extensible.  I wanted my infrastructure to be like a programming buddy helping
me out.  But a buddy that didn't mind doing a log of crap work.  Although it's probably unnecessary to start with, it's trivailly easy to extend 
the infrastructure.  You can see how at [Extended Config](extendedConfiguration.md).

I tell you this right off because I think it will help you guess how things work.  I've always been frustrated by tools that make me either specify
everything (no defaults) or have defaults that are only appropriate to 1% of its most advanced users.  For this webhandle, when there was a
choice to be made about how something should work, the guide was, "What would a reasonably sensible programming buddy try to do to make your life easier."


Handlers
========

Handler Annotations
-------------------

To add a new handler, all you have to do is add its fully qualified class name to the path like:

<blockquote>
com.emergentideas.webhandle.LoginHanlder
</blockquote>

In the class itself, one or more of your methods should have a Handle annotation like

>	@Handle("/login")
>	public String login(String userName, String password, String passwordConfirm) {

This method will now get called for all urls in your application (assuming you've deployed without a context root) that are

>	www.domain.com/login

If you only wanted to respond to GET requests, you could have used

>	@Handle(value = "/login", method = HttpMethod.GET)


Let's say now you've got a method that will process a login for a user where the name is in the url.  You can do that by:

>	@Handle("/login/{userId}")

Let's say the user id has only digits and if there are characters there, this method is not for them.  That annotation would look like:

>	@Handle("/login/{userId:\d*}")

You can see the colon and the regular expression following.  And of course, you can put multiple parameters in the url.

>	@Handle("/login/{userId:\d*}/info/{whatInfo}")

Now, let's say you're writing your obligatory todo app and you've got an hanlder for lists.  You could specify the prefix on every method like:

>	@Handle("/lists/all")
>	@Handle("/lists/{listNumber:\d*")

However, what you could do is add a Handle annotation to the class itself:

>	@Handle("/lists")
public class ListsHandler {

and then an annotation to method which will make up the second part of the url

>	@Handle("/all")
public Object all(...

Btw, feel free to stick parameters in the annotation on the class as well.


Hanlder Methods
---------------

The first time I saw Spring's auto-populated handler arguments, I thought to myself, "I was blind but light has let me see.  I will never use the
request object again."  Okay, maybe it wasn't quite that strong, but one thing I'd always hated about handling requests in Java was the need to get
from the request object everything you were interested as a String and manually process it.  Writing code to convert and process parameters is a crock
of shit, that's why I wrote my infrastructure buddy to do it for me.  In addition to Spring's already cool model, I've added some features that make
it easier to do data cleansing on input.

The basic principle here is to by default do the obvious thing I want it to do 95% of the time.  In those 4% of thecases where it doesn't, there are
some annotations to get the infrastructure to do more work for you.  In the other 1%, you can always get the request object :-)  

Handler methods are just normal public methods with a Handle annotation that defines when it gets invoked.  It takes parameters that come from one of
several sources.  The most obvious of these are the request parameters and parameters defined by the url.  There are also parameters that come from the
infrastructure, the session, the J2EE env, etc.  For every argument in the method, webhandle does its best to find you a value.

### Getting them Called

When you define a handler like this:

>	@Handle(value = "/login", method = HttpMethod.POST)
>	public String login(String userName, String password, String passwordConfirm) {

### Providing them with Data

Webhandle tries to figure out the names and types of those parameters.  As long as your code is built with debugging information it will probably decide
that the name is the variable name.  If not, or you want your data to come from someplace else, or the name isn't a legal Java identifier you can add a 
name annotation.

>	public String login(@Name("the-user-name") String userName, String password, String passwordConfirm) {

Webhandle will now try to find a value named "theUserName" to drop into that variable.

You can also dictate which sources are allowed to contribute values with the @Source annotation. This is part of the 4%, so I'll not say anything more about
for now.

Type conversion is automatic so something like the following should work great.

>	public String subscribeToBirthdayCard(String name, Date birthday, boolean receiveUpdates) {

This assumes submission from a form like:

<blockquote>
	Name: <input type="text" name="name"/>
	Birthdate: <input type="text" name="birthday" />
	Also receive updates: <input type="checkbox" name="receiveUpdates" />
</blockquote>

"name"'s behavior is obvious.  "receiveUpdates" tries to convert whatever parameter has or has not been submitted to a boolean in pretty much the way you'd expect.
"birthday" is going to cause webhandle to try really, really hard to come up with a date based on the input.  If you're using a dojo or jQuery date picker, this is
going to be no problem.  If you're using free text input, it will be right a lot of the time.

Another popular pattern is to inject all the parameters into a command object.  In webhandle, this behavior looks like

>	public String subscribeToBirthdayCard(@NotNull @Command SubscriptionInfo info) {

There are two annotations there that do two different things.  @Command tries to do the injection.  @NotNull says it's okay to create a brand new object of type 
SubscriptionInfo if one doesn't exist.  If you wanted to have a sub class of SubscriptionInfo created instead of a SubscriptionInfo object, you do it like:

>	public String subscribeToBirthdayCard(@NotNull(BirthdaySubscriptionInfo.class) @Command SubscriptionInfo info) {

In either case, the class being created must have a no arguments constructor.

A derivative of this pattern, and I think even more popular, is to inject all the properties into an object loaded by the database.  To do that, mark it with the @Db
annotation like:

>	public String subscribeToBirthdayCard(@Db("id") @NotNull(BirthdaySubscriptionInfo.class) @Command SubscriptionInfo info) {

The value of the Db annotation is the name of the parameter that specifies the unique object id of the object as stored in the database.  By default, webhandles has a
JPA environment about 95% configured for you.  There are a couple steps you need to do to tell it how to connect to your database that you can read about. 
[Add Database Access](dbConf.md)

One cool/terrifying thing about @Command is that it will try to inject database loaded objects into the command object.  So, let's say you have an object like:

<blockquote>
public class BirthdaySubscriptionInfo {
	String name;
	Date birthday;
	boolean receiveUpdates;
	BirthdayCard birthdayCard;

	...

	public void setBirthdayCard(BirthdayCard birthdayCard);
</blockquote>

If the request has a parameter named "birthdayCard" that has a value like 5, @Command will try to lookup an object of type BirthdayCard with id 5.  This either makes it 
very easy to present screens that allows users to link objects together or is a security nightmare.  You can always prevent @Command from injecting objects by adding a
@NoInject annotation to the method like:

>	@NoInject
>	public void setBirthdayCard(BirthdayCard birthdayCard);

Also, look for the the new annotation comming soon, @Inject, which does not have this db object injection behavior.

#### Environment Objects

Getting a hold of an environment object is also simple.  To get the request, just:

>	public String login(String userName, String password, String passwordConfirm, HttpServletRequest request) {

It usually doesn't matter what you name these since webhandle is using the type to find them.  All of the typical J2EE objects are available as well as a bunch of webhandle
objects that you'll probably get more use out of.

##### Location
The most important of these is Location.  Location is like a bunch of hash maps stacked on top of one another.  The location you get is one specifically created for this request.
However, it is stacked on top of the location for the user's session and the location for the application.  A call to get will look through the entire stack.  A call to change
will modify only the top level.

There are a bunch of cool things you can do with this setup.  For instance, you can add data at the app level that is available for all your requests and templates to reference.  
Another is that you can redefine objects that act as part of the infrastructure (you can give each user their own different set of templates if you want).  For the moment though
think of it as a hash map that is also a file tree.

Although it has a hash an interface like a hash map, it behaves a little like a file tree as well because it will traverse the objects it finds to get the data you want. So, if you
have code like this:

>	location.add(new BirthdaySubscriptionInfo("Dan Kolz", myBirday));

Calling 

>	location.get("name")

Will return "Dan Kolz".  In this case, it has found the object added at the root, the birthday subscription, and accessed the name getter method.

Alternatively, you could have added the object like:

>	location.put("subscription", new BirthdaySubscriptionInfo("Dan Kolz", myBirday));

You could then call:

>	location.get("subscription/name")

In this case, it found the object with the key "subscription" and then used the "name" getter.  One important thing about using the getter is that the getter need not actually 
provide access to a member variable.  I could add a method called "getLastName" to the subscription class which tries to parse out the last name.  In that case, a call using the 
path "subscription/lastName" would invoke that method and return the data.

This isn't very different from how the JSP EL (Evaluation Language) works.  In webhandle I've just exposed it as part of the handling process and given it some steroids.

##### Segmented Output

The segmented output object is also major.  Basically, it holds the information that will be given to the user, segmented by type.  This allows handlers and templates to contribute to
any part of the final document AT ANY TIME.  This isn't always necessary, but I wanted my infrastructure buddy to help with a problem you can see by going to 
[w3schools meta tag page](http://www.w3schools.com/tags/tag_meta.asp).  If you view the source on a page ENTIRELY devoted to the keyword and description meta tags, you can see:

<blockquote>
&lt;meta name="Keywords" content="html,css,tutorial,html5,dhtml,css3,xsl,xslt,xhtml,javascript,jquery,asp,ado,net,vbscript,dom,sql,colors,soap,php,rss,authoring,programming,training,learning,quiz,beginner's guide,primer,lessons,school,howto,reference,examples,samples,source code,tags,demos,tips,links,FAQ,tag list,forms,frames,color table,w3c,cascading style sheets,active server pages,dynamic html,internet,database,development,Web building,Webmaster,html guide" /&gt;

&lt;meta name="Description" content="Free HTML XHTML CSS JavaScript jQuery XML DOM XSL XSLT RSS AJAX ASP .NET PHP SQL tutorials, references, examples for web building." /&gt;
</blockquote>

I don't know, since I've never worked for w3schools, but it looks like there's exactly one bit of code/template that contributes to the meta tags and it dumps everything in there.  
The content of the page itself contributes nothing.  

Segmented output allows contribution to all areas at multiple levels.  Any of your templates or handlers can tribute to (or replace) the meta tags, the title, the html head element, etc.
It doesn't always make sense for templates to contribute to anything but the body, but when it does, I wanted webhandle to help me make it possible instead of causing me to write code
as a work around that looks like a 5th grade shop project.  (Appologies to any 5th graders out there just finishing a shop project.)




