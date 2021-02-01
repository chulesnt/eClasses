package mensagens.model;

import java.sql.Timestamp;

public class MensagensModel {
	private Long idMensagem, idAluno, idProf;
	private String texto;
	private boolean alunoEnviou, comentario;
	private Timestamp dataHorario;

	public MensagensModel(Long idMensagem, Long idAluno, Long idProf, String texto, boolean alunoEnviou, boolean comentario, Timestamp dataHorario) {
		this.idMensagem = idMensagem;
		this.idAluno = idAluno;
		this.idProf = idProf;
		this.texto = texto;
		this.alunoEnviou = alunoEnviou;
		this.comentario = comentario;
		this.dataHorario = dataHorario;
	}
	
	public Long getIdMensagem() {
		return idMensagem;
	}

	public void setIdMensagem(Long idMensagem) {
		this.idMensagem = idMensagem;
	}

	public Long getIdAluno() {
		return idAluno;
	}

	public void setIdAluno(Long idAluno) {
		this.idAluno = idAluno;
	}

	public Long getIdProf() {
		return idProf;
	}

	public void setIdProf(Long idProf) {
		this.idProf = idProf;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public boolean isAlunoEnviou() {
		return alunoEnviou;
	}

	public void setAlunoEnviou(boolean alunoEnviou) {
		this.alunoEnviou = alunoEnviou;
	}

	public boolean isComentario() {
		return comentario;
	}

	public void setComentario(boolean comentario) {
		this.comentario = comentario;
	}

	public Timestamp getDataHorario() {
		return dataHorario;
	}

	public void setDataHorario(Timestamp dataHorario) {
		this.dataHorario = dataHorario;
	}
}
