
Fitting with the concept of progressive enhancement, webhandle default templates (called tripartate) are simple to start with and simple
to add functionality to.

Every template can have multiple parts.  Each part goes someplace within the response.  Each template part has a file.  So, if I want a template
named "myInfo" that writes information to the body, I create a file named: myInfo.body


If I want to show a greating, the contents of myInfo.body looks like:

<blockquote>
	<pre>
		Hi, I'm <strong>Dan</strong>.
	</pre>
</blockquote>

So, basically, write in html like every other templating language.


Now, I want to use dynamic data loaded by the Handle (using code like location.put("theName", "Dan")).

<blockquote>
	<pre>
		Hi, I'm <strong>__theName__</strong>.
	</pre>
</blockquote>

Notice the double underscore before and after the data value.  

This active portion will only print something if the name is not null.

Now, let's move the name formatting to its own template named "nameFormat".  I'll create a file in my templates folder called "nameFormat.body"
and fill it with this.

<blockquote>
	<pre>
		<strong>__$this__</strong>
	</pre>
</blockquote>

The $this value always stand for whatever got passed to it.  Now the myInfo.body file looks like this.

<blockquote>
	<pre>
		Hi, I'm __theName::nameFormat__.
	</pre>
</blockquote>

Notice the double colon then the name of the template we want to process the information.  Super simple.  

Now, a progressive enhancement.  If the data we referenced were an object, we'd do pretty much the same thing.  Let's say the handle added data
like this:

>> location.put("person", new Person("Dan"));

where person has a "theName" bean property.  If person has a toString() which just returns the name, the files don't need to change except to 
reference the new variable name.  However, with a person object we could have written the template as:


The $this value always stand for whatever got passed to it.  Now the myInfo.body file looks like this.

myInfo.body
<blockquote>
	<pre>
		Hi, I'm __person::nameFormat__.
	</pre>
</blockquote>

nameFormat.body:
<blockquote>
	<pre>
		<strong>__theName__</strong>
	</pre>
</blockquote>


Now, the person object is passed to the nameFormat template where it accesses the "theName" property.

Now, a progressive enhancement.  Let's say we've got a collection of people we'd like to layout in tiles.  Assuming the handle added the
list ot the Location with the key "people", we could write templates like this:

allPeople.body
<blockquote>
	<pre>
		__people::myInfo__
	</pre>
</blockquote>

myInfo.body
<blockquote>
	<pre>
		<div>
			Hi, I'm __$this::nameFormat__.
		</div>
	</pre>
</blockquote>

nameFormat.body:
<blockquote>
	<pre>
		<strong>__theName__</strong>
	</pre>
</blockquote>


Data which are collections automatically trigger one template rendering per item.  If, for whatever reason you want to pass the collection
as a collection to a single template, you can reference it like "@people".

The "$this" in myInfo.body is not necessary.  If no data is referenced, the next template will keep the same current item as its context.  we could
have written it like:

myInfo.body:
<blockquote>
	<pre>
		<div>
			Hi, I'm __::nameFormat__.
		</div>
	</pre>
</blockquote>


The third part of the tripartate template element is the conditional.  The data selector is in itself an implicit conditional in that if the data does not
exist the template won't be rendered.  However, sometimes you'd like to conditionally render the template based on one piece of data but actually render
another.  For exmple, we could write a template like:

<blockquote>
	<pre>
		<div>
			Hi, I'm __doesntKnowWhoIAm??person::nameFormat__.
		</div>
	</pre>
</blockquote>

Here, if the doesntKnowWhoIAm key is true, then the person will be rendered by the nameFormat template.

Now, a progressive enhancement.  The conditional and data selector are both expressions by default.  They are JEXL expressions which allows you to do
some things like:

<blockquote>
	<pre>
		<div>
			Hi, I'm __doesntKnowWhoIAm || firstTimeVistior ??person::nameFormat__.
		</div>
	</pre>
</blockquote>

The template name is, by default, a literal value referencing an absolute template path.  However, you can have an expression evaluated to a template name
prefixing the template specifier with a "$".  So, if the person object knew which template should render its name, you could write:

<blockquote>
	<pre>
		<div>
			Hi, I'm __doesntKnowWhoIAm || firstTimeVistior ??person::$person/useThisTemplate__.
		</div>
	</pre>
</blockquote>

Notice the "/" here.  JEXL expressions use a "." like many other expression languages to denote members and functions.  This still works in tripartate templates.
However, you can also use the "/" which acts on data slightly differently.  One way is to act as an accumulator of all the data at a path.  So:

>> __people/addresses/street1__

will create a list of all the street1 member values for all of the addresses for all of the people (assuming people contains multiple Person objects
and that each person had multiple Address objects).  If you tried:

>> __people.addresses.street1__

it would just poop on your shoes since the dot operator doesn't know how to extract values from the members of a collection.












To include the web context in the template use the text:
__webAppContextPath__



btw, if you want an actual double underscore, you can use this:

>> __::dus__
