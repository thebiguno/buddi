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


public class index extends ObjectJspBase {


    static {
    }
    public index( ) {
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

            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/index.jsp";from=(0,0);to=(66,0)]
                out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\r\n<html>\r\n<head>\r\n  <title>Ant Installer</title>\r\n  <link href=\"style.css\" type=\"text/css\" rel=\"stylesheet\">\r\n  <link href=\"css/nav.css\" rel=\"stylesheet\" type=\"text/css\">\r\n  <link rel=\"SHORTCUT ICON\" type=\"image/png\" href=\"images/antinstaller-icon.png\">\r\n\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n  <meta name=\"keywords\"\r\n content=\"Ant, installer, AntInstall, gui, console, input, parameters, properties, swing, user interface, validation, configuration\">\r\n <script type=\"text/javascript\" src=\"js/menu.js\"></script>\r\n <script type=\"text/javascript\" src=\"js/sstree.js\"></script>\r\n <script type=\"text/javascript\" src=\"js/winfix.js\"></script>\r\n</head>\r\n<body onload=\"collapseAll('contents-panel', ['ol']); \"><div id=\"tpallcontent\">\r\n<table cellspacing=\"0\" width=\"100%\">\r\n  <tbody>\r\n    <tr class=\"tpheader\">\r\n      <th class=\"tpleft\">\r\n\t\t\t<a target=\"_top\" href=\"index.html\" title=\"home\"><img src=\"images/ant-install-small.png\" alt=\"AntInstaller\" id=\"logo\" width=\"76\" height=\"50\"/></a>\r\n\t\t\t<script type=\"text/javascript\">winFix();</script>\r\n      </th>\r\n      <th class=\"tptop\" valign=\"bottom\">\r\n\t\t\t\t<img src=\"space.gif\" height=\"1\" width=\"440\" alt=\"spacer\"/><br/>\r\n\t\t\t\t\r\n\t\t\t\t<table>\r\n\t\t\t\t\t<tr>\r\n\t\t\t\t\t\t<td valign=\"top\">\r\n\t\t\t\t\t\t\t<div class=\"tpheadertitle\">AntInstaller</div>\r\n\t\t\t\t\t\t\t<!--img src=\"images/ant-install-title.png\" alt=\"AntInstaller\"/-->\r\n\t\t\t\t\t\t</td>\r\n\t\t\t\t\t\t<td width=\"100%\" align=\"right\" valign=\"bottom\" nowrap=\"NOWRAP\">\r\n\t\t\t\t<div class=\"tpraised\">\r\n\t\t\t\t<a class=\"tpbutton\" target=\"_top\" href=\"index.html\">home</a>\r\n\t\t\t\t<a class=\"tpbutton\" target=\"_top\" href=\"http://sourceforge.net/project/showfiles.php?group_id=123466&amp;package_id=134917\">download</a>\r\n\t\t\t\t<a class=\"tpbutton\" target=\"_top\" href=\"http://sf.net\">sourceforge</a> \r\n\t\t\t\t<a class=\"tpbutton\" target=\"_top\" href=\"manual-ant.html\">antmanual</a> \r\n\t\t\t\t<a class=\"tpbutton\" target=\"_top\" href=\"http://sourceforge.net/tracker/?group_id=123466&amp;atid=696615\">RFEs</a> \r\n\t\t\t\t<a class=\"tpbutton\" target=\"_top\" href=\"http://sourceforge.net/tracker/?group_id=123466&amp;atid=696612\">Bugs</a></div>\r\n\t\t\t\t\t\t</td>\r\n\t\t\t\t\t</tr>\r\n\t\t\t\t</table>\r\n      </th>\r\n    </tr>\r\n    <tr class=\"tpbody\">\r\n\t\t<td class=\"tpleft\" valign=\"bottom\">\r\n\t\t\t<br/>\r\n\t\t\t<div id=\"logoset\">\r\n\t\t\t<a target=\"_top\" href=\"http://sourceforge.net\"><img src=\"http://sourceforge.net/sflogo.php?group_id=123466&amp;type=2\" alt=\"SourceForge.net Logo\" border=\"0\" height=\"37\" width=\"125\"></a>\r\n\t\t\t<br/><br/>\r\n\t\t\t<a target=\"_top\" href=\"http://sourceforge.net/donate/index.php?group_id=123466\">\r\n\t\t\t<img src=\"http://sourceforge.net/images/project-support.jpg\" alt=\"donate to AntInstaller\"/>\r\n\t\t\t</a>\r\n\t\t\t</div>\r\n\t\t</td>\r\n      <td class=\"tpright\" valign=\"top\">\r\n      <div class=\"tpcontent\">\r\n\t\t\t<!--[segment-content] page content start -->\r\n<script type=\"text/javascript\">\r\nfunction gooSrch(){\r\n  var text = document.getElementById('gf').elements['q'].value;\r\n\tdocument.getElementById('gf').elements['q'].value = text + \" site:antinstaller.sourceforge.net\";\r\n\tdocument.getElementById('gf').submit();\r\n}\r\ncollapseAll('frontpage-panel', ['ol']); \r\n</script>\r\n");
            // end
            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/index.jsp";from=(66,60);to=(85,2)]
                out.write("\r\n            <div align=\"right\">\r\n\t\t\t\t\t\t\t<form action=\"http://www.google.com/search\" method=\"get\" id=\"gf\" style=\"display:inline\" onsubmit=\"return gooSrch();\">\r\n\t\t\t\t\t\t\t\t<input type=\"text\" name=\"q\" size=\"10\" class=\"winFix\" />\r\n\t\t\t\t\t\t\t\t<input type=\"image\" src=\"images/toolbar.gif\" /><span class=\"tpsmall\"> Google Search this site</span>\r\n\t\t\t\t\t\t\t</form>\r\n\t\t\t\t\t\t</div>\r\n\t\t<hr/>\r\n\t\tAntInstaller enables you to quickly build installers for your applications using an XML config file and all the power of <a  href=\"http://ant.apache.org\">Ant</a>.<br/><br/>\r\n\t\t\r\n\t\tThe beauty of AntInstaller, from a user point of view, is that it lets you install an app with a user friendly swing GUI. If working over ssh or without an X environment you can still install on the command line with no changes to the installer.<br/><br/>\r\n\t\t\r\n\t\tTo start using AntInstaller <a href=\"http://sourceforge.net/project/showfiles.php?group_id=123466&amp;package_id=134917\" title=\"download\">download</a> and install the binaries. Then either read the <a href=\"quickstart.html\" title=\"AntInstaller quick start\">quick start</a> page, or use the <a href=\"quickstart.html#wizard\" title=\"AntInstaller wizard\">wizard</a> provided to generate an initial installer for your project.  You will need to edit the <code>antinstall-config.xml</code> file following instructions in the AntInstaller <a href=\"manual.html\" title=\"AntInstaller manual\">manual</a> and create an Ant <code>build.xml</code> file referencing the Ant <a href=\"manual/manual/index.html\" title=\"Ant manual\">manual</a>.  You can include the custom Ant <a href=\"installertask.html\" title=\"AntInstaller manual\">task</a> into your project's build to generate the installer.<br/><br/>\r\n\r\n\t\tIn a move to be compatible with Apache projects the code in this project is now licensed under the less restrictive Apache 2.0 license.  The new (18/05/2005) license can be viewed here <a href=\"http://www.apache.org/licenses/LICENSE-2.0\">Apache License 2.0</a>\r\n\t\t<br/><br/>\r\n\t\tAll core code is available in CVS, and can be browsed online <a href=\"http://antinstaller.cvs.sourceforge.net/antinstaller/\" title=\"CVS Browse\">here</a>.\r\n\t\t<h2>Site map</h2>\r\n\t\t<div id=\"frontpage-panel\">\r\n\t\t");
            // end
            // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/index.jsp";from=(85,2);to=(85,58)]
                {
                    String _jspx_qStr = "";
                    pageContext.include("contents-include.html" + _jspx_qStr);
                }
            // end
            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/index.jsp";from=(85,58);to=(123,2)]
                out.write("\r\n\t\t</div>\r\n\t\t<h2>Core Developers</h2>\r\n\t\t<ul>\r\n\t\t\t<li>Paul Hinds - project lead, core dev, and slacking for months on end ;)</li>\r\n\t\t\t<li>Mark Wilson - core development, bug fixings RFEs</li>\r\n\t\t</ul>\r\n\t\t<br/>\r\n\t\t<h2>Contributors</h2>\r\n\t\t\r\n\t\tP.S. Big thanks to Contributors and bug fixers\r\n\t\t<ul>\r\n\t\t\t<li>Marcia Choy - for inital comments and corrections in the web pages</li>\r\n\t\t\t<li>Dueringer Markus - for hints on showing hidden directories</li>\r\n\t\t\t<li>Simon Brooke - for feedback and suggestions.</li>\r\n\t\t\t<li>Mark Anderson - for the TargetSelectInput and -lib fix.</li>\r\n\t\t\t<li>Mohan Kishore - for improvement to the build process.</li>\r\n\t\t\t<li>Mike Watts - for comment and suggestions</li>\r\n\t\t\t<li>Eddyrun - for suggestions for ifProperty enhancements</li>\r\n\t\t\t<li>Upayavira - for help with Apache licensing</li>\r\n\t\t\t<li>Dabintxi - for work on i18n</li>\r\n\t\t\t<li>Michael Lipp - for german translation</li>\r\n\t\t</ul>\r\n\t\t<br/>\r\n\t\t\r\n\t\t<h2>Contact</h2>\r\n\t\t\r\n\t\tTo get in contact the following channels are available.<br/><br/>\r\n\t\t\r\n\t\t<ul>\r\n\t\t\t<li><a href=\"http://sourceforge.net/tracker/?group_id=123466&atid=696615\" title=\"Request for enhancements\">RFEs</a> - Request for enhancements</li>\r\n\t\t\t<li><a href=\"http://sourceforge.net/tracker/?group_id=123466&atid=696612\" title=\"Bug reporting\">Bugs</a> - Bug reporting</li>\r\n\t\t\t<li><a href=\"http://sourceforge.net/forum/forum.php?forum_id=420821\" title=\"Help Forum\">Help Forum</a> - Help mailing list</li>\r\n\t\t\t<li><a href=\"http://sourceforge.net/forum/forum.php?forum_id=420820\" title=\"Open Forum\">Open Forum</a> - Open discussion forum</li>\r\n\t\t\t<li><a href=\"http://sourceforge.net/forum/forum.php?forum_id=420822\" title=\"Dev Forum\">Dev Forum</a> - Developers forum (closed group)</li>\r\n\t\t\t<li><a href=\"http://sourceforge.net/sendmessage.php?touser=616485\" title=\"General contact\">General contact</a> - Drop me a line</li>\r\n\t\t</ul>\r\n\t\t<h2>Version</h2>\r\n\t\t");
            // end
            // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/index.jsp";from=(123,4);to=(128,2)]
                
