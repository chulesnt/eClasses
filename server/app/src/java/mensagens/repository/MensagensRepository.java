package mensagens.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import mensagens.model.MensagensModel;

public class MensagensRepository {
	private Connection c;
	
	public MensagensRepository(Connection c){
		this.c = c;
	}
	
	public boolean criarMensagem(String texto, String idAluno, String idProf, boolean alunoEnviou, String data) throws ParseException, SQLException{
		Long idAlunoParsed = Long.parseLong(idAluno);
		Long idProfParsed = Long.parseLong(idProf);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date parsedDate = dateFormat.parse(data);
		Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
		
		PreparedStatement ps = c.prepareStatement("INSERT INTO mensagem (texto, \"id-aluno\", \"id-prof\", \"aluno-enviou\", comentario, \"data-horario\") VALUES (?, ?, ?, ?, ?, ?)");
		ps.setString(1, texto);
		ps.setLong(2, idAlunoParsed);
		ps.setLong(3, idProfParsed);
		ps.setBoolean(4, alunoEnviou);
		ps.setBoolean(5, false);
		ps.setTimestamp(6, timestamp);
		
		return ps.executeUpdate() != 0;
	}
	
	public boolean excluirMensagem(String idMensagem) throws SQLException{
		Long idMensagemParsed = Long.parseLong(idMensagem);
		
		PreparedStatement ps = c.prepareStatement("DELETE FROM mensagem WHERE \"id-mensagem\" = ?");
		ps.setLong(1, idMensagemParsed);
		
		return ps.executeUpdate() != 0;
	}
	
	public List exibirMensagens(String idAluno, String idProf) throws SQLException{
		List mensagens = new LinkedList<>();
		
		Long idAlunoParsed = Long.parseLong(idAluno);
		Long idProfParsed = Long.parseLong(idProf);
		
		PreparedStatement ps = c.prepareStatement("SELECT * FROM mensagem WHERE \"id-aluno\" = ? AND \"id-prof\" = ? AND comentario = false ORDER BY \"data-horario\"");
		ps.setLong(1, idAlunoParsed);
		ps.setLong(2, idProfParsed);
		
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			MensagensModel msg = new MensagensModel(
					rs.getLong("id-mensagem"),
					rs.getLong("id-aluno"),
					rs.getLong("id-prof"),
					rs.getString("texto"),
					rs.getBoolean("aluno-enviou"),
					rs.getBoolean("comentario"),
					rs.getTimestamp("data-horario")
			);
			mensagens.add(msg);
		}
		rs.close();
		ps.close();
		return mensagens;
	}
}
