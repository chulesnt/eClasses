package alunos.repository;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.Hasher;
import utils.autenticador.Autenticador;
import utils.autenticador.Cargos;


public class AlunosRepository {
	private Connection c;
	
	public AlunosRepository(Connection c){
		this.c = c;
	}
	
	public boolean insert(String email, String senha, String nome, String idMunicipio, String idUf) throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException, ParseException{
		String hashedSenha = Hasher.hash(senha);
		
		Boolean assinante = false;
		
		PreparedStatement ps = c.prepareStatement("INSERT INTO aluno (\"email-aluno\", senha, nome, \"id-municipio\", \"id-uf\", assinante) VALUES (?, ?, ?, ?, ?, ?)");
		ps.setString(1, email);
		ps.setString(2, hashedSenha);
		ps.setString(3, nome);
		ps.setInt(4, Integer.parseInt(idMunicipio));
		ps.setInt(5, Integer.parseInt(idUf));
		ps.setBoolean(6, assinante);
		
		return ps.executeUpdate() != 0;
	}
	
	public boolean logar(HttpServletRequest req, HttpServletResponse res, String id, String senha) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException{
		Long idLong = Long.parseLong(id);
		Autenticador aut = new Autenticador(req, res);
		String str = "SELECT * FROM aluno WHERE \"id-aluno\" = ?";
		PreparedStatement st = c.prepareStatement(str);
		st.setLong(1, idLong);
		ResultSet rs = st.executeQuery();
		rs.next();
		if (Hasher.validar(senha, rs.getString("senha"))) {
			aut.logar(idLong, Cargos.ALUNO, false);
			return true;
		}
		return false;

	}
}