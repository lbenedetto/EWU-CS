var proxy = 'https://cors-anywhere.herokuapp.com/';

var HttpClient = function () {
	this.get = function (aUrl, aCallback) {
		$.ajax({
			url: proxy + aUrl,
			type: 'GET',
			success: aCallback
		});
	}
};

function clearTable(table) {
	for (var i = 1; i < table.rows.length;) {
		table.deleteRow(i);
	}
}

function clearTables(tables) {
	for (var i = 0; i < tables.length; i++) {
		clearTable(tables[i]);
	}
}

function search() {
	var word = $("#inputWord").val().toString();
	var client = new HttpClient();
	var tableDefinitions = document.getElementById("outputTableDefinitions");
	var tableSynonyms = document.getElementById("outputTableSynonyms");
	var tableAntonyms = document.getElementById("outputTableAntonyms");
	var tableAnagrams = document.getElementById("outputTableAnagrams");
	var tableTranslations = document.getElementById("outputTableTranslations");

	clearTables([tableDefinitions, tableSynonyms, tableAntonyms, tableAnagrams, tableTranslations]);

	//Definition
	client.get('http://api.datamuse.com/words?sp=' + word + '&qe=sp&md=d',
		function (response) {
			var definitions = response[0]["defs"];
			for (var j = 0; j < definitions.length && j < 5; j++) {
				var def = definitions[j];
				var ix = def.indexOf("\t");
				var p1 = def.substring(0, ix);
				var p2 = def.substring(ix + 1);
				var row = tableDefinitions.insertRow(j + 1);
				row.insertCell(0).innerHTML = p1;
				row.insertCell(1).innerHTML = p2;
			}
		});
	//Synonym
	client.get('http://api.datamuse.com/words?rel_syn=' + word,
		function (response) {
			for (var j = 0; j < response.length && j < 5; j++) {
				tableSynonyms.insertRow(j + 1).insertCell(0).innerHTML = response[j]["word"];
			}
		});
	//Antonym
	client.get('http://api.datamuse.com/words?rel_ant=' + word,
		function (response) {
			for (var j = 0; j < response.length && j < 5; j++) {
				tableAntonyms.insertRow(j + 1).insertCell(0).innerHTML = response[j]["word"];
			}
		});
	//Anagram
	client.get('http://www.anagramica.com/best/:' + word,
		function (response) {
			var words = response['best'];
			for (var j = 0; j < words.length && j < 5; j++) {
				tableAnagrams.insertRow(j + 1).insertCell(0).innerHTML = words[j];
			}
		});
	//Translation
	client.get('https://script.google.com/macros/s/AKfycbyzC7f07-99b4Oadwx0TWIDdhCgF-0z9w16Q3vIs4e9DgmbV6gm/exec?source=en&q=' + word + '&target=es',
		function (response) {
			var row = tableTranslations.insertRow(1);
			row.insertCell(0).innerHTML = "Spanish";
			row.insertCell(1).innerHTML = JSON.parse(response)["translatedText"];
		});
	client.get('https://script.google.com/macros/s/AKfycbyzC7f07-99b4Oadwx0TWIDdhCgF-0z9w16Q3vIs4e9DgmbV6gm/exec?source=en&q=' + word + '&target=zh-CN',
		function (response) {
			var row = tableTranslations.insertRow(1);
			row.insertCell(0).innerHTML = "Chinese";
			row.insertCell(1).innerHTML = JSON.parse(response)["translatedText"];
		});
	client.get('https://script.google.com/macros/s/AKfycbyzC7f07-99b4Oadwx0TWIDdhCgF-0z9w16Q3vIs4e9DgmbV6gm/exec?source=en&q=' + word + '&target=da',
		function (response) {
			var row = tableTranslations.insertRow(1);
			row.insertCell(0).innerHTML = "Danish";
			row.insertCell(1).innerHTML = JSON.parse(response)["translatedText"];
		});
}