package co.edu.uniandes.csw.mueblesdelosalpes.servicios;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Usuario;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.OperacionInvalidaException;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioRegistroMockLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/Registro")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RegistroService {

    @EJB
    private IServicioRegistroMockLocal registroEjb;

    /** Construye una excepción HTTP con un mensaje de texto. */
    private WebApplicationException error(Response.Status estado, String mensaje) {
        return new WebApplicationException(
                Response.status(estado).entity(mensaje).type(MediaType.TEXT_PLAIN).build());
    }

    /** Busca un usuario por login; lanza 404 si no existe. */
    private Usuario buscar(String login) {
        for (Usuario u : registroEjb.darClientes()) {
            if (u.getLogin().equals(login)) {
                return u;
            }
        }
        throw error(Response.Status.NOT_FOUND, "No existe un cliente con login " + login);
    }

    /** GET /Registro/clientes : todos los usuarios registrados. */
    @GET
    @Path("clientes/")
    public List<Usuario> getClientes() {
        return registroEjb.darClientes();
    }

    /** GET /Registro/clientes/{login} : un usuario por su login. */
    @GET
    @Path("clientes/{login}")
    public Usuario getCliente(@PathParam("login") String login) {
        return buscar(login);
    }

    /** POST /Registro/clientes : registra un usuario nuevo. */
    @POST
    @Path("clientes/")
    public Usuario registrar(Usuario usuario) {
        if (usuario == null || usuario.getLogin() == null) {
            throw error(Response.Status.BAD_REQUEST, "Debe enviar un usuario con login");
        }
        try {
            registroEjb.registrar(usuario);
        } catch (OperacionInvalidaException e) {
            throw error(Response.Status.BAD_REQUEST, e.getMessage());
        }
        return usuario;
    }

    /** DELETE /Registro/clientes/{login} : elimina un usuario. */
    @DELETE
    @Path("clientes/{login}")
    public void eliminarCliente(@PathParam("login") String login) {
        buscar(login);
        try {
            registroEjb.eliminarCliente(login);
        } catch (OperacionInvalidaException e) {
            throw error(Response.Status.BAD_REQUEST, e.getMessage());
        }
    }
}