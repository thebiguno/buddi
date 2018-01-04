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


public class manual_0002dprint extends ObjectJspBase {


    static {
    }
    public manual_0002dprint( ) {
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

            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/manual-print.jsp";from=(0,0);to=(13,0)]
                out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\r\n<html>\r\n<head>\r\n  <title>Ant Installer</title>\r\n  <link href=\"style-print.css\" type=\"text/css\" rel=\"stylesheet\">\r\n  <link rel=\"SHORTCUT ICON\" href=\"images/antinstaller-icon.png\">\r\n  <meta name=\"keywords\"\r\n content=\"Ant, installer, AntInstaller, gui, console, input, parameters, properties, swing, user interface, validation, configuration\">\r\n</head>\r\n<body>\r\n\t<div class=\"tpheadertitle\">AntInstaller</div>\r\n\t<div class=\"tpcontent\">\r\n<!-- JSP start -->\r\n");
            // end
            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/manual-print.jsp";from=(13,28);to=(14,0)]
                out.write("\r\n");
            // end
            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/manual-print.jsp";from=(14,30);to=(15,0)]
                out.write("\r\n");
            // end
            // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/manual-print.jsp";from=(15,2);to=(34,0)]
                
                
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
            // end
            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/manual-print.jsp";from=(34,2);to=(39,0)]
                out.write("\r\n\t</div>\r\n</div>\r\n</body>\r\n</html>\r\n");
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
