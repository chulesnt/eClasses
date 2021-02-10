package professores.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import professores.repository.ProfessoresRepository;
import utils.Conector;
import utils.Headers;

@WebServlet(name = "CadastrarProfessor", urlPatterns = {"/professor/cadastrar"})
public class CadastrarProfessor extends HttpServlet {

	protected void processRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		Connection c;
		PrintWriter out = res.getWriter();
		try {
			c = Conector.getConnection();
			ProfessoresRepository r = new ProfessoresRepository(c);
			String email = req.getParameter("email");
			String nome = req.getParameter("nome");
			String senha = req.getParameter("senha");
			String uf = req.getParameter("idUf");
			String municipio = req.getParameter("idMunicipio");
			String materia = req.getParameter("idMateria");


			try {
				boolean sucesso = r.cadastrar(email, senha, nome, municipio, uf, materia);
				if(sucesso) {
					res.setStatus(200);
					out.println("<sucesso><mensagem>Cadastro realizado com sucesso</mensagem></sucesso>");
				} else {
					out.println("<erro><mensagem>Cadastro falhou</mensagem></erro>");
				}
			} catch (NoSuchAlgorithmException | InvalidKeySpecException | ParseException ex) {
				res.setStatus(422);
				out.println("<erro><mensagem>Erro interno</mensagem></erro>");
			}
			c.close();
		} catch (ClassNotFoundException | SQLException ex) {
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