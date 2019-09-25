package apiActivosFijo.example.resource;

import apiActivosFijo.example.model.EstadoActual;
import apiActivosFijo.example.service.EstadoActualService;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static apiActivosFijo.example.Diccionario.Diccionario.CODIGO;
import static apiActivosFijo.example.Diccionario.Diccionario.DESCRIPCION;
import static apiActivosFijo.example.Diccionario.Diccionario.KEY_ESTADO_ACTUAL;

/**
 * The type Estado actual resource.
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/estadoActual")
public class EstadoActualResource {

    /**
     * The Uri info.
     */
    @Context
    UriInfo uriInfo;

    @EJB
    private EstadoActualService estadoActualService;

    private Responses responses = new Responses();


    /**
     * Gets all estado actual.
     *
     * @param firstResult the first result
     * @param maxResult   the max result
     * @return the all estado actual
     */
    @GET
    public Response getAllEstadoActual(@QueryParam("firstResult") Integer firstResult, @QueryParam("maxResult") Integer maxResult) {
        if (firstResult != null && firstResult < 1) {
            return responses.getResponse(Response.Status.NOT_ACCEPTABLE, "firstResult", new String[]{"firstResult"}, new String[]{firstResult.toString()});
        }
        if (maxResult != null && maxResult < 1) {
            return responses.getResponse(Response.Status.NOT_ACCEPTABLE, "maxResult", new String[]{"maxResult"}, new String[]{maxResult.toString()});
        }
        List<EstadoActual> estadoActuals = estadoActualService.getAllEstadoActual(firstResult, maxResult);
        return Response.ok(estadoActuals).build();
    }

    /**
     * Gets estado actual by codigo.
     *
     * @param codigo the codigo
     * @return the estado actual by codigo
     */
    @GET
    @Path("/byCodigo")
    public Response getEstadoActualByCodigo(@QueryParam(CODIGO) String codigo) {
        if (codigo.equals("")) {
            return responses.getResponse(Response.Status.BAD_REQUEST, KEY_ESTADO_ACTUAL, new String[]{CODIGO, DESCRIPCION}, new String[]{"", "El codigo ingresado no puede estar vacio"});
        }
        EstadoActual estadoActual = estadoActualService.getEstadoActualByCodigo(codigo);

        if (estadoActual == null) {
            return responses.getResponse(Response.Status.NOT_FOUND, KEY_ESTADO_ACTUAL, new String[]{CODIGO, DESCRIPCION}, new String[]{codigo, "No existe ningun registro con el codigo ingresado"});
        }

        return Response.ok(estadoActual).build();
    }

    /**
     * Create estado actual response.
     *
     * @param estadoActual the estado actual
     * @return the response
     */
    @POST
    public Response createEstadoActual(EstadoActual estadoActual) {
        if (estadoActual.getCodigo().equals("")) {
            return responses.getResponse(Response.Status.BAD_REQUEST, KEY_ESTADO_ACTUAL, new String[]{CODIGO, DESCRIPCION}, new String[]{"", "El codigo ingresado no puede estar vacio"});
        }

        EstadoActual buscarEstadoActual = estadoActualService.getEstadoActualByCodigo(estadoActual.getCodigo());

        if (buscarEstadoActual != null) {
            return responses.getResponse(Response.Status.FORBIDDEN, KEY_ESTADO_ACTUAL + "_Existe", new String[]{CODIGO, DESCRIPCION}, new String[]{estadoActual.getCodigo(), "El codigo del objeto que desea crear ya se encuentra registrado"});
        }
        EstadoActual createEstadoActual = estadoActualService.createEstadoActual(estadoActual);
        return Response.status(Response.Status.CREATED).entity(createEstadoActual).build();
    }


    /**
     * Update estadoActual response.
     *
     * @param estadoActual the estado actual
     * @return the response
     */
    @PUT
    public Response updateEstadoActual(EstadoActual estadoActual) {
        if (estadoActual.getCodigo().equals("")) {
            return responses.getResponse(Response.Status.BAD_REQUEST, KEY_ESTADO_ACTUAL, new String[]{CODIGO, DESCRIPCION}, new String[]{"", "El codigo ingresado no puede estar vacio"});
        }

        if (estadoActualService.getEstadoActualByCodigo(estadoActual.getCodigo()) == null) {
            return responses.getResponse(Response.Status.NOT_FOUND, KEY_ESTADO_ACTUAL, new String[]{CODIGO, DESCRIPCION}, new String[]{estadoActual.getCodigo(), "No existe ningun registro con el codigo ingresado"});
        }

        EstadoActual estadoActualToUpdate = estadoActualService.updateEstadoActual(estadoActual.getCodigo(), estadoActual);
        return Response.ok(estadoActualToUpdate).build();
    }

    /**
     * Delete estado actual response.
     *
     * @param codigo the codigo
     * @return the response
     */
    @DELETE
    public Response deleteEstadoActual(@QueryParam(CODIGO) String codigo) {
        if (codigo.equals("")) {
            return responses.getResponse(Response.Status.BAD_REQUEST, KEY_ESTADO_ACTUAL, new String[]{CODIGO, DESCRIPCION}, new String[]{"", "El codigo ingresado no puede estar vacio"});
        }
        if (estadoActualService.getEstadoActualByCodigo(codigo) == null) {
            return responses.getResponse(Response.Status.NOT_FOUND, KEY_ESTADO_ACTUAL, new String[]{CODIGO, DESCRIPCION}, new String[]{codigo, "No existe ningun registro con el codigo ingresado"});
        }
        EstadoActual estadoActual = estadoActualService.deleteEstadoActual(codigo);
        return Response.ok(estadoActual).build();
    }
}
