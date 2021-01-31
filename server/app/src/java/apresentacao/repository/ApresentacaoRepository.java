/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apresentacao.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
}
