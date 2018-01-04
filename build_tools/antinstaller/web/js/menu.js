var WIDTH = 200;
var showing = false;
var contentsPanel = null;

function toggleMenu(){
	contentsPanel = document.getElementById('contents-panel');
	toggleAnchor = document.getElementById('toggle');
	if(showing) {
		hideMenu();
		showing = false;
		toggleAnchor.innerHTML = "show menu";
	}
	else {
		showMenu();
		showing = true;
		toggleAnchor.innerHTML = "hide menu";
	}
}

function showMenu(){
	if (contentsPanel.style.left != "0px") {
		doShow(WIDTH * -1);
	}
}

function hideMenu(){
	if (contentsPanel.style.left != "- " + WIDTH + "px") {
		doHide(0);
	}
}

function doHide(pos){
	contentsPanel.style.left = pos + "px";
	if(pos >= WIDTH * -1){
		pos = pos - 20;
		setTimeout("doHide(" + pos + ")", 10);
	}
}
function doShow(pos){
	contentsPanel.style.left = pos + "px";
	if(pos < 0){
		pos = pos + 20;
		setTimeout("doShow(" + pos + ")", 10);
	}
}