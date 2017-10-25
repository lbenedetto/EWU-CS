var TABLE = $('#table');
var CLONE = $('#clone');

var HttpClient = function () {
	this.get = function (aUrl, aCallback) {
		$.ajax({
			url: proxy + aUrl,
			type: 'GET',
			success: aCallback
		});
	}
};
var client = new HttpClient();

$('.table-edit').click(function () {
	var button = $(this);
	var children = button.closest('tr').find('td');
	var isEdit = children.get(1).contentEditable;
	if (isEdit === "true") {
		button.html("Edit");
		saveRow(children);
	} else {
		button.html("Save");
		editRow(children);
	}
	for (var i = 1; i < children.length; i++) {
		var child = children.get(i);
		child.contentEditable = isEdit !== "true";
	}
});

$('.table-remove').click(function () {
	$(this).parents('tr').detach();
});

$('.table-add').click(function () {
	var clone = CLONE.find('tr.hide').clone(true).removeClass('hide table-line');
	TABLE.find('table').append(clone);
});

function saveRow(fields) {
	console.log("saving row " + fields.get(1).innerHTML)
}

function editRow(fields) {
	console.log("editing row " + fields.get(1).innerHTML)
}