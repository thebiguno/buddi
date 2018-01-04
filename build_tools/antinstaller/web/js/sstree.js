// tree curtesy http://sstree.tigris.org
function toggle(elm) {
 var newDisplay = "none";
 elm.style.backgroundImage = 'url(branding/images/sstree/folder-closed.gif)';
 var e = elm.nextSibling; 
 while (e != null) {
  if (e.tagName == "OL" || e.tagName == "ol") {
   if (e.style.display == "none") {
    newDisplay = "block";
    elm.style.backgroundImage = 'url(branding/images/sstree/folder-open.gif)';
   }
   break;
  }
  e = e.nextSibling;
 }
 while (e != null) {
  if (e.tagName == "OL" || e.tagName == "ol") e.style.display = newDisplay;
  e = e.nextSibling;
 }
}

function collapseAll(divId, tags) {
 for (i = 0; i < tags.length; i++) {
  var lists = document.getElementById(divId).getElementsByTagName(tags[i]);
  for (var j = 0; j < lists.length; j++) {
   if(lists[j].className == "init-hidden"){
    lists[j].style.display = "none";
   }
   else{
   	// here set default icon
   	var a = lists[j].parentNode.getElementsByTagName("a")[0];
   	if(a.tagName == "A"){
   	 a.style.backgroundImage = 'url(branding/images/sstree/folder-open.gif)';
   	}
   }
  }
  var e = document.getElementById("root");
  e.style.display = "block";
 }
}

function openBookMark() {
 var h = location.hash;
 if (h == "") h = "default";
 if (h == "#") h = "default";
 var ids = h.split(/[#.]/);
 for (i = 0; i < ids.length; i++) {
  if (ids[i] != "") toggle(document.getElementById(ids[i]));
 }
}
