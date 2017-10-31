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

var client = new HttpClient();

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

function getDefinition(word, table) {
	client.get('http://api.datamuse.com/words?sp=' + word + '&qe=sp&md=d',
		function (response) {
			var definitions = response[0]["defs"];
			for (var j = 0; j < definitions.length && j < 5; j++) {
				var def = definitions[j];
				var ix = def.indexOf("\t");
				var p1 = def.substring(0, ix);
				var p2 = def.substring(ix + 1);
				var row = table.insertRow(j + 1);
				row.insertCell(0).innerHTML = p1;
				row.insertCell(1).innerHTML = p2;
			}
		});
}

function getSynonym(word, table) {
	client.get('http://api.datamuse.com/words?rel_syn=' + word,
		function (response) {
			for (var j = 0; j < response.length && j < 5; j++) {
				table.insertRow(j + 1).insertCell(0).innerHTML = response[j]["word"];
			}
		});
}

function getAntonym(word, table) {
	client.get('http://api.datamuse.com/words?rel_ant=' + word,
		function (response) {
			for (var j = 0; j < response.length && j < 5; j++) {
				table.insertRow(j + 1).insertCell(0).innerHTML = response[j]["word"];
			}
		});
}

function getAnagram(word, table) {
	client.get('http://www.anagramica.com/best/:' + word,
		function (response) {
			var words = response['best'];
			for (var j = 0; j < words.length && j < 5; j++) {
				table.insertRow(j + 1).insertCell(0).innerHTML = words[j];
			}
		});
}

function getYoda(word, table) {
	var phrase = encodeURI(word);
	client.get('http://api.funtranslations.com/translate/yoda.json?text=' + phrase,
		function (response) {
			table.insertRow(1).insertCell(0).innerHTML = response["contents"]["translated"];
		});
}

function getTranslations(word, table) {
	getTranslation(word, table, "Spanish", "es");
	getTranslation(word, table, "Chinese", "zh-CN");
	getTranslation(word, table, "Arabic", "ar");
	getTranslation(word, table, "Hindi", "hi");
	getTranslation(word, table, "Danish", "da");
}

function getTranslation(word, table, language, languageCode) {
	client.get('https://script.google.com/macros/s/AKfycbyzC7f07-99b4Oadwx0TWIDdhCgF-0z9w16Q3vIs4e9DgmbV6gm/exec?q=' + word + '&source=en&target=' + languageCode,
		function (response) {
			var row = table.insertRow(1);
			row.insertCell(0).innerHTML = language;
			row.insertCell(1).innerHTML = JSON.parse(response)["translatedText"];
		});
}

function searchWord() {
	var word = $("#inputWord").val().toString();

	var tableDefinitions = document.getElementById("outputTableDefinitions");
	var tableSynonyms = document.getElementById("outputTableSynonyms");
	var tableAntonyms = document.getElementById("outputTableAntonyms");
	var tableAnagrams = document.getElementById("outputTableAnagrams");
	var tableTranslations = document.getElementById("outputTableTranslations");
	var tableYoda = document.getElementById("outputTableYoda");

	clearTables([tableDefinitions, tableSynonyms, tableAntonyms, tableAnagrams, tableTranslations, tableYoda]);
	if (word.indexOf(" ") === -1) {
		getDefinition(word, tableDefinitions);
		getSynonym(word, tableSynonyms);
		getAntonym(word, tableAntonyms);
		getAnagram(word, tableAnagrams);
	} else {
		getYoda(word, tableYoda);
	}
	getTranslations(word, tableTranslations);
}
