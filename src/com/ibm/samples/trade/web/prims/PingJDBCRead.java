//
// "This sample program is provided AS IS and may be used, executed, copied and modified without royalty payment by customer (a) for its own
// instruction and study, (b) in order to develop applications designed to run with an IBM WebSphere product, either for customer's own internal use 
// or for redistribution by customer, as part of such an application, in customer's own products. " 
//
// (C) COPYRIGHT International Business Machines Corp., 2005
// All Rights Reserved * Licensed Materials - Property of IBM
//

package com.ibm.samples.trade.web.prims;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.ibm.samples.trade.*;
import com.ibm.samples.trade.direct.*;
import com.ibm.samples.trade.util.*;

/**
 * 
 * PingJDBCReadPrepStmt uses a prepared statement for database read access. 
 * This primative uses {@link com.ibm.samples.trade.direct.TradeDirect} to set the price of a random stock
 * (generated by {@link com.ibm.websphere.samples.trade.Trade_Config}) through the use of prepared statements. 
 * 
 */

public class PingJDBCRead extends HttpServlet
{

	private static String initTime;
	private static int hitCount;

	/**
	 * forwards post requests to the doGet method
	 * Creation date: (11/6/2000 10:52:39 AM)
	 * @param res javax.servlet.http.HttpServletRequest
	 * @param res2 javax.servlet.http.HttpServletResponse
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException
	{
		doGet(req, res);
	}
	/**
	* this is the main method of the servlet that will service all get requests.
	* @param request HttpServletRequest
	* @param responce HttpServletResponce
	**/
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException
	{
		res.setContentType("text/html");
		java.io.PrintWriter out = res.getWriter();
		String symbol=null;
		StringBuffer output = new StringBuffer(100);

		try
			{
			//TradeJDBC uses prepared statements so I am going to make use of it's code.
			TradeDirect trade = new TradeDirect();
			symbol = TradeConfig.rndSymbol();
			
			QuoteDataBean quoteData = null;
			int iter = TradeConfig.getPrimIterations();
			for (int ii = 0; ii < iter; ii++) {
				quoteData = trade.getQuote(symbol);
			}

			output.append(
				"<html><head><title>Ping JDBC Read w/ Prepared Stmt.</title></head>"
					+ "<body><HR><FONT size=\"+2\" color=\"#000066\">Ping JDBC Read w/ Prep Stmt:</FONT><HR><FONT size=\"-1\" color=\"#000066\">Init time : "
					+ initTime);
                        hitCount++;
			output.append("<BR>Hit Count: " + hitCount);
			output.append(
				"<HR>Quote Information <BR><BR>: "
					+ quoteData.toHTML());
			output.append("<HR></body></html>");
			out.println(output.toString());
		}
		catch (Exception e)
		{
			Log.error(
				e,
				"PingJDBCRead w/ Prep Stmt -- error getting quote for symbol",
				symbol);
			res.sendError(500, "PingJDBCRead Exception caught: " + e.toString());
		}

	}
	/** 
	 * returns a string of information about the servlet
	 * @return info String: contains info about the servlet
	 **/
	public String getServletInfo()
	{
		return "Basic JDBC Read using a prepared statment, makes use of TradeJDBC class";
	}
	/**
	* called when the class is loaded to initialize the servlet
	* @param config ServletConfig:
	**/
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		hitCount = 0;
		initTime = new java.util.Date().toString();
	}
}