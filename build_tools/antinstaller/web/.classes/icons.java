import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Vector;
import org.tp23.jasper.runtime.*;
import java.beans.*;
import org.tp23.jasper.*;
import org.tp23.jsp.*;
import java.io.*;
import java.util.*;


public class icons extends ObjectJspBase {


    static {
    }
    public icons( ) {
    }

    private static boolean _jspx_inited = false;

    public final void _jspx_init() throws JasperException {
    }

    public void _jspService(final Destination destination)
        throws IOException, JspEngineException {

        ObjectJspFactory _jspxFactory = null;
        ObjectPageContext pageContext = null;
        Session session = null;
        ObjectContainerContext application = null;
        ObjectConfig config = null;
        ObjectJspWriter out = null;
        Object page = this;
        String  _value = null;
        try {

            if (_jspx_inited == false) {
                _jspx_init();
                _jspx_inited = true;
            }
            _jspxFactory = destination.getServer().getObjectJspFactory();
            pageContext = _jspxFactory.getPageContext(this, destination,
			"", true, 8192, true);

            application = pageContext.getObjectContainerContext();
            config = pageContext.getObjectConfig();
            session = destination.getSession();
            out = pageContext.getOut();

            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/icons.jsp";from=(0,0);to=(59,0)]
                out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\r\n<html>\r\n<head>\r\n  <title>Ant Installer</title>\r\n  <link href=\"style.css\" type=\"text/css\" rel=\"stylesheet\">\r\n  <link href=\"css/nav.css\" rel=\"stylesheet\" type=\"text/css\">\r\n  <link rel=\"SHORTCUT ICON\" type=\"image/png\" href=\"images/antinstaller-icon.png\">\r\n\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n  <meta name=\"keywords\"\r\n content=\"Ant, installer, AntInstall, gui, console, input, parameters, properties, swing, user interface, validation, configuration\">\r\n <script type=\"text/javascript\" src=\"js/menu.js\"></script>\r\n <script type=\"text/javascript\" src=\"js/sstree.js\"></script>\r\n <script type=\"text/javascript\" src=\"js/winfix.js\"></script>\r\n</head>\r\n<body onload=\"collapseAll('contents-panel', ['ol']); \"><div id=\"tpallcontent\">\r\n<table cellspacing=\"0\" width=\"100%\">\r\n  <tbody>\r\n    <tr class=\"tpheader\">\r\n      <th class=\"tpleft\">\r\n\t\t\t<a target=\"_top\" href=\"index.html\" title=\"home\"><img src=\"images/ant-install-small.png\" alt=\"AntInstaller\" id=\"logo\" width=\"76\" height=\"50\"/></a>\r\n\t\t\t<script type=\"text/javascript\">winFix();</script>\r\n      </th>\r\n      <th class=\"tptop\" valign=\"bottom\">\r\n\t\t\t\t<img src=\"space.gif\" height=\"1\" width=\"440\" alt=\"spacer\"/><br/>\r\n\t\t\t\t\r\n\t\t\t\t<table>\r\n\t\t\t\t\t<tr>\r\n\t\t\t\t\t\t<td valign=\"top\">\r\n\t\t\t\t\t\t\t<div class=\"tpheadertitle\">AntInstaller</div>\r\n\t\t\t\t\t\t\t<!--img src=\"images/ant-install-title.png\" alt=\"AntInstaller\"/-->\r\n\t\t\t\t\t\t</td>\r\n\t\t\t\t\t\t<td width=\"100%\" align=\"right\" valign=\"bottom\" nowrap=\"NOWRAP\">\r\n\t\t\t\t<div class=\"tpraised\">\r\n\t\t\t\t<a class=\"tpbutton\" target=\"_top\" href=\"index.html\">home</a>\r\n\t\t\t\t<a class=\"tpbutton\" target=\"_top\" href=\"http://sourceforge.net/project/showfiles.php?group_id=123466&amp;package_id=134917\">download</a>\r\n\t\t\t\t<a class=\"tpbutton\" target=\"_top\" href=\"http://sf.net\">sourceforge</a> \r\n\t\t\t\t<a class=\"tpbutton\" target=\"_top\" href=\"manual-ant.html\">antmanual</a> \r\n\t\t\t\t<a class=\"tpbutton\" target=\"_top\" href=\"http://sourceforge.net/tracker/?group_id=123466&amp;atid=696615\">RFEs</a> \r\n\t\t\t\t<a class=\"tpbutton\" target=\"_top\" href=\"http://sourceforge.net/tracker/?group_id=123466&amp;atid=696612\">Bugs</a></div>\r\n\t\t\t\t\t\t</td>\r\n\t\t\t\t\t</tr>\r\n\t\t\t\t</table>\r\n      </th>\r\n    </tr>\r\n    <tr class=\"tpbody\">\r\n\t\t<td class=\"tpleft\" valign=\"bottom\">\r\n\t\t\t<br/>\r\n\t\t\t<div id=\"logoset\">\r\n\t\t\t<a target=\"_top\" href=\"http://sourceforge.net\"><img src=\"http://sourceforge.net/sflogo.php?group_id=123466&amp;type=2\" alt=\"SourceForge.net Logo\" border=\"0\" height=\"37\" width=\"125\"></a>\r\n\t\t\t<br/><br/>\r\n\t\t\t<a target=\"_top\" href=\"http://sourceforge.net/donate/index.php?group_id=123466\">\r\n\t\t\t<img src=\"http://sourceforge.net/images/project-support.jpg\" alt=\"donate to AntInstaller\"/>\r\n\t\t\t</a>\r\n\t\t\t</div>\r\n\t\t</td>\r\n      <td class=\"tpright\" valign=\"top\">\r\n      <div class=\"tpcontent\">\r\n\t\t\t<!--[segment-content] page content start -->\r\n\r\n");
            // end
            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/icons.jsp";from=(59,60);to=(66,2)]
                out.write("\r\n\t\t<h2>icons</h2>\r\n\t\tAntInstaller has a few options for the button icons used in the installer.<br/><br/>\r\n\t\t\r\n\t\tThese can be included in the installer by adding the <code>icons</code> attribute to the <code>installer</code> element\r\n\t\tof the <a href=\"installertask.html\">AntInstaller Ant task</a>.<br/><br/>\r\n\t\t\r\n\t\t");
            // end
            // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/icons.jsp";from=(66,4);to=(79,3)]
                
                		String strJspDir = (String)application.getAttribute("myjasper.jsp.dir");
                		File fleJspDir = new File(strJspDir);
                		File dir = new File(fleJspDir, "images/navicons");
                		File[] directories = dir.listFiles();
                		for (int i = 0; i < directories.length; i++){
                			if(!directories[i].isDirectory())continue;
                			out.println("<h2>" + directories[i].getName() + "</h2>");
                			File iconsDir = new File(directories[i], "resources/icons");
                			File[] icons = iconsDir.listFiles();
                			for (int j = 0; j < icons.length; j++){
                				if(!icons[j].getName().endsWith(".png"))continue;
                			
                			
            // end
            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/icons.jsp";from=(79,5);to=(80,23)]
                out.write("\r\n<img align=\"left\" alt=\"");
            // end
            // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/icons.jsp";from=(80,26);to=(80,44)]
                out.print(icons[j].getName());
            // end
            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/icons.jsp";from=(80,46);to=(80,69)]
                out.write("\" src=\"images/navicons/");
            // end
            // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/icons.jsp";from=(80,72);to=(80,96)]
                out.print(directories[i].getName());
            // end
            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/icons.jsp";from=(80,98);to=(80,115)]
                out.write("/resources/icons/");
            // end
            // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/icons.jsp";from=(80,118);to=(80,136)]
                out.print(icons[j].getName());
            // end
            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/icons.jsp";from=(80,138);to=(81,3)]
                out.write("\" width=\"16\" height=\"16\"/>\r\n\t\t\t");
            // end
            // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/icons.jsp";from=(81,5);to=(84,2)]
                
                				
                			}
                		
            // end
            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/icons.jsp";from=(84,4);to=(87,2)]
                out.write("\r\n\t\t\t<br/><b>License</b><br/>\r\n\t\t\t<div class=\"tpcropped\">\r\n\t\t");
            // end
            // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/icons.jsp";from=(87,4);to=(95,2)]
                
                			BufferedReader br = new BufferedReader(new FileReader(new File(iconsDir, "LICENSE")));
                			String line = null;
                			while( (line=br.readLine()) != null ){
                				if(line.equals(""))out.println("<br/>");
                				else out.println(line.replaceAll("&", "&amp;").replaceAll(">", "&gt;").replaceAll("<", "&lt;"));
                				out.println("<br/>");
                			}
                		
            // end
            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/icons.jsp";from=(95,4);to=(97,2)]
                out.write("\r\n\t\t\t</div><hr/>\r\n\t\t");
            // end
            // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/icons.jsp";from=(97,4);to=(100,2)]
                
                			
                		}
                		
            // end
            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/icons.jsp";from=(100,4);to=(200,0)]
                out.write("\r\n\r\n\r\n<!-- content end [segment-end]-->\r\n\t\t \t</div>\r\n      </td>\r\n    </tr>\r\n  </tbody>\r\n</table>\r\n<div id=\"contents-panel\">\r\n<!--[segment-file://contents-include.html] menu start -->\r\n<div id=\"contents-menu\">\r\n<ol class=\"sidebar\" id=\"root\">\r\n\t<li class=\"panel\"><a href=\"#default\" class=\"folder\" onclick=\"toggle(this)\"></a><b>Site map</b>\r\n\t\t<ol>\r\n\t\t\t<li class=\"sidebar\"><a href=\"introduction.html\">Introduction</a></li>\r\n\t\t\t<li class=\"panel\"><a href=\"#default\" class=\"folder\" onclick=\"toggle(this)\"></a><b>Developer References</b>\r\n\t\t\t\t<ol>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"quickstart.html\">Quick Start</a></li>\r\n\t\t\t\t\t<li class=\"panel\"><a href=\"#default\" class=\"folder\" onclick=\"toggle(this)\"></a><a href=\"manual.html\">Manual</a>\r\n\t\t\t\t\t\t<ol class=\"init-hidden\">\r\n\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#config\">antinstall-config.xml</a></li>\r\n\t\t\t\t\t\t\t<li class=\"panel\"><a href=\"#default\" class=\"folder\" onclick=\"toggle(this)\"></a><a href=\"manual.html#page\">Pages</a>\r\n\t\t\t\t\t\t\t\t<ol class=\"init-hidden\">\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#pagesplash\">Splash Page</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#pagetext\">Text Page</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#pagelicense\">License Page</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#pageinput\">Input Page</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#pageprogress\">Progress Page</a></li>\r\n\t\t\t\t\t\t\t\t</ol>\r\n\t\t\t\t\t\t\t</li>\r\n\t\t\t\t\t\t\t<li class=\"panel\"><a href=\"#default\" class=\"folder\" onclick=\"toggle(this)\"></a><a href=\"manual.html#inputtypes\">Input types</a>\r\n\t\t\t\t\t\t\t\t<ol class=\"init-hidden\">\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#app-root\">Application Root</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#checkbox\">Checkbox</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#comment\">Comment</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#date\">Date</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#directory\">Directory</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#file\">File</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#large-select\">Large Select</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#password\">Password Text</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#password-confirm\">Confirm Password</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#select\">Select</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#target\">Target</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#target-select\">Target Select</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#text\">Unvalidated Text</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#validated\">Validated Text</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#extvalidated\">Externally Validated Text</a></li>\r\n\t\t\t\t\t\t\t\t</ol>\r\n\t\t\t\t\t\t\t</li>\r\n\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#extractor\">Self Extractor</a></li>\r\n\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#non-extractor\">Non Extractor</a></li>\r\n\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#scripts\">Start Scripts</a></li>\r\n\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#refs\">Dynamic References</a></li>\r\n\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#pagedisplay\">Page Displaying</a></li>\r\n\t\t\t\t\t\t</ol>\r\n\t\t\t\t\t</li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"installertask.html\">Installer Ant task</a></li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"validationofconfig.html\">Validation of config</a></li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"lookandfeels.html\">LookAndFeels</a> <br/>(inc screenshots)</li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"classpathresources.html\">Resources/Classpath issues</a></li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"i18n.html\">Internationalisation</a></li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"auto.html\">Automated installs</a></li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"installtypes.html\">Multiple install types</a></li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"posttargets.html\">Post display targets</a></li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"icons.html\">Button Icons</a></li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"antinstall-config-example.html\">Example antinstall-config.xml</a></li>\r\n\t\t\t\t</ol>\r\n\t\t\t</li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"manual-ant.html\">Ant Manual</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"antlinks.html\">Ant links</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"userusage.html\">User usage</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"licenses.html\">Licenses</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"potentialuses.html\">Potential uses</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"roadmap.html\">Road Map</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"wanted.html\">Wanted</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"dtds.html\">DTDs</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"changelog.html\">Changelog</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"http://sourceforge.net/projects/antinstaller\">Project page on SourceForge</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"java2html/antinstaller/index.html\">Java2HTML (main)</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"java2html/ext/index.html\">Java2HTML (extensions)</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"http://antinstaller.cvs.sourceforge.net/antinstaller\">Public CVS over HTTP</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"http://sourceforge.net/sendmessage.php?touser=616485\">Contact AntInstaller Admin</a></li>\r\n\t\t</ol>\r\n\t</li>\r\n</ol>\r\n<br/>\r\n<br/>\r\n</div>\r\n\r\n\r\n<!-- menu end [segment-end]-->\r\n</div>\r\n<div id=\"contents-options\">\r\n<a id=\"toggle\" href=\"#\" onclick=\"toggleMenu(); return false;\">show menu</a>\r\n</div>\r\n\r\n</div>\r\n</body>\r\n</html>\r\n");
            // end

        } catch (Exception ex) {
            if (out.getBufferSize() != 0)
                out.clearBuffer();
            pageContext.handlePageException(ex);
        } finally {
            out.flush();
            _jspxFactory.releasePageContext(pageContext);
        }
    }
}
