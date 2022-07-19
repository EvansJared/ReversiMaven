package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Game;

/**
 * Servlet implementation class GameServlet
 */

public class GameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request  the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException      if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String locStr = request.getParameter("loc");
		// String locStr2 = request.getParameter("x");

		String newGame = request.getParameter("newGame");

		if (newGame != null) {
			request.getSession().setAttribute("game", new Game());
		} else if (locStr != null) {
			Matcher matcher = Pattern.compile("\\d+").matcher(locStr);
			List<Integer> list = new ArrayList<Integer>();
			while (matcher.find()) {
				list.add(Integer.parseInt(matcher.group()));
			}
			((Game) request.getSession(true).getAttribute("game")).placeMark(list.get(1), list.get(0));

		}

		request.getRequestDispatcher("/index.jsp").forward(request, response);

	}

}
