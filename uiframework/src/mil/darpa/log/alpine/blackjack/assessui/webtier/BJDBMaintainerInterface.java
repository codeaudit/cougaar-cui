package mil.darpa.log.alpine.blackjack.assessui.webtier;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.rmi.RemoteException;
import java.util.*;

import mil.darpa.log.alpine.blackjack.assessui.middletier.BJDBMaintainer;
import mil.darpa.log.alpine.blackjack.assessui.middletier.BJDBMaintainerHome;

public class BJDBMaintainerInterface extends HttpServlet
{
    private static final String CONTENT_TYPE = "text/xml";

    /**Initialize global variables*/
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
    }

    /**Process the HTTP Get request*/
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String updateXML = "";
        try
        {
            updateXML = request.getParameter("updateXML");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        PrintWriter out = response.getWriter();
        out.println("Got updateXML string!");
        updateDatabase(updateXML);
        out.close();
    }

    /**Process the HTTP Post request*/
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
        throws ServletException, IOException
    {
        String updateXML = "";
        try
        {
            updateXML = request.getParameter("updateXML");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
//        out.println("<?xml version=\"1.0\"?>");
        out.println("Got updateXML string!");
        updateDatabase(updateXML);
        out.close();
    }

    private void updateDatabase(String updateXML)
    {
        try
        {
            BJDBMaintainer bjdbMaintainer = getBJDBMaintainer();
            bjdbMaintainer.updateDatabase(updateXML);
            bjdbMaintainer.remove();
        }
        catch (Exception ex)
        {
            System.err.println("Caught an unexpected exception!");
            ex.printStackTrace();
        }
    }

    private BJDBMaintainerHome home = null;
    private BJDBMaintainer getBJDBMaintainer()
        throws NamingException, RemoteException, CreateException
    {
        if (home == null)
        {
            InitialContext rootCtx = new InitialContext();
            Object objref = rootCtx.lookup("BJDBMaintainer");
            home = (BJDBMaintainerHome)PortableRemoteObject.narrow(objref,
                                                BJDBMaintainerHome.class);
        }
        return home.create();
    }
}
