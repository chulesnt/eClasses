package alunos.servlets;

import alunos.repository.AlunosRepository;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.Conector;
import utils.Headers;
import utils.autenticador.Autenticador;
import utils.autenticador.Cargos;

@WebServlet(name = "AlterarMaterias", urlPatterns = {"/aluno/materias"})
public class AlterarMaterias extends HttpServlet {

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
				Map<String, String> materias = new LinkedHashMap<>();
				for (int i = 1; 1 > 0; i++) {
					if (req.getParameter("materia"+i) != null) 
						materias.put("materia"+i, req.getParameter("materia"+i));
					else
						break;
				}
				boolean sucesso = r.alterarMaterias(idAluno, materias);
				if(sucesso) {
					res.setStatus(200);
					out.println("<sucesso><mensagem>Matérias alteradas com sucesso</mensagem></sucesso>");
				} else { 
					out.println("<erro><mensagem>Alteração de matérias falhou</mensagem></erro>");
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