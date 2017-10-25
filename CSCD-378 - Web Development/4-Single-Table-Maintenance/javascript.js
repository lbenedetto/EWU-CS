var $TABLE = $('#table');
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
	} else {
		button.html("Save");
		saveRow(children)
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
	var $clone = $TABLE.find('tr.hide').clone(true).removeClass('hide table-line');
	$TABLE.find('table').append($clone);
});

function saveRow(fields){

}