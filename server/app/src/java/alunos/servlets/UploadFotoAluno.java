package alunos.servlets;

import alunos.repository.AlunosRepository;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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
import utils.Conector;

@WebServlet(name = "UploadFotoAluno", urlPatterns = {"/aluno/foto/upload"})
public class UploadFotoAluno extends HttpServlet {

	protected void processRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		Connection c;
		
		try {
			c = Conector.getConnection();
			AlunosRepository pr = new AlunosRepository(c);
			
			String idAluno = null, fileName = null;
			
			if (ServletFileUpload.isMultipartContent(req)) {
            try {
                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);
				
				for (FileItem item : multiparts) {
                    if ("idAluno".equals(item.getFieldName())) {
						idAluno = item.getString();
                    }
                }
				
                for (FileItem item : multiparts) {
                    if (!item.isFormField() && "foto".equals(item.getFieldName())) {
						if(item.getSize() >= 5000000){
							res.setStatus(500);
							out.println("<erro><mensagem>A imagem excede o tamanho permitido</mensagem></erro>");
							return;
						}
						if(!item.getContentType().equals("image/jpeg") && !item.getContentType().equals("image/jpg") && !item.getContentType().equals("image/png")){
							res.setStatus(500);
							out.println("<erro><mensagem>Tipo não permitido</mensagem></erro>");
							return;
						}
						fileName = "upload-al-" + idAluno + ".jpg";
						File img = new File(req.getServletContext().getRealPath("uploads")+ File.separator + fileName);
						if(img.exists()) img.delete();
                        item.write(img);
						if(pr.inserirFoto(idAluno, fileName)){
							res.setStatus(200);
							out.println("<sucesso><mensagem>Upload realizado com sucesso</mensagem></sucesso>");
						}
						else{
							res.setStatus(500);
							out.println("<erro><mensagem>Não foi possível fazer o upload da foto</mensagem></erro>");
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
