package professores.servlets;

import java.io.File;
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
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import professores.repository.ProfessoresRepository;
import utils.Conector;
import utils.Headers;

@WebServlet(name = "UploadFoto", urlPatterns = {"/foto/upload"})
public class UploadFoto extends HttpServlet {

	protected void processRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		Connection c;
		
		try {
			c = Conector.getConnection();
			ProfessoresRepository pr = new ProfessoresRepository(c);
			
			String idProf = null, fileName = null;
			
			if (ServletFileUpload.isMultipartContent(req)) {
            try {
                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);
				
				for (FileItem item : multiparts) {
                    if ("idProf".equals(item.getFieldName())) {
						idProf = item.getString();
                    }
                }
				
                for (FileItem item : multiparts) {
                    if (!item.isFormField() && "foto".equals(item.getFieldName())) {
						fileName = "upload" + idProf + ".jpg";
						File img = new File(req.getServletContext().getRealPath("uploads")+ File.separator + fileName);
						if(img.exists()) img.delete();
						System.out.println(img);
                        item.write(img);
						if(pr.inserirFoto(idProf, fileName)){
							res.setStatus(200);
							out.println("<sucesso><mensagem>Upload realizado com sucesso</mensagem></sucesso>");
						}
                    }
                }
            } catch (Exception ex) {
				res.setStatus(500);
                out.println("<erro><mensagem>Falha no upload" + ex + "</mensagem></erro>");
            }
 
        } else {
            res.setStatus(400);
            out.println("<erro><mensagem>Tipo da requisicao invalido</mensagem></erro>");
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
