package co.edu.uniandes.login

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN','ROLE_SUPERADMIN'])
class AdministradorController {

    static scaffold = true
}
