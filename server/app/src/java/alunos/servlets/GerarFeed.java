package alunos.servlets;

import alunos.repository.AlunosRepository;
import consultas.repository.ConsultasRepository;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import professores.model.ProfessorModel;
import utils.Conector;
import utils.autenticador.Autenticador;
import utils.autenticador.Cargos;

@WebServlet(name = "GerarFeed", urlPatterns = {"/aluno/feed"})
public class GerarFeed extends HttpServlet {
	protected void processRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		Connection c;
		
		try {
			c = Conector.getConnection();
			AlunosRepository ar = new AlunosRepository(c);
			ConsultasRepository cr = new ConsultasRepository(c);
			Autenticador aut = new Autenticador(req, res);
			double prefPreco = 0;
			int idPrefLocal = 0, idMunicipio = 0, idUf = 0, prefAlunos = 0;
			List idMaterias = new ArrayList();
		
			if(aut.getCargoLogado() == Cargos.ALUNO){
				String idAluno = String.valueOf(aut.getIdLogado());
				String orderBy = req.getParameter("orderBy");
				String order = req.getParameter("order");
				
				PreparedStatement ps = c.prepareStatement("SELECT * FROM aluno WHERE \"id-aluno\" = ?");
				ps.setLong(1, Long.parseLong(idAluno));

				ResultSet rs = ps.executeQuery();
				if(!rs.next()){
					res.setStatus(400);
					out.println("<erro><mensagem>Aluno não encontrado</mensagem></erro>");
					return;
				}
				prefPreco = rs.getDouble("preferencia-preco");
				idPrefLocal = rs.getInt("id-preferencia-local");
				idMunicipio = rs.getInt("id-municipio");
				idUf = rs.getInt("id-uf");
				prefAlunos = rs.getInt("preferencia-numero-alunos");
				
				ps = c.prepareStatement("SELECT \"id-materia\" FROM alunopreferenciasmaterias WHERE \"id-aluno\" = ?");
				ps.setLong(1, Long.parseLong(idAluno));
				
				rs = ps.executeQuery();
				while(rs.next()){
					idMaterias.add(String.valueOf(rs.getInt("id-materia")));
				}

				List<ProfessorModel> r = ar.gerarFeed(prefPreco, idPrefLocal, idMunicipio, idUf, prefAlunos, idMaterias, orderBy, order);
				out.println("<feed>");
				for(int i = 0; i < r.size(); i++){
					out.println("<professor>");
					out.println("<idProf>" + r.get(i).getIdProf() + "</idProf>");
					out.println("<emailProf>" + r.get(i).getEmailProf() + "</emailProf>");
					out.println("<senha>" + r.get(i).getSenha() + "</senha>");
					out.println("<nome>" + r.get(i).getNome() + "</nome>");
					out.println("<descricaoApresentacao>" + r.get(i).getDescricaoApresentacao() + "</descricaoApresentacao>");
					out.println("<avaliacao>" + r.get(i).getAvaliacao() + "</avaliacao>");
					out.println("<precoHora>" + r.get(i).getPrecoHora() + "</precoHora>");
					out.println("<numeroAvaliacoes>" + r.get(i).getNumeroAvaliacoes() + "</numeroAvaliacoes>");
					out.println("<municipio>" + cr.getMunicipio(r.get(i).getIdMunicipio()) + "</municipio>");
					out.println("<uf>" + cr.getUf(r.get(i).getIdUf()) + "</uf>");
					out.println("<materia>" + cr.getMateria(r.get(i).getIdMateria()) + "</materia>");
					out.println("<numeroAlunosMin>" + r.get(i).getNumeroAlunosMin() + "</numeroAlunosMin>");
					out.println("<numeroAlunosMax>" + r.get(i).getNumeroAlunosMax() + "</numeroAlunosMax>");
					out.println("<foto>" + r.get(i).getFoto() + "</foto>");
					out.println("</professor>");
				}
				out.println("</feed>");
			} else{
				res.setStatus(403);
				out.println("<erro><mensagem>Você não tem permissão para fazer isso</mensagem></erro>");
			}
			c.close();
		} catch (ClassNotFoundException | SQLException ex) {
			res.setStatus(500);
			out.println("<erro><mensagem>Erro na interação com o servidor + " + ex + "</mensagem></erro>");
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
