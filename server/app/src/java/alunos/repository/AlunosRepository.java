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
	
	public boolean insert(String email, String senha, String nome, String idMunicipio, String idUf, String preferenciaPreco, String idPreferenciaLocal, String preferenciaNumeroAlunos, String assinante, String data) throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException, ParseException{
		String hashedSenha = Hasher.hash(senha);
		
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
		Date dataFim = null;
		java.sql.Date dataFimAssinatura = null;
		dataFim = f.parse(data);
		dataFimAssinatura = new java.sql.Date(dataFim.getTime());
		
		PreparedStatement ps = c.prepareStatement("INSERT INTO aluno (\"email-aluno\", senha, nome, \"id-municipio\", \"id-uf\", \"preferencia-preco\", \"id-preferencia-local\", \"preferencia-numero-alunos\", assinante) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		ps.setString(1, email);
		ps.setString(2, senha);
		ps.setString(3, nome);
		ps.setInt(4, Integer.parseInt(idMunicipio));
		ps.setInt(5, Integer.parseInt(idUf));
		ps.setFloat(6, Float.parseFloat(preferenciaPreco));
		ps.setInt(7, Integer.parseInt(idPreferenciaLocal));
		ps.setInt(8, Integer.parseInt(preferenciaNumeroAlunos));
		ps.setBoolean(9, Boolean.parseBoolean(assinante));
		ps.setDate(9, dataFimAssinatura);
		
		return ps.executeUpdate() != 0;
	}
}
