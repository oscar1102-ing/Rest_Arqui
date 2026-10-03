package co.edu.uniandes.csw.mueblesdelosalpes.servicios;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Usuario;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.AutenticacionException;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioSeguridadMockLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/Seguridad")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SeguridadService {

    @EJB
    private IServicioSeguridadMockLocal seguridadEjb;

    /** Construye una excepción HTTP con un mensaje de texto. */
    private WebApplicationException error(Response.Status estado, String mensaje) {
        return new WebApplicationException(
                Response.status(estado).entity(mensaje).type(MediaType.TEXT_PLAIN).build());
    }

    /**
     * POST /Seguridad/ingresar : recibe login y contraseña en JSON y devuelve el
     * usuario autenticado, o 401 si las credenciales no son válidas.
     */
    @POST
    @Path("ingresar/")
    public Usuario ingresar(Usuario credenciales) {
        if (credenciales == null || credenciales.getLogin() == null
                || credenciales.getContraseña() == null) {
            throw error(Response.Status.BAD_REQUEST, "Debe enviar login y contraseña");
        }
        try {
            return seguridadEjb.ingresar(credenciales.getLogin(), credenciales.getContraseña());
        } catch (AutenticacionException e) {
            throw error(Response.Status.UNAUTHORIZED, e.getMessage());
        }
    }
}