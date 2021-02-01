package apresentacao.servlets;

import alunos.repository.AlunosRepository;
import apresentacao.repository.ApresentacaoRepository;
import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet(name = "Avaliar", urlPatterns = {"/apresentacao/avaliar"})
public class Avaliar extends HttpServlet {

	protected void processRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		Connection c;
		Headers.XMLHeaders(req, res);
		try {
			c = Conector.getConnection();
			ApresentacaoRepository r = new ApresentacaoRepository(c);
			AlunosRepository a = new AlunosRepository(c);
			Autenticador aut = new Autenticador(req, res);


			String idProf = req.getParameter("idProf");
			String nota = req.getParameter("nota");
			Long idAluno = (Long) aut.getIdLogado();

			if (aut.getCargoLogado() == Cargos.ALUNO && a.checarAssinante(idAluno)){
				if(r.avaliar(idAluno, idProf, nota) && r.atualizarAvaliacao(idProf, nota)){
					res.setStatus(200);
					out.println("<sucesso><mensagem>Avaliacao submetida</mensagem></sucesso>");
				} else {
					out.println("<erro><mensagem>Ocorreu um erro, confira se voce ja avaliou este professor</mensagem></erro>");
				}
			} else {
				res.setStatus(403);
				out.println("<erro><mensagem>Voce nao tem permissao para fazer isso</mensagem></erro>");
			}
		} catch (ClassNotFoundException | SQLException ex) {
			res.setStatus(500);
			out.println("<erro><mensagem>Erro na interacao com o servidor</mensagem></erro>");
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