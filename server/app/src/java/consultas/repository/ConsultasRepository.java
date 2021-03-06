/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consultas.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author WazX
 */
public class ConsultasRepository {
	private Connection c;
	
	public ConsultasRepository(Connection c){
		this.c = c;
	}
	
	
	public String getUf(int id) throws SQLException{
		PreparedStatement ps = c.prepareStatement("SELECT * FROM uf WHERE \"id-uf\" = ?");
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		rs.next();
		return rs.getString("uf");
	}
	
	public String consultarUf(String id) throws SQLException {
		String xml = "<uf>";
		PreparedStatement ps = c.prepareStatement("SELECT * FROM uf WHERE \"id-uf\" = ?");
		Long idParsed = Long.parseLong(id);
		ps.setLong(1, idParsed);
		ResultSet rs = ps.executeQuery();
		rs.next();
		xml += "<id>" + idParsed + "</id>";
		xml += "<nome>" + rs.getString("nome") + "</nome>";
		xml += "<sigla>" + rs.getString("uf") + "</sigla>";

		xml += "</uf>";
		return xml;
	}
	
	public String getMunicipio(int id) throws SQLException{
		PreparedStatement ps = c.prepareStatement("SELECT * FROM municipio WHERE \"id-municipio\" = ?");
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		rs.next();
		return rs.getString("nome");
	}
	
	public String consultarMunicipio(String id) throws SQLException {
		String xml = "<municipio>";
		PreparedStatement ps = c.prepareStatement("SELECT * FROM municipio WHERE \"id-municipio\" = ?");
		Long idParsed = Long.parseLong(id);
		ps.setLong(1, idParsed);
		ResultSet rs = ps.executeQuery();
		rs.next();
		xml += "<id>" + idParsed + "</id>";
		xml += "<nome>" + rs.getString("nome") + "</nome>";
		xml += "<uf>" + rs.getString("uf") + "</uf>";

		xml += "</municipio>";
		return xml;
	}
	
	public String getMateria(int id) throws SQLException{
		PreparedStatement ps = c.prepareStatement("SELECT * FROM materias WHERE \"id-materia\" = ?");
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		rs.next();
		return rs.getString("nome");
	}
	
	public String consultarMateria(String id) throws SQLException {
		String xml = "<materia>";
		PreparedStatement ps = c.prepareStatement("SELECT * FROM materias WHERE \"id-materia\" = ?");
		Long idParsed = Long.parseLong(id);
		ps.setLong(1, idParsed);
		ResultSet rs = ps.executeQuery();
		rs.next();
		xml += "<id>" + idParsed + "</id>";
		xml += "<nome>" + rs.getString("nome") + "</nome>";

		xml += "</materia>";
		return xml;
	}
	
	public String consultarPreferenciaLocalizacao(String id) throws SQLException {
		String xml = "<preferencia-localizacao>";
		PreparedStatement ps = c.prepareStatement("SELECT * FROM preferencialocalizacao WHERE \"id-preferencia-local\" = ?");
		Long idParsed = Long.parseLong(id);
		ps.setLong(1, idParsed);
		ResultSet rs = ps.executeQuery();
		rs.next();
		xml += "<id>" + idParsed + "</id>";
		xml += "<descricao>" + rs.getString("descricao") + "</descricao>";

		xml += "</preferencia-localizacao>";
		return xml;
	}
	
	public String consultarAlunoAvaliaProf(String idAluno, String idProf) throws SQLException {
		String xml = "<aluno-avalia-prof>";
		PreparedStatement ps = c.prepareStatement("SELECT * FROM alunoavaliaprof WHERE \"id-aluno\" = ? AND \"id-prof\" = ?");
		Long idParsedAluno = Long.parseLong(idAluno);
		Long idParsedProf = Long.parseLong(idProf);
		ps.setLong(1, idParsedAluno);
		ps.setLong(2, idParsedProf);
		ResultSet rs = ps.executeQuery();
		rs.next();
		xml += "<id-aluno>" + idParsedAluno + "</id-aluno>";
		xml += "<id-prof>" + idParsedProf + "</id-prof>";
		xml += "<avaliacao>" + rs.getInt("avaliacao") + "</avaliacao>";

		xml += "</aluno-avalia-prof>";
		return xml;
	}
	
	public String contarUsuarios() throws SQLException {
		String xml = "<usuarios>";
		int contAlunos = 0;
		int contProfs = 0;
		
		PreparedStatement ps1 = c.prepareStatement("SELECT * FROM aluno");
		ResultSet rs1 = ps1.executeQuery();
		
		while(rs1.next()) {
			contAlunos++;
		}
		
		PreparedStatement ps2 = c.prepareStatement("SELECT * FROM professor");
		ResultSet rs2 = ps2.executeQuery();
		
		while(rs2.next()) {
			contProfs++;
		}
		
		xml += "<alunos>" + contAlunos + "</alunos>";
		xml += "<professores>" + contProfs + "</professores>";
		
		xml+= "</usuarios>";
		
		return xml;
	}
}
