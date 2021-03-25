package mensagens.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mensagens.model.MensagensModel;
import mensagens.repository.MensagensRepository;
import utils.Conector;
import utils.autenticador.Autenticador;
import utils.autenticador.Cargos;

@WebServlet(name = "ChatProf", urlPatterns = {"/chat/professor"})
public class ChatProf extends HttpServlet {

	protected void processRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		Connection c;
		
		Autenticador aut = new Autenticador(req, res);
		
		if(aut.getCargoLogado() == Cargos.PROFESSOR){
			try {
				c = Conector.getConnection();
				MensagensRepository mr = new MensagensRepository(c);
				
				String id = String.valueOf(aut.getIdLogado());

				List<Long> r = mr.quemProfFalou(id);
				out.println("<alunos>");
				for(int i = 0; i < r.size(); i++){
					PreparedStatement ps = c.prepareStatement("SELECT * FROM aluno WHERE \"id-aluno\" = ?");
					ps.setLong(1, r.get(i));
					ResultSet rs = ps.executeQuery();
					rs.next();
					out.println("<aluno>");
					out.println("<idAluno>" + r.get(i) + "</idAluno>");
					out.println("<nome>" + rs.getString("nome") + "</nome>");
					out.println("<foto>" + rs.getString("foto") + "</foto>");
					out.println("</aluno>");
				}
				out.println("</alunos>");
				c.close();
			} catch (ClassNotFoundException | SQLException ex) {
				res.setStatus(500);
				out.println("<erro><mensagem>Erro na interação com o servidor</mensagem></erro>");
			}
		} else{
			res.setStatus(403);
			out.println("<erro><mensagem>Você não tem permissão para fazer isso</mensagem></erro>");
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
