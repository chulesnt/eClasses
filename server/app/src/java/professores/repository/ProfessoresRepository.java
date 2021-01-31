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
		
		Boolean premium = false;
		
		String desc = "";
		String titulo = "";
		float avaliacao = 0;
		int numAval = 0;
		float preco = 0;
		
		
		PreparedStatement ps = c.prepareStatement("INSERT INTO professor (\"email-prof\", senha, nome, \"id-municipio\", \"id-uf\", \"id-materia\", premium, \"descricao_apresentacao\", \"titulo_apresentacao\", avaliacao, \"numero-avaliacoes\", \"preco-hora\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		ps.setString(1, email);
		ps.setString(2, hashedSenha);
		ps.setString(3, nome);
		ps.setInt(4, Integer.parseInt(idMunicipio));
		ps.setInt(5, Integer.parseInt(idUf));
		ps.setInt(6, Integer.parseInt(idMateria));
		ps.setBoolean(7, premium);
		ps.setString(8, desc);
		ps.setString(9, titulo);
		ps.setFloat(10, avaliacao);
		ps.setInt(11, numAval);
		ps.setFloat(12, preco);
		
		
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
	
	public boolean editar(String idProf, String nome, String idMunicipio, String idUf, String desc, String titulo, String premium, String preco, String	idMateria, String numAMin, String numAMax, String dataPremium) throws SQLException, ParseException {
		int adcs = 0;
		int cont = 1;
		boolean[] pars = new boolean[11];
		
		
		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		Date dataUtil = null;
		java.sql.Date dataSql = null;
		
		for (int i = 0; i < 11; i++) {
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
		if (!"".equals(titulo)) {
			if (adcs > 0) {
				query += ",";
			}
			query += " \"titulo_apresentacao\"= ?";
			adcs++;
			pars[4] = true;
		}
		if (!"".equals(premium)) {
			if (adcs > 0) {
				query += ",";
			}
			query += " \"premium\"= ?";
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
		if (!"".equals(dataPremium)) {
			dataUtil = formato.parse(dataPremium);
			dataSql = new java.sql.Date(dataUtil.getTime());
			if (adcs > 0) {
				query += ",";
			}
			query += " \"data-fim-premium\"= ?";
			pars[10] = true;
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
		if (pars[4]) {
			ps.setString(cont, titulo);
			cont++;
		}
		if (pars[5]) {
			ps.setBoolean(cont, Boolean.parseBoolean(premium));
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
		if (pars[10]) {
			ps.setDate(cont, dataSql);
			cont++;
		}
		
		
		

		ps.setLong(cont, Long.parseLong(idProf));

		int sucesso = ps.executeUpdate();

		return sucesso != 0;
	}
	
	public boolean alterarSenha(Long id, String senha) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {		
		String hashSenha = null;
		hashSenha = Hasher.hash(senha);
		String query = "UPDATE professor SET senha= ? WHERE \"id-prof\" = ?";

		PreparedStatement ps = c.prepareStatement(query);
		ps.setString(1, hashSenha);
		ps.setLong(2, id);
		int sucesso = ps.executeUpdate();
		return sucesso != 0;
	}
	
	
}