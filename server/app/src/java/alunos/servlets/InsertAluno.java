package alunos.servlets;

import alunos.repository.AlunosRepository;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.Conector;

@WebServlet(name = "InsertAluno", urlPatterns = {"/aluno/insert"})
public class InsertAluno extends HttpServlet {

	protected void processRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		Connection c;
		try {
			c = Conector.getConnection();
			PrintWriter out = res.getWriter();
			out.println("Conexão: " + c);
			AlunosRepository r = new AlunosRepository(c);

			String email = req.getParameter("email");
			
			out.println("email: " + email);

			try {
				out.println("aaa");
				boolean sucesso = r.insert(email, "123", "nome", "1", "1", "1", "1", "1", "false", "11/11/2020");
				if(sucesso) out.println("Inserido com sucesso");
				else out.println("Não foi possível inserir");
			} catch (NoSuchAlgorithmException | InvalidKeySpecException | SQLException | ParseException ex) {
				Logger.getLogger(InsertAluno.class.getName()).log(Level.SEVERE, null, ex);
			}
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(InsertAluno.class.getName()).log(Level.SEVERE, null, ex);
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
