var $TABLE = $('#table');

$('.table-edit').click()(function () {
	$(this).parents('tr').removeClass()
});
$('.table-remove').click(function () {
	$(this).parents('tr').detach();
});

$('.table-add').click(function () {
	var $clone = $TABLE.find('tr.hide').clone(true).removeClass('hide table-line');
	$TABLE.find('table').append($clone);
});