                		String strJspDir = (String)application.getAttribute("myjasper.jsp.dir");
                		File fleJspDir = new File(strJspDir);
                		Properties versionProps = new Properties();
                		versionProps.load(new FileInputStream(new File(fleJspDir, "../build/version.properties")));
                		
            // end
            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/index.jsp";from=(128,4);to=(129,43)]
                out.write("\r\n\t\tCVS AntInstaller development version: <b>");
            // end
            // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/index.jsp";from=(129,46);to=(129,93)]
                out.print(versionProps.getProperty("ant.install.version"));
            // end
            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/index.jsp";from=(129,95);to=(155,26)]
                out.write("</b>\r\n\t\t<br/><br/>\r\n\t\t\r\n\t\t<h2>Firefox</h2>\r\n\r\n\t\tSite designed for FireFox, other browsers should be ok see <a href=\"browsersupport.html\" alt=\"Browser support\" >Browser support</a> and report bugs if you find them.<br/><br/>\r\n\r\n\t\t<a href=\"http://getfirefox.com/\"\r\n\t\t\t\ttitle=\"Get Firefox - The Browser, Reloaded.\"><img\r\n\t\t\t\tsrc=\"images/browser/firefox-spread-btn-2.png\"\r\n\t\t\t\twidth=\"180\" height=\"60\" alt=\"Get Firefox\"></a>\r\n\t\t<br/>\r\n\t\t<script language=\"JavaScript\" type=\"text/javascript\">\r\n\t\tfunction addSidebar(title, link){ \r\n\t\t\tif ((typeof window.sidebar == \"object\") && (typeof window.sidebar.addPanel == \"function\")){\r\n\t\t\t\twindow.sidebar.addPanel (title, link, \"\");\r\n\t\t\t}\r\n\t\t} \r\n\t\t</script>\r\n\t\t<br/>\r\n\t\tIf you have Mozilla/Firefox\r\n\t\t<a href=\"#\" onclick=\"javascript:addSidebar('AntInstaller SideBar', 'http://antinstaller.sourceforge.net/contents.html');return false;\">Add a sidebar</a> for this site.<br/><br/>\r\n\r\n\t\t\r\n\t\t\r\n\t\t<hr/>\r\n\t\tSite last generated: <b>");
            // end
            // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/index.jsp";from=(155,29);to=(155,49)]
                out.print(new java.util.Date());
            // end
            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/index.jsp";from=(155,51);to=(254,0)]
                out.write("</b>\r\n\r\n<!-- content end [segment-end]-->\r\n\t\t \t</div>\r\n      </td>\r\n    </tr>\r\n  </tbody>\r\n</table>\r\n<div id=\"contents-panel\">\r\n<!--[segment-file://contents-include.html] menu start -->\r\n<div id=\"contents-menu\">\r\n<ol class=\"sidebar\" id=\"root\">\r\n\t<li class=\"panel\"><a href=\"#default\" class=\"folder\" onclick=\"toggle(this)\"></a><b>Site map</b>\r\n\t\t<ol>\r\n\t\t\t<li class=\"sidebar\"><a href=\"introduction.html\">Introduction</a></li>\r\n\t\t\t<li class=\"panel\"><a href=\"#default\" class=\"folder\" onclick=\"toggle(this)\"></a><b>Developer References</b>\r\n\t\t\t\t<ol>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"quickstart.html\">Quick Start</a></li>\r\n\t\t\t\t\t<li class=\"panel\"><a href=\"#default\" class=\"folder\" onclick=\"toggle(this)\"></a><a href=\"manual.html\">Manual</a>\r\n\t\t\t\t\t\t<ol class=\"init-hidden\">\r\n\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#config\">antinstall-config.xml</a></li>\r\n\t\t\t\t\t\t\t<li class=\"panel\"><a href=\"#default\" class=\"folder\" onclick=\"toggle(this)\"></a><a href=\"manual.html#page\">Pages</a>\r\n\t\t\t\t\t\t\t\t<ol class=\"init-hidden\">\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#pagesplash\">Splash Page</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#pagetext\">Text Page</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#pagelicense\">License Page</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#pageinput\">Input Page</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#pageprogress\">Progress Page</a></li>\r\n\t\t\t\t\t\t\t\t</ol>\r\n\t\t\t\t\t\t\t</li>\r\n\t\t\t\t\t\t\t<li class=\"panel\"><a href=\"#default\" class=\"folder\" onclick=\"toggle(this)\"></a><a href=\"manual.html#inputtypes\">Input types</a>\r\n\t\t\t\t\t\t\t\t<ol class=\"init-hidden\">\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#app-root\">Application Root</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#checkbox\">Checkbox</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#comment\">Comment</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#date\">Date</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#directory\">Directory</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#file\">File</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#large-select\">Large Select</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#password\">Password Text</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#password-confirm\">Confirm Password</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#select\">Select</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#target\">Target</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#target-select\">Target Select</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#text\">Unvalidated Text</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#validated\">Validated Text</a></li>\r\n\t\t\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#extvalidated\">Externally Validated Text</a></li>\r\n\t\t\t\t\t\t\t\t</ol>\r\n\t\t\t\t\t\t\t</li>\r\n\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#extractor\">Self Extractor</a></li>\r\n\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#non-extractor\">Non Extractor</a></li>\r\n\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#scripts\">Start Scripts</a></li>\r\n\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#refs\">Dynamic References</a></li>\r\n\t\t\t\t\t\t\t<li class=\"sidebar\"><a href=\"manual.html#pagedisplay\">Page Displaying</a></li>\r\n\t\t\t\t\t\t</ol>\r\n\t\t\t\t\t</li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"installertask.html\">Installer Ant task</a></li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"validationofconfig.html\">Validation of config</a></li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"lookandfeels.html\">LookAndFeels</a> <br/>(inc screenshots)</li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"classpathresources.html\">Resources/Classpath issues</a></li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"i18n.html\">Internationalisation</a></li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"auto.html\">Automated installs</a></li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"installtypes.html\">Multiple install types</a></li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"posttargets.html\">Post display targets</a></li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"icons.html\">Button Icons</a></li>\r\n\t\t\t\t\t<li class=\"sidebar\"><a href=\"antinstall-config-example.html\">Example antinstall-config.xml</a></li>\r\n\t\t\t\t</ol>\r\n\t\t\t</li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"manual-ant.html\">Ant Manual</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"antlinks.html\">Ant links</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"userusage.html\">User usage</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"licenses.html\">Licenses</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"potentialuses.html\">Potential uses</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"roadmap.html\">Road Map</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"wanted.html\">Wanted</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"dtds.html\">DTDs</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"changelog.html\">Changelog</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"http://sourceforge.net/projects/antinstaller\">Project page on SourceForge</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"java2html/antinstaller/index.html\">Java2HTML (main)</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"java2html/ext/index.html\">Java2HTML (extensions)</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"http://antinstaller.cvs.sourceforge.net/antinstaller\">Public CVS over HTTP</a></li>\r\n\t\t\t<li class=\"sidebar\"><a href=\"http://sourceforge.net/sendmessage.php?touser=616485\">Contact AntInstaller Admin</a></li>\r\n\t\t</ol>\r\n\t</li>\r\n</ol>\r\n<br/>\r\n<br/>\r\n</div>\r\n\r\n\r\n<!-- menu end [segment-end]-->\r\n</div>\r\n<div id=\"contents-options\">\r\n<a id=\"toggle\" href=\"#\" onclick=\"toggleMenu(); return false;\">show menu</a>\r\n</div>\r\n\r\n</div>\r\n</body>\r\n</html>\r\n");
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
