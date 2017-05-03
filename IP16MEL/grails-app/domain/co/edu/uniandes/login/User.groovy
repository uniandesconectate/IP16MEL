package co.edu.uniandes.login

class User {

	transient springSecurityService

	String username
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	
	/**
	 * Adicionados al modelo
	 */
	String nombre
	int medallas=0;
	int gemas=0;
	int puntos=0;
	int[] estrellasSemanas=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
	double[] aporteSemanas=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
	static belongsTo = [faccion: Faccion]


	static transients = ['springSecurityService']

	static constraints = {
		username blank: false, unique: true
		password blank: false
	}

	static mapping = {
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role }
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}
}
