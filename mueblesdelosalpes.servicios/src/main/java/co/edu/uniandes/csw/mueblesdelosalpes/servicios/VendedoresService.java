package co.edu.uniandes.csw.mueblesdelosalpes.servicios;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Vendedor;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.OperacionInvalidaException;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioVendedoresMockLocal;
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

@Path("/Vendedores")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VendedoresService {

    @EJB
    private IServicioVendedoresMockLocal vendedoresEjb;

    /** Construye una excepción HTTP con un mensaje de texto. */
    private WebApplicationException error(Response.Status estado, String mensaje) {
        return new WebApplicationException(
                Response.status(estado).entity(mensaje).type(MediaType.TEXT_PLAIN).build());
    }

    /** Busca un vendedor por identificación; lanza 404 si no existe. */
    private Vendedor buscar(long id) {
        for (Vendedor v : vendedoresEjb.getVendedores()) {
            if (v.getIdentificacion() == id) {
                return v;
            }
        }
        throw error(Response.Status.NOT_FOUND, "No existe un vendedor con id " + id);
    }

    /** GET /Vendedores/vendedores : todos los vendedores. */
    @GET
    @Path("vendedores/")
    public List<Vendedor> getVendedores() {
        return vendedoresEjb.getVendedores();
    }

    /** GET /Vendedores/vendedores/{id} : un vendedor por su identificación. */
    @GET
    @Path("vendedores/{id}")
    public Vendedor getVendedor(@PathParam("id") long id) {
        return buscar(id);
    }

    /** POST /Vendedores/vendedores : agrega un vendedor (el id lo asigna el sistema). */
    @POST
    @Path("vendedores/")
    public Vendedor agregarVendedor(Vendedor vendedor) {
        if (vendedor == null) {
            throw error(Response.Status.BAD_REQUEST, "Debe enviar un vendedor");
        }
        try {
            vendedoresEjb.agregarVendedor(vendedor);
        } catch (OperacionInvalidaException e) {
            throw error(Response.Status.BAD_REQUEST, e.getMessage());
        }
        return vendedor;
    }

    /** DELETE /Vendedores/vendedores/{id} : elimina un vendedor. */
    @DELETE
    @Path("vendedores/{id}")
    public void eliminarVendedor(@PathParam("id") long id) {
        buscar(id);
        try {
            vendedoresEjb.eliminarVendedor(id);
        } catch (OperacionInvalidaException e) {
            throw error(Response.Status.BAD_REQUEST, e.getMessage());
        }
    }
}