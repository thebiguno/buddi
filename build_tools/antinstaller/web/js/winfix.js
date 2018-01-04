function winFix(){
	if(navigator.appName.indexOf('Microsoft') > -1) {
		var img = document.getElementById('logo');
		img.src = 'images/space.gif'
	}
}