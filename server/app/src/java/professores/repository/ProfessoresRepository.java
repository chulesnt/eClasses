package professores.repository;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.Hasher;
import utils.autenticador.Autenticador;
import utils.autenticador.Cargos;


public class ProfessoresRepository {
	private Connection c;
	
	public ProfessoresRepository(Connection c){
		this.c = c;
	}
	
	public boolean cadastrar(String email, String senha, String nome, String idMunicipio, String idUf, String idMateria) throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException, ParseException{
		String hashedSenha = Hasher.hash(senha);
		
		String desc = "";
		float avaliacao = 0;
		int numAval = 0;
		float preco = 0;
		int numeroMin = 1;
		int numeroMax = 50;
		
		
		PreparedStatement ps = c.prepareStatement("INSERT INTO professor (\"email-prof\", senha, nome, \"id-municipio\", \"id-uf\", \"id-materia\", \"descricao_apresentacao\", avaliacao, \"numero-avaliacoes\", \"preco-hora\", \"numero-alunos-min\", \"numero-alunos-max\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		ps.setString(1, email);
		ps.setString(2, hashedSenha);
		ps.setString(3, nome);
		ps.setInt(4, Integer.parseInt(idMunicipio));
		ps.setInt(5, Integer.parseInt(idUf));
		ps.setInt(6, Integer.parseInt(idMateria));
		ps.setString(7, desc);
		ps.setFloat(8, avaliacao);
		ps.setInt(9, numAval);
		ps.setFloat(10, preco);
		ps.setInt(11, numeroMin);
		ps.setInt(12, numeroMax);
		
		
		return ps.executeUpdate() != 0;
	}
	
	public boolean logar(HttpServletRequest req, HttpServletResponse res, String email, String senha) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException{
		Autenticador aut = new Autenticador(req, res);
		
		PreparedStatement pt = c.prepareStatement("SELECT * FROM professor WHERE \"email-prof\" = ?");
		pt.setString(1, email);
		ResultSet rs = pt.executeQuery();
		rs.next();
		Long idLong = rs.getLong("id-prof");
		
		if (Hasher.validar(senha, rs.getString("senha"))) {
			aut.logar(idLong, Cargos.PROFESSOR, false);
			return true;
		}
		return false;
	}
	
	public boolean editar(String idProf, String nome, String idMunicipio, String idUf, String desc, String preco, String	idMateria, String numAMin, String numAMax, String link) throws SQLException, ParseException {
		int adcs = 0;
		int cont = 1;
		boolean[] pars = new boolean[10];
		
		
		
		for (int i = 0; i < 10; i++) {
			pars[i] = false;
		}
		
		
		String query = "UPDATE professor SET";
		if (!"".equals(nome)) {
			query += " nome= ?";
			adcs++;
			pars[0] = true;
		}
		if (!"".equals(idMunicipio)) {
			if (adcs > 0) {
				query += ",";
			}
			query += " \"id-municipio\"= ?";
			adcs++;
			pars[1] = true;
		}
		if (!"".equals(idUf)) {
			if (adcs > 0) {
				query += ",";
			}
			query += " \"id-uf\"= ?";
			adcs++;
			pars[2] = true;
		}
		if (!"".equals(desc)) {
			if (adcs > 0) {
				query += ",";
			}
			query += " \"descricao_apresentacao\"= ?";
			adcs++;
			pars[3] = true;
		}

		pars[4] = true;
		
		if (!"".equals(link)) {
			if (adcs > 0) {
				query += ",";
			}
			query += " \"link-video\"= ?";
			adcs++;
			pars[5] = true;
		}
			
		if (!"".equals(preco)) {
			if (adcs > 0) {
				query += ",";
			}
			query += " \"preco-hora\"= ?";
			adcs++;
			pars[6] = true;
		}
		if (!"".equals(idMateria)) {
			if (adcs > 0) {
				query += ",";
			}
			query += " \"id-materia\"= ?";
			adcs++;
			pars[7] = true;
		}
		if (!"".equals(numAMin)) {
			if (adcs > 0) {
				query += ",";
			}
			query += " \"numero-alunos-min\"= ?";
			adcs++;
			pars[8] = true;
		}
		if (!"".equals(numAMax)) {
			if (adcs > 0) {
				query += ",";
			}
			query += " \"numero-alunos-max\"= ?";
			adcs++;
			pars[9] = true;
		}
		

		query += " WHERE \"id-prof\" = ?";

		
		
		PreparedStatement ps = c.prepareStatement(query);

		
		if (pars[0]) {
			ps.setString(cont, nome);
			cont++;
		}
		if (pars[1]) {
			ps.setInt(cont, Integer.parseUnsignedInt(idMunicipio));
			cont++;
		}
		if (pars[2]) {
			ps.setInt(cont, Integer.parseUnsignedInt(idUf));
			cont++;
		}
		if (pars[3]) {
			ps.setString(cont, desc);
			cont++;
		}
		if (pars[5]) {
			ps.setString(cont, link);
			cont++;
		}	
		if (pars[6]) {
			ps.setFloat(cont, Float.parseFloat(preco));
			cont++;
		}
		if (pars[7]) {
			ps.setInt(cont, Integer.parseUnsignedInt(idMateria));
			cont++;
		}
		if (pars[8]) {
			ps.setInt(cont, Integer.parseUnsignedInt(numAMin));
			cont++;
		}
		if (pars[9]) {
			ps.setInt(cont, Integer.parseUnsignedInt(numAMax));
			cont++;
		}
		
		
		

		ps.setLong(cont, Long.parseLong(idProf));

		int sucesso = ps.executeUpdate();

		return sucesso != 0;
	}
	
	public boolean alterarSenha(Long id, String senhaAntiga, String senhaNova) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {	
		
		PreparedStatement pt = c.prepareStatement("SELECT * FROM professor WHERE \"id-prof\" = ?");
		pt.setLong(1, id);
		ResultSet rs = pt.executeQuery();
		rs.next();
		
		if (Hasher.validar(senhaAntiga, rs.getString("senha"))) {
			String hashSenha = null;
			hashSenha = Hasher.hash(senhaNova);
			String query = "UPDATE professor SET senha= ? WHERE \"id-prof\" = ?";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, hashSenha);
			ps.setLong(2, id);
			int sucesso = ps.executeUpdate();
			return sucesso != 0;
		}
		return false;
	}
	
