-Upload Files

Uploading files is easy. First, add the attribute

enctype="multipart/form-data"

to each form element with a file upload like you normally would so that the browser
will submit the file data.

In the handle, there are two ways to access the data. Either have a parameter with type FileItem
like:

public Object upload(FileItem myData)

or you can have parameters like the following to get the data:

public Object upload(Byte[] myData)

or like the following to get the file name:

public Object upload(String myData)

If you want to get both the name and the data without refering the FileItem, you can do something like this:

public Object upload(@Name("myDate") String myDataName, Byte[] myData)
