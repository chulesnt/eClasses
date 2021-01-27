package professores.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import professores.repository.ProfessoresRepository;
import utils.Conector;
import utils.Headers;
import utils.autenticador.Autenticador;

@WebServlet(name = "LogarProfessor", urlPatterns = {"/professor/logar"})
public class LogarProfessor extends HttpServlet {

	protected void processRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException, ClassNotFoundException, SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
		
		PrintWriter out = res.getWriter();
		Connection c = Conector.getConnection();
		Headers.XMLHeaders(req, res);
		ProfessoresRepository r = new ProfessoresRepository(c);
		Autenticador aut = new Autenticador(req, res);

		String senha = req.getParameter("senha");
		String id = req.getParameter("id");

		if(r.logar(req, res, id, senha)){
			res.setStatus(200);
			out.println("<sucesso><mensagem>Logado com sucesso como " + aut.getCargoLogado() + "</mensagem></sucesso>");
		} else {
			res.setStatus(400);
			out.println("<erro><mensagem>Senha incorreta</mensagem></erro>");
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
		try {
			processRequest(request, response);
		} catch (ClassNotFoundException | SQLException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
			response.setStatus(400);
			response.getWriter().println("<erro><mensagem>Erro interno</mensagem></erro>");
		}
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
		try {
			processRequest(request, response);
		} catch (ClassNotFoundException | SQLException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
			response.setStatus(400);
			response.getWriter().println("<erro><mensagem>Erro interno</mensagem></erro>");
		}
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