	public String consultarPorId(String id) throws SQLException {
		String xml = "<professor>";
		PreparedStatement ps = c.prepareStatement("SELECT * FROM professor WHERE \"id-prof\" = ?");
		Long idParsed = Long.parseLong(id);
		ps.setLong(1, idParsed);
		ResultSet rs = ps.executeQuery();
		rs.next();
		xml += "<id>" + idParsed + "</id>";
		xml += "<nome>" + rs.getString("nome") + "</nome>";
		xml += "<email>" + rs.getString("email-prof") + "</email>";
		xml += "<id-municipio>" + rs.getInt("id-municipio") + "</id-municipio>";
		xml += "<id-uf>" + rs.getInt("id-uf") + "</id-uf>";
		xml += "<descricao-apresentacao>" + rs.getString("descricao_apresentacao") + "</descricao-apresentacao>";
		xml += "<avaliacao>" + rs.getFloat("avaliacao") + "</avaliacao>";
		xml += "<numero-avaliacoes>" + rs.getInt("numero-avaliacoes") + "</numero-avaliacoes>";
		xml += "<preco-hora>" + rs.getFloat("preco-hora") + "</preco-hora>";
		xml += "<id-materia>" + rs.getInt("id-materia") + "</id-materia>";
		xml += "<numero-alunos-min>" + rs.getInt("numero-alunos-min") + "</numero-alunos-min>";
		xml += "<numero-alunos-max>" + rs.getInt("numero-alunos-max") + "</numero-alunos-max>";
		xml += "<foto>" + rs.getString("foto") + "</foto>";
		xml += "<link>" + rs.getString("link-video") + "</link>";
		
		ps = c.prepareStatement("SELECT * FROM mensagem WHERE \"id-prof\" = ? AND comentario = ?");
		ps.setLong(1, idParsed);
		ps.setBoolean(2, true);
		rs = ps.executeQuery();
		xml += "<comentarios>";
		while(rs.next()) {
			xml += "<id-aluno>" + rs.getLong("id-aluno") + "</id-aluno>";
			xml += "<texto>" + rs.getString("texto") + "</texto>";
			xml += "<data>" + rs.getTimestamp("data-horario").getTime() + "</data>";
		}
		xml += "</comentarios>";

		xml += "</professor>";
		return xml;
	}
	
	public boolean inserirFoto(String idProf, String foto) throws SQLException{
		PreparedStatement ps = c.prepareStatement("UPDATE professor SET foto = ? WHERE \"id-prof\" = ?");
		Long idParsed = Long.parseLong(idProf);
		ps.setString(1, foto);
		ps.setLong(2, idParsed);
		return ps.executeUpdate() != 0;
	}
	
}