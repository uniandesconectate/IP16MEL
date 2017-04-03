package co.edu.uniandes.mel.dto;

import java.util.ArrayList;

public class FaccionDTO {
	
	private String nombreFaccion;
	private int puntos;
	private int monedas;
	private ArrayList <UsuarioDTO> miembros;

	

	public FaccionDTO() {
		super();
		this.nombreFaccion = "";
		this.puntos = 0;
		this.monedas = 0;
		this.miembros = new ArrayList <UsuarioDTO> ();
	}
	
	public String getNombreFaccion() {
		return nombreFaccion;
	}
	public void setNombreFaccion(String nombreFaccion) {
		this.nombreFaccion = nombreFaccion;
	}
	public int getPuntos() {
		return puntos;
	}
	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}
	public int getMonedas() {
		return monedas;
	}
	public void setMonedas(int monedas) {
		this.monedas = monedas;
	}
	public ArrayList<UsuarioDTO> getMiembros() {
		return miembros;
	}
	public void setMiembros(ArrayList<UsuarioDTO> miembros) {
		this.miembros = miembros;
	}

}
