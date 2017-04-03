package co.edu.uniandes.mel.helpers;

import co.edu.uniandes.mel.dto.UsuarioDTO;
import co.edu.uniandes.mel.dto.FaccionDTO;
//import grails.converters.JSON;
//import grails.plugins.rest.client.RestBuilder;
//import grails.plugins.rest.client.RestResponse;

public class EngineHelper {
	
	
	private String token = "210fd18fe01d086fe1f6ed60f789137b"; //Token de API en PlayNGage
	private String testGroup = "First";
	private String urlEngineApi = "http://playngage.io/api/players";

	
	
	/**
	 * Retorna los datos del usuario en el game engine. Sino existe returna null.
	 * @param String usuario nombre del usuario 
	 * @return UsuarioDTO  Datos del usuario o nulo
	 */
	public UsuarioDTO getDatosUsuario(String usuario){
		
		// Llamado al servicio rest
//		RestBuilder rest = new RestBuilder();
//		RestResponse resp = rest.post(urlEngineApi + "/players" +
//				"?id_in_app=" + usuario) {
//			header 'Authorization', 'Token token=' + token
//			header 'Accept', '*/*'
//		};
//
//		
//		
		UsuarioDTO us= new UsuarioDTO();
		return us;
	}
	
	/**
	 * Retorna los datos de la facción en el game engine. Sino existe returna null.
	 * @param String nombreFaccion
	 * @return TeamDTO the team data or null
	 */
	public FaccionDTO getDatosFaccion (String nombfreFaccion) {
		FaccionDTO faccion = new FaccionDTO();
		return faccion;
	}

}
