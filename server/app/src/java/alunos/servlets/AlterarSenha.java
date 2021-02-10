package alunos.servlets;

import alunos.repository.AlunosRepository;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.Conector;
import utils.Headers;
import utils.autenticador.Autenticador;
import utils.autenticador.Cargos;

@WebServlet(name = "AlterarSenhaAluno", urlPatterns = {"/aluno/senha"})
public class AlterarSenha extends HttpServlet {

	protected void processRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		Connection c;
		PrintWriter out = res.getWriter();
		try {
			c = Conector.getConnection();
			AlunosRepository r = new AlunosRepository(c);
			Autenticador x = new Autenticador(req, res);
			String senhaAntiga = req.getParameter("senhaAntiga");
			String senhaNova = req.getParameter("senhaNova");
			if (x.getCargoLogado() == Cargos.ALUNO) {
				if (r.alterarSenha((Long) x.getIdLogado(), senhaAntiga, senhaNova)) {
					res.setStatus(200);
					out.println("<sucesso><mensagem>Senha alterada com sucesso</mensagem></sucesso>");
				} else {
					out.println("<erro><mensagem>A senha atual está incorreta</mensagem></erro>");
				}
			} else {
				res.setStatus(403);
				out.println("<erro><mensagem>Você não tem permissão para fazer isso</mensagem></erro>");
			}
			c.close();
		} catch (ClassNotFoundException | SQLException ex) {
			res.setStatus(500);
			out.println("<erro><mensagem>Erro na interação com o servidor</mensagem></erro>");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
			res.setStatus(422);
			out.println("<erro><mensagem>Erro interno</mensagem></erro>");
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