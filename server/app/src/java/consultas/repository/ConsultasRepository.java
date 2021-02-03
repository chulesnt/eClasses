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
}
