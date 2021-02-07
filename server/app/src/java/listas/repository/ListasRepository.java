/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listas.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author WazX
 */
public class ListasRepository {
	private Connection c;
	
	public ListasRepository(Connection c){
		this.c = c;
	}
	
	public String listarUf() throws SQLException {
		String xml = "<ufs>";
		PreparedStatement ps = c.prepareStatement("SELECT * FROM uf");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			xml += "<uf>";
			xml += "<id>" + rs.getLong("id-uf") + "</id>";
			xml += "<nome>" + rs.getString("nome") + "</nome>";
			xml += "<sigla>" + rs.getString("uf") + "</sigla>";
			xml += "</uf>";
		}
		xml += "</ufs>";
		return xml;
	}
	
	public String listarMunicipio(String uf) throws SQLException {
		String xml = "<municipios>";
		xml += "<"+uf+">";
		PreparedStatement ps = c.prepareStatement("SELECT * FROM municipio WHERE uf = ?");
		ps.setString(1, uf);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			xml += "<municipio>";
			xml += "<id>" + rs.getLong("id-municipio") + "</id>";
			xml += "<nome>" + rs.getString("nome") + "</nome>";
			xml += "</municipio>";
		}
		xml += "</"+uf+">";
		xml += "</municipios>";
		return xml;
	}
	
	public String listarMateria() throws SQLException {
		String xml = "<materias>";
		PreparedStatement ps = c.prepareStatement("SELECT * FROM materias");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			xml += "<materia>";
			xml += "<id>" + rs.getLong("id-materia") + "</id>";
			xml += "<nome>" + rs.getString("nome") + "</nome>";
			xml += "</materia>";
		}
		xml += "</materias>";
		return xml;
	}
	
	public String listarPreferenciaLocalizacao() throws SQLException {
		String xml = "<preferencia-localizacao>";
		PreparedStatement ps = c.prepareStatement("SELECT * FROM preferencialocalizacao");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			xml += "<pref>";
			xml += "<id>" + rs.getLong("id-preferencia-local") + "</id>";
			xml += "<descricao>" + rs.getString("descricao") + "</descricao>";
			xml += "</pref>";
		}
		xml += "</preferencia-localizacao>";
		return xml;
	}
}
