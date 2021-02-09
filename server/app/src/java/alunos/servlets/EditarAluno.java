package alunos.servlets;

import alunos.repository.AlunosRepository;
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
import utils.Conector;
import utils.Headers;
import utils.autenticador.Autenticador;
import utils.autenticador.Cargos;

@WebServlet(name = "EditarAluno", urlPatterns = {"/aluno/editar"})
public class EditarAluno extends HttpServlet {

	protected void processRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		Connection c;
		PrintWriter out = res.getWriter();
		try {
			c = Conector.getConnection();
			AlunosRepository r = new AlunosRepository(c);
			Autenticador x = new Autenticador(req, res);
			if (x.getCargoLogado() == Cargos.ALUNO) {
				Long idAluno = (Long) x.getIdLogado();
				String id = Long.toString(idAluno);
				String nome = req.getParameter("nome");
				String uf = req.getParameter("idUf");
				String municipio = req.getParameter("idMunicipio");
				String preferenciaPreco = req.getParameter("preferenciaPreco");
				String preferenciaLocal = req.getParameter("preferenciaLocal");
				String preferenciaNumeroAlunos = req.getParameter("preferenciaNumeroAlunos");
				String assinante = req.getParameter("assinante");
				String dataFimAssinatura = req.getParameter("dataFimAssinatura");

				try {
					boolean sucesso = r.editar(id, nome, municipio, uf, preferenciaPreco, preferenciaLocal, preferenciaNumeroAlunos, assinante, dataFimAssinatura);
					if(sucesso) {
						res.setStatus(200);
						out.println("<sucesso><mensagem>Dados alterados com sucesso</mensagem></sucesso>");
					} else { 
						out.println("<erro><mensagem>Alteração falhou</mensagem></erro>");
					}
				} catch (ParseException ex) {
					res.setStatus(422);
					out.println("<erro><mensagem>Erro interno</mensagem></erro>");
				}
			} else {
				res.setStatus(403);
				out.println("<erro><mensagem>Você não tem permissão para fazer isso</mensagem></erro>");
			}
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