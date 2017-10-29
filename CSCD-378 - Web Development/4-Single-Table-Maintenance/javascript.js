var TABLE = $('#table');
var CLONE = $('#clone');
var id;
$.fn.exists = function () {
	return this.length !== 0;
};

$.delete = function (data) {
	$.ajax({
		url: "server.php",
		type: 'DELETE',
		data: data
	});
};

$.get("server.php", function (response) {
	var re = JSON.parse(response);
	for (var i = 0; i < re.length; i++) {
		var r = re[i];
		updateRow(r["id"], [r["name"], r["year"], r["studio"], r["price"], r["description"]])
	}
});

$('.table-remove').click(function () {
	var row = $(this).parents('tr');
	var id = row.attr('id');
	row.detach();
	$.delete({id: id});
});

function updateRow(id, data) {
	var row = $("#" + id);
	var exists = row.exists();
	if (!exists) {
		row = CLONE.find('tr.hide').clone(true).removeClass('hide table-line');
		row.attr('id', id);
	}
	var col = row.find('td');
	for (var i = 0; i < 5; i++) {
		col[i + 1].innerText = data[i];
	}
	if (!exists) {//Add it to the table if it isn't already in the table
		TABLE.find('table').append(row);
	}
}

function save() {
	var inputs = $('#newMovieForm').find("input[type=text], textarea");
	inputs.each(function (i, x) {
		inputs[i] = x.value;
	});
	$.post("server.php", {
		id: id,
		name: inputs[0],
		year: inputs[1],
		studio: inputs[2],
		price: inputs[3],
		description: inputs[4]
	});
	updateRow(id, inputs);
	hideForm();
	clearInputs();
}

function cancel() {
	hideForm();
	clearInputs();
}

function newMovie() {
	var largest = 0;
	TABLE.find('tr').each(function (i, x) {
		var cur = parseInt($(x).attr('id'));
		if (cur > largest) largest = cur;
	});
	id = largest + 1;
	showForm();
}

$('.table-edit').click(function () {
	var row = $(this).closest('tr');
	var children = row.find('td');
	var inputs = $('#newMovieForm').find("input[type=text], textarea");
	for (var i = 1; i < children.length; i++) {
		inputs.get(i - 1).value = children.get(i).innerText;
	}
	id = row.attr('id');
	showForm();
});


function hideForm() {
	$('#newMovieDiv').addClass("hide");
	$('#table').removeClass("blur");
}

function showForm() {
	$('#newMovieDiv').removeClass("hide");
	$('#table').addClass("blur");
}

function clearInputs() {
	$('#newMovieForm').find("input[type=text], textarea").val("");
}