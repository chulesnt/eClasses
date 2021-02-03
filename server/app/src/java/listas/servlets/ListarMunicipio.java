package listas.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import listas.repository.ListasRepository;
import utils.Conector;
import utils.Headers;

@WebServlet(name = "ListarMunicipio", urlPatterns = {"/listar/municipio"})
public class ListarMunicipio extends HttpServlet {

	protected void processRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		PrintWriter out = res.getWriter();
		Headers.XMLHeaders(req, res);
		try {
			String xml;
			Connection c = Conector.getConnection();
			String siglaUf = req.getParameter("uf");
			ListasRepository r = new ListasRepository(c);
			xml = r.listarMunicipio(siglaUf);
			res.setStatus(200);
			out.println(xml);
		} catch (SQLException | ClassNotFoundException ex) {
			res.setStatus(500);
			out.println("<erro><mensagem>Erro na interação com o servidor</mensagem></erro>");
		}
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

}