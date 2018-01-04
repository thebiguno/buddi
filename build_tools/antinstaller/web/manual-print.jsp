<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <title>Ant Installer</title>
  <link href="style-print.css" type="text/css" rel="stylesheet">
  <link rel="SHORTCUT ICON" href="images/antinstaller-icon.png">
  <meta name="keywords"
 content="Ant, installer, AntInstaller, gui, console, input, parameters, properties, swing, user interface, validation, configuration">
</head>
<body>
	<div class="tpheadertitle">AntInstaller</div>
	<div class="tpcontent">
<!-- JSP start -->
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%

String strJspDir = (String)application.getAttribute("myjasper.jsp.dir");
File fleJspDir = new File(strJspDir);

BufferedReader br = new BufferedReader(new FileReader(new File(fleJspDir, "manual.html")));
String line = null;
boolean copy = false;
while((line = br.readLine()) != null){
	if(line.indexOf("<h2>Manual</h2>") > -1) {
		copy = true;
	}
	else if(line.indexOf("<!-- content end [segment-end]-->") > -1) {
		copy = false;
	}
	if(copy) {
		out.println(line);
	}
}
%>
	</div>
</div>
</body>
</html>
