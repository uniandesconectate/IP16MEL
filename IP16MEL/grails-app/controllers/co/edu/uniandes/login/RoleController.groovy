package co.edu.uniandes.login
import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.SpringSecurityService

@Secured(['ROLE_ADMIN'])
class RoleController {

    static scaffold=true
}
