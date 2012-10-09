function createActionItem(itemType, parentId) {
	$.get(urlCreator.getCreateUrl(itemType, parentId) + "?response-wrapper=none&response-package=body-only", function(data, textStatus, jqXHR) {
		if(reloadIfSessionExpired(jqXHR)) { return; }
		var dialog = createDialog(data, 'Create');
		dialog.addClass('dialog-create-form');
	})
	.error(callError);
	return false;
}

function submitCreateActionItem(evt) {
	evt.preventDefault();
	var createForm = $(evt.target);
	var parms = createForm.serialize();
	$.post(createForm.attr('action'), parms + "&response-wrapper=none&response-package=body-only", function(data, textStatus, jqXHR) {
		if(reloadIfSessionExpired(jqXHR)) { return; }
		$(urlCreator.getParentSelector(data)).append(data);
	})
	.error(callError);
	createForm.closest('.ui-dialog').remove();
	return false;
}

function deleteActionItem(evt) {
	var actionDiv = $(evt.target).closest('.action-item');
	
	if(confirm('Delete?')) {
		$.post(urlCreator.getDeleteUrl(evt.target), function(data, textStatus, jqXHR) {
			if(reloadIfSessionExpired(jqXHR)) { return; }
			actionDiv.remove();
		})
		.error(callError);
	}
}

function editActionItem(evt) {
	$.get(urlCreator.getEditUrl(evt.target) + "?response-wrapper=none&response-package=body-only", function(data, textStatus, jqXHR) {
		if(reloadIfSessionExpired(jqXHR)) { return; }
		var dialog = createDialog(data, 'Edit', submitEditActionItem);
		dialog.addClass('dialog-edit-form');
	})
	.error(callError);
	return false;
}

function submitEditActionItem(evt) {
	evt.preventDefault();
	var createForm = $(evt.target);
	var parms = createForm.serialize();
	
	$.post(createForm.attr('action'), parms + "&response-wrapper=none&response-package=body-only", function(data, textStatus, jqXHR) {
		if(reloadIfSessionExpired(jqXHR)) { return; }
		var parsedResponse = $(data);
		
		var form = parsedResponse.find('form');
		if(form.size() > 0) {
			var dialog = createDialog(data, 'Edit', submitEditActionItem);
			dialog.addClass('dialog-edit-form');
		}
		else {
			var objectId = parsedResponse.attr('data-id');
			if(objectId != null) {
				$('[data-id="' + objectId + '"]').replaceWith(data);
			}
		}
	})
	.error(callError);
	
	// remove the previous dialog
	createForm.closest('.ui-dialog').remove();
	return false;
}

function reloadIfSessionExpired(jqXHR) {
	if('required' == jqXHR.getResponseHeader('X-Authorization')) {
		window.location.reload(true);
		return true;
	}
	return false;
}

function createDialog(content, dialogTitle, submitHandler) {
	var dialog = $(content);
	dialog.dialog({title: dialogTitle, width: 'auto'});
	if(submitHandler) {
		dialog.on('submit', submitHandler);
	}
	return dialog;
}

function callError() {
	createDialog("<div><h1>Rough Roads Ahead</h1> <p>Looks like there's a problem connecting to the server.<p></div>", "We've Got a Problem");
}

urlCreator = {
	editUrlFunctions: {},
	createUrlFunctions: {},
	deleteUrlFunctions: {},
	parentSelectors: {},
	
	registerEdit: function(className, urlFunction) {
		this.editUrlFunctions[className] = urlFunction;
	},
	
	registerDelete: function(className, urlFunction) {
		this.deleteUrlFunctions[className] = urlFunction;
	},
	
	registerCreate: function(className, urlFunction) {
		this.createUrlFunctions[className] = urlFunction;
	},
	
	registerParentSelector: function(className, selector) {
		this.parentSelectors[className] = selector;
	},
	
	getParentSelector: function(data) {
		var actionNode = $(data).find('.action-item');
		if(actionNode.size() == 0) {
			actionNode = $(data);
		}
		var classes = actionNode.attr('class').split(' ');
		
		for(var i = 0; i < classes.length; i++) {
			var selector = this.parentSelectors[classes[i]];
			if(selector) {
				return selector;
			}
		}
	},
	
	getEditUrl: function(focusNode) {
		return this._getUrl(focusNode, this.editUrlFunctions);
	},
	
	getCreateUrl: function(itemType, parentId) {
		var urlFunction = this.createUrlFunctions[itemType]; 
		if(urlFunction != null) {
			return urlFunction(itemType, parentId);
		}
	},
	
	getDeleteUrl: function(focusNode) {
		return this._getUrl(focusNode, this.deleteUrlFunctions);
	},
	
	_getUrl: function(focusNode, functionMap) {
		var actionNode = $(focusNode).closest('.action-item');
		var classes = actionNode.attr('class').split(' ');
		
		for(var i = 0; i < classes.length; i++) {
			var urlFunction = functionMap[classes[i]]; 
			if(urlFunction != null) {
				return urlFunction(actionNode, focusNode);
			}
		}
	}
	

};


$(function() {
	$('body').on('submit', '.dialog-create-form', submitCreateActionItem);
	$('body').on('submit', '.dialog-edit-form', submitEditActionItem);
});