var $TABLE = $('#table');

$('.table-edit').click(function () {
	var button = $(this);
	var children = button.closest('tr').find('td');
	var isEdit = children.get(0).contentEditable;
	if (isEdit === "true")
		button.html("Edit");
	else
		button.html("Done");

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