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


public class contents extends ObjectJspBase {


    static {
    }
    public contents( ) {
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

            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/contents.jsp";from=(0,0);to=(13,0)]
                out.write("<html>\r\n<head>\r\n  <title>Ant Installer</title>\r\n  <link href=\"css/nav.css\" rel=\"stylesheet\" type=\"text/css\">\r\n  <link href=\"style.css\" type=\"text/css\" rel=\"stylesheet\">\r\n  <meta name=\"keywords\"\r\n content=\"Ant, installer, AntInstall, gui, console, input, parameters, properties, swing, user interface, valiation, configuration\">\r\n <script type=\"text/javascript\" src=\"js/menu.js\"></script>\r\n <script type=\"text/javascript\" src=\"js/sstree.js\"></script>\r\n</head>\r\n<body onload='collapseAll([\"ol\"]); openBookMark();'>\r\n<a href=\"index.html\" alt=\"home\" target=\"_content\">\r\n<img src=\"images/ant-install-small.png\"  target=\"_content\" align=\"baseline\"/></a><br/>\r\n");
            // end
            // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/contents.jsp";from=(13,0);to=(13,56)]
                {
                    String _jspx_qStr = "";
                    pageContext.include("contents-include.html" + _jspx_qStr);
                }
            // end
            // HTML // begin [file="/home/teknopaul/workspace/AntInstaller/web/home/teknopaul/workspace/AntInstaller/web/contents.jsp";from=(13,56);to=(15,0)]
                out.write("\r\n</body>\r\n");
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
