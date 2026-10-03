/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.uniandes.csw.mueblesdelosalpes.servicios;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Mueble;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioCatalogoMockLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/Catalogo")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CatalogoService {

    @EJB
    private IServicioCatalogoMockLocal catalogoEjb;

    /** Busca un mueble por referencia; lanza 404 si no existe. */
    private Mueble buscar(long id) {
        for (Mueble m : catalogoEjb.darMuebles()) {
            if (m.getReferencia() == id) {
                return m;
            }
        }
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    /** GET /Catalogo/muebles : todos los muebles. */
    @GET
    @Path("muebles/")
    public List<Mueble> getTodosLosMuebles() {
        return catalogoEjb.darMuebles();
    }

    /** GET /Catalogo/muebles/{id} : un mueble por su referencia. */
    @GET
    @Path("muebles/{id}")
    public Mueble getMueble(@PathParam("id") long id) {
        return buscar(id);
    }

    /** POST /Catalogo/muebles : agrega un mueble al catálogo. */
    @POST
    @Path("muebles/")
    public Mueble agregarMueble(Mueble mueble) {
        catalogoEjb.agregarMueble(mueble);
        return mueble;
    }

    /** PUT /Catalogo/muebles/{id}/ejemplar : descuenta un ejemplar del mueble. */
    @PUT
    @Path("muebles/{id}/ejemplar")
    public Mueble removerEjemplar(@PathParam("id") long id) {
        buscar(id);
        catalogoEjb.removerEjemplarMueble(id);
        return buscar(id);
    }

    /** DELETE /Catalogo/muebles/{id} : elimina el mueble del catálogo. */
    @DELETE
    @Path("muebles/{id}")
    public void eliminarMueble(@PathParam("id") long id) {
        buscar(id);
        catalogoEjb.eliminarMueble(id);
    }
}
