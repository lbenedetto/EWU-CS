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
var id = 0;
var tid = 0;
$('.table-remove').click(function () {
	$(this).parents('tr').detach();
	//TODO: Remove from DB
});

function save() {
	var newRow;
	if (tid > id) {
		newRow = CLONE.find('tr.hide').clone(true).removeClass('hide table-line');
		newRow.attr('id', tid);
	} else {
		newRow = $("#" + tid)
	}
	var data = newRow.find('td');
	var inputs = $('#newMovieForm').find("input[type=text], textarea");
	for (var i = 0; i < 5; i++) {
		data[i + 1].innerText = inputs[i].value;
	}
	if (tid > id) {
		TABLE.find('table').append(newRow);
		id++;
	}
	hideForm();
	clearInputs();
}

function cancel() {
	hideForm();
	clearInputs();
}

function newMovie() {
	tid = id + 1;
	showForm();
}

$('.table-edit').click(function () {
	var row = $(this).closest('tr');
	var children = row.find('td');
	var inputs = $('#newMovieForm').find("input[type=text], textarea");
	for (var i = 1; i < children.length; i++) {
		inputs.get(i - 1).value = children.get(i).innerText;
	}
	tid = row.attr('id');
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
