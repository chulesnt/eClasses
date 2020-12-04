package utils.autenticador;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class Autenticador {
	
	private HttpServletRequest req;
	private HttpServletResponse res;
	
	private final int DURATION = 60 * 60;
	private final int EXTENDED_DURATION = 7 * 24 * 60 * 60;
	
	public Autenticador(HttpServletRequest req, HttpServletResponse res){
		this.req = req;
		this.res = res;
	}
	
	public Cargos getCargoLogado(){
		HttpSession session = this.req.getSession();
		Cargos cargo = (Cargos) session.getAttribute("cargo");
	
		return cargo;
	}
	
	public Object getIdLogado(){
		HttpSession session = this.req.getSession();
		return session.getAttribute("id");
	}
	
	public void logar(Object id, Cargos cargo, boolean manterLogin){
		HttpSession session = this.req.getSession();
		session.setAttribute("id", id);
		session.setAttribute("cargo", cargo);
		if(manterLogin == true){
			session.setMaxInactiveInterval(EXTENDED_DURATION);
		} else session.setMaxInactiveInterval(DURATION);
	}
	
}
