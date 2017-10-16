$(window).on("load", init);

function navigateToHash() {
	var hash = window.location.hash;
	$(hash)[0].click();
}

function init() {
	$(".nav").click(nav);
	var height = $(document).height() - 90;
	var width = $(document).width() - 300;
	var iframe = $("#content");
	iframe.height(height);
	iframe.width(width);
	console.log(width + "x" + height);
}

function nav() {
	window.location.hash = "#" + $(this).get(0).id;
}