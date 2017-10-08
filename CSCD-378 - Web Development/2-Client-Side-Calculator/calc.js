$(window).on("load", init);

var out = $("#readout");

function init() {
	$(".num").click(num);
	$(".op").click(operators);
	$(".special").click(special);
	$(".clear").click(clear);
}

function num() {
	var input = $(this).text().toString();
	var expr = /[%\/*\-+]/;
	if(expr.test(out.val().toString())){
		clearText();
	}
	if (input === ".") {
		expr = /.*\..*/;
		if (!expr.test(out.val().toString())) {
			addText(input);
		}
	} else {
		addText(input);
	}
}

function operators() {
	// % / * - +
	var operator = $(this).text().toString();
	sessionStorage.reg = out.val() + operator;
	setText(operator);
}

function special() {
	//Sq   +-   1/x   =
	var input = $(this).text().toString();
	switch (input) {
		case "Sq":
			setText(Math.sqrt(out.val()));
			break;
		case "+-":
			setText(out.val() * -1);
			break;
		case "1/x":
			setText(1/out.val());
			break;
		case "=":
			var expr = sessionStorage.reg + out.val();
			setText(eval(expr));
			break;
	}
}

function clear() {
	var input = $(this).text().toString();
	if (input === "C") {
		sessionStorage.reg = "";
	}
	clearText();
}

function addText(t) {
	out.get(0).value += t;
}

function setText(t) {
	out.get(0).value = t;
}

function clearText() {
	out.get(0).value = "";
}