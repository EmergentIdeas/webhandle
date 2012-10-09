Action Items
------------

One usage pattern is to have each click result in a page submit and reload of a new page.  This is
compatible and works great in all browsers.  However, doing whole page reloads is a little slower
and removes the context of the action.  Alternatively, many one page javascript based apps send
json back and forth and write Javascript application controller code that how to modify the page.
This is also great but you must then write your controller code in Javascript as well as using
a Javascript template library which will be inconvient if not all of your application is written
in javascript.

Action Items provide a little bit of script so that you can have objects edit/created with a dialog
and the html on page updated but have the server do all of the controller code and html generation.

To make this work, you'll need the following js files included:

<pre>
	//ajax.googleapis.com/ajax/libs/jquery/1.8.1/jquery.js
	//ajax.googleapis.com/ajax/libs/jqueryui/1.8.23/jquery-ui.min.js
	/js/actionItems.js
</pre>

Action Items require a node which contains the objects that can be created or edit.  An example would
be:

<pre>
	<div id="listItems">
</pre>

Each item must share a given class (like todo-item) and be marked with the action-item class.  Additionally,
each action-item must have an attribute "data-id" that tells the system what id should be used for restful
handlers.  An example would be:

<pre>
<div class="todo-item action-item" data-id="26">
	<span title="remove"><i class="icon-remove"></i></span>
	<span title="edit"><i class="icon-edit"></i></span>
	this is what I have to do
</div>
</pre>

Each type of item should have urls for create, edit, and delete.  These urls are registered with with a piece
of javascript like:

<pre>
$(function() {
	urlCreator.registerEdit('todo-item', todoEditUrl);
	urlCreator.registerDelete('todo-item', todoDeleteUrl);
	urlCreator.registerCreate('todo-item', todoCreateUrl);
	urlCreator.registerParentSelector('todo-item', '#listItems');
});
</pre>

In the above case, the "todo-item' class has a function registered for each of edit, create, and delete.  These
methods look like:

<pre>
function todoEditUrl(actionNode, focusNode) {
	return "/list-item/" + actionNode.attr('data-id') + "/edit";
}
function todoDeleteUrl(actionNode, focusNode) {
	return "/list-item/" + actionNode.attr('data-id') + "/delete";
}
function todoCreateUrl(itemType, parentId) {
	return "/list/" + parentId + "/item/create";
}
</pre>

Here, the edit and delete url generators are passed the node with the class action-item as well as the focusNode, which is
the node that has the registered click listener.  The create url is passed the object type as well as the id of the parent
object (which may have no representation on the page).

The handlers at the the create and edit urls should respond to a GET 
request with a bare form that allows the editing of the selected item.  An example would be:

<pre>
<form method="post" action="/web2/list-item/33/edit">
	<label for="todoText">Thing to do</label>
	<input type="text" id="todoText" placeholder="What do you need to do?" name="todoText" value="the data submit" >
	<button type="submit" class="btn btn-primary">Save</button>
</form>
</pre>

Notice that the above is a valid form.  The action attribute contains the url that the info will be submitted to.  The Action
Items script takes care of capturing the submit event, submitting the post with an xhr, and stopping the default submit action.
If successful, the action url should return a bare html string like the block that starts with:

>> <div class="todo-item action-item" data-id="26">

If the request is not successful, the return value should be an bare html form like the block that starts with:

>> <form method="post" action="/web2/list-item/33/edit">

To cause the creation of a new object, a link or button like the following can be used (or the listener equivalent):

>> <button type="button" onclick="return createActionItem('todo-item', 1)">New Todo Item</button>

Notice that the createActionItem function is passed the class of the object to be created as well as the id of the parent object.

Since there are probably many edit/delete links/buttons, I've added added listeners to those like:

<pre>
$(function() {
	var listItems = $('#listItems');
	listItems.on('click', '.icon-remove', deleteActionItem);
	listItems.on('click', '.icon-edit', editActionItem);
});
</pre>

The editActionItem and deleteActionItem methods will automatically climb the tree to find the node with the action-item class 
to determine which object type should be edited or deleted and what the id of that item is.

Putting all my script pieces together, I've got:

<pre>
function todoEditUrl(actionNode, focusNode) {
	return "/list-item/" + actionNode.attr('data-id') + "/edit";
}

function todoDeleteUrl(actionNode, focusNode) {
	return "/list-item/" + actionNode.attr('data-id') + "/delete";
}

function todoCreateUrl(itemType, parentId) {
	return "/list/" + parentId + "/item/create";
}

$(function() {
	urlCreator.registerEdit('todo-item', todoEditUrl);
	urlCreator.registerDelete('todo-item', todoDeleteUrl);
	urlCreator.registerCreate('todo-item', todoCreateUrl);
	urlCreator.registerParentSelector('todo-item', '#listItems');
	
	var listItems = $('#listItems');
	listItems.on('click', '.icon-remove', deleteActionItem);
	listItems.on('click', '.icon-edit', editActionItem);
});
</pre>
