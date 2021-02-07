package professores.servlets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.Conector;

@WebServlet(name = "ConsultarFoto", urlPatterns = {"/foto/consultar"})
public class ConsultarFoto extends HttpServlet {

	protected void processRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		Connection c;
		
		String foto = null;
		String idProf = req.getParameter("idProf");
		Long idParsed = Long.parseLong(idProf);
		
		PreparedStatement ps;
		try {
			c = Conector.getConnection();
			
			ps = c.prepareStatement("SELECT foto FROM professor WHERE \"id-prof\" = ?");
			ps.setLong(1, idParsed);
			ResultSet rs = ps.executeQuery();
			rs.next();
			foto = rs.getString("foto");
			
			String path = req.getServletContext().getRealPath("uploads")+ File.separator;

			File files = new File(path);
			res.setContentType("image/png");

			for (String file : files.list()) {
				if(file.equals(foto)){
					File f = new File(path + file);
					BufferedImage bi = ImageIO.read(f);
					OutputStream out = res.getOutputStream();
					ImageIO.write(bi, "png", out);
					out.close();
				}
			}
		} catch (ClassNotFoundException | SQLException ex) {
			res.setStatus(500);
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
