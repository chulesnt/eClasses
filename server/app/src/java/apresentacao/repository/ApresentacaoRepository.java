/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apresentacao.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author WazX
 */
public class ApresentacaoRepository {
	private Connection c;
	
	public ApresentacaoRepository(Connection c){
		this.c = c;
	}
	
	public boolean comentar(Long idAluno, String idProf, String comentario, String data) throws SQLException, ParseException {
		
		Long idProfParsed = Long.parseLong(idProf);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date parsedDate = dateFormat.parse(data);
		Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
		
		PreparedStatement ps = c.prepareStatement("INSERT INTO mensagem (texto, \"id-aluno\", \"id-prof\", \"aluno-enviou\", comentario, \"data-horario\") VALUES (?, ?, ?, ?, ?, ?)");
		ps.setString(1, comentario);
		ps.setLong(2, idAluno);
		ps.setLong(3, idProfParsed);
		ps.setBoolean(4, true);
		ps.setBoolean(5, true);
		ps.setTimestamp(6, timestamp);
		
		return ps.executeUpdate() != 0;			
	}
	
	public boolean avaliar(Long idAluno, String idProf, String nota) throws SQLException {
		Long idProfParsed = Long.parseLong(idProf);
		int notaParsed = Integer.parseInt(nota);
		ResultSet rs = null;
		
		try {
			PreparedStatement ps = c.prepareStatement("SELECT * from alunoAvaliaProf WHERE \"id-prof\" = ?");
			ps.setLong(1, idProfParsed);
			rs = ps.executeQuery();
		} catch (SQLException ex) {
			
		}
		while (rs.next()) {
			if (rs.getLong("id-aluno") == idAluno)
				return false;
			}
		PreparedStatement ps = c.prepareStatement("INSERT INTO alunoAvaliaProf (\"id-aluno\", \"id-prof\", avaliacao) VALUES (?,?,?)");
		ps.setLong(1, idAluno);
		ps.setLong(2, idProfParsed);
		ps.setInt(3, notaParsed);
		return ps.executeUpdate() != 0;
	}
	
	public boolean atualizarAvaliacao(String idProf, String nota) throws SQLException {
		Long idProfParsed = Long.parseLong(idProf);
		int notaParsed = Integer.parseInt(nota);
		PreparedStatement ps = c.prepareStatement("SELECT * from professor WHERE \"id-prof\" = ?");
		ps.setLong(1, idProfParsed);
		ResultSet rs = ps.executeQuery();
		rs.next();
		int num = rs.getInt("numero-avaliacoes");
		float notaAnterior = rs.getFloat("avaliacao");
		float notaFinal = (num*notaAnterior+notaParsed)/(num+1);
		ps = c.prepareStatement("UPDATE professor SET avaliacao = ?, \"numero-avaliacoes\" = ? WHERE \"id-prof\" = ?");
		ps.setFloat(1, notaFinal);
		ps.setInt(2, num+1);
		ps.setLong(3, idProfParsed);
		return ps.executeUpdate() != 0;
	}
}
