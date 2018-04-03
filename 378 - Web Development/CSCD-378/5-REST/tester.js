function newWindow(response) {
	var w = window.open();
	w.document.write("<pre>" + response + "</pre>")
}

function get(url) {
	$.ajax({
		type: "GET",
		url: url,
		success: function (response) {
			var parsed = JSON.parse(response);
			var formatted = JSON.stringify(parsed, null, 2);
			newWindow(formatted)
		},
		error: function (response) {
			newWindow(response.status + " :" + response.responseText)
		}
	});
}

function post(url, data) {
	$.ajax({
		type: "POST",
		url: url,
		data: data,
		success: function (response) {
			var parsed = JSON.parse(response);
			var formatted = JSON.stringify(parsed, null, 2);
			newWindow(formatted)
		},
		error: function (response) {
			newWindow(response.status + " :" + response.responseText)
		}
	});
}

//<editor-fold desc="VINS">
function getAllVINS() {
	get("./api/v1/vin/GET/");
}

function getSpecificVIN() {
	get("./api/v1/vin/GET/?stock=DL694932");
}

function getRegexVIN() {
	get("./api/v1/vin/GET/?regex=.*DL..4932");
}

function deleteVIN() {
	get("./api/v1/vin/DELETE/?stock=DL694932");
}

function addVIN() {
	post("./api/v1/vin/CREATE/",
		{
			"stock": "DL694932",
			"data": "1C4BJWEG5DL694932"
		});
}

function updateVIN() {
	post("./api/v1/vin/UPDATE/",
		{
			"stock": "DL694932",
			"data": "2C4BJWEG5DL694932"
		});
}
//</editor-fold>

//<editor-fold desc="UPS">
function getAllUPS() {
	get("./api/v1/ups/GET/");
}

function getSpecificUPS() {
	get("./api/v1/ups/GET/?stock=DL694932");
}

function getRegexUPS() {
	get("./api/v1/ups/GET/?regex=UPS-780..33-0");
}

function deleteUPS() {
	get("./api/v1/ups/DELETE/?stock=DL694932");
}

function addUPS() {
	post("./api/v1/ups/CREATE/",
		{
			"stock": "DL694932",
			"data": "UPS-7806533-0"
		});
}

function updateUPS() {
	post("./api/v1/ups/UPDATE/",
		{
			"stock": "DL694932",
			"data": "UPS-7806533-1"
		});
}
//</editor-fold>

//<editor-fold desc="Entry">
function getAllEntry() {
	get("./api/v1/entrydate/GET/");
}

function getSpecificEntry() {
	get("./api/v1/entrydate/GET/?stock=DL694932");
}

function getRegexEntry() {
	get("./api/v1/entrydate/GET/?regex=4\\/2.\\/2016");
}

function deleteEntry() {
	get("./api/v1/entrydate/DELETE/?stock=DL694932");
}

function addEntry() {
	post("./api/v1/entrydate/CREATE/",
		{
			"stock": "DL694932",
			"data": "4/26/2016"
		});
}

function updateEntry() {
	post("./api/v1/entrydate/UPDATE/",
		{
			"stock": "DL694932",
			"data": "4/28/2016"
		});
}
//</editor-fold>

