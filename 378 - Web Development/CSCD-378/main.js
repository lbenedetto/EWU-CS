$(window).on("load", init);

function navigateToHash() {
	var hash = window.location.hash;
	$(hash)[0].click();
}

function init() {
	$(".nav").click(nav);
}

function nav() {
	window.location.hash = "#" + $(this).get(0).id;
}