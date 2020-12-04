package alunos.repository;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import utils.Hasher;


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
}
