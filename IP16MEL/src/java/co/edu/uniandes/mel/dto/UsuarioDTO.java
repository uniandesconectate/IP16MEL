package co.edu.uniandes.mel.dto;
import co.edu.uniandes.mel.dto.Constantes;

public class UsuarioDTO {
	private String nombreUsuario;
	private int medallas;
	private int gemas;
	private int puntos;
	private int[] estrellasSemanas;
	private double[] aporteSemanas;
	
	public UsuarioDTO() {
		super();
		this.nombreUsuario = "";
		this.medallas = 0;
		this.gemas = 0;
		this.puntos = 0;
		this.estrellasSemanas = new int[Constantes.NUM_SEMANAS];
		this.aporteSemanas = new double[Constantes.NUM_SEMANAS];
		
	}
	
	

	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public int getMedallas() {
		return medallas;
	}
	public void setMedallas(int medallas) {
		this.medallas = medallas;
	}
	public int getGemas() {
		return gemas;
	}
	public void setGemas(int gemas) {
		this.gemas = gemas;
	}
	public int getPuntos() {
		return puntos;
	}
	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}
	public int[] getEstrellasSemanas() {
		return estrellasSemanas;
	}
	public void setEstrellasSemanas(int[] estrellasSemanas) {
		this.estrellasSemanas = estrellasSemanas;
	}
	public double[] getAporteSemanas() {
		return aporteSemanas;
	}
	public void setAporteSemanas(double[] aporteSemanas) {
		this.aporteSemanas = aporteSemanas;
	}
	
	
	

}
