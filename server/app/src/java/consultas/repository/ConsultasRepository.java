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
}
