package co.edu.uniandes.login
import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.SpringSecurityService

@Secured(['ROLE_ADMIN','ROLE_SUPERADMIN'])
class UserRoleController {

    static scaffold=true
}
