package br.com.erudio.rest.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import br.com.erudio.model.Quadra;
import br.com.erudio.service.LocalService;
import br.com.erudio.service.QuadraService;

@Stateless
@Path("/quadra")
@Api(value = "/quadra", description = "Permite realizar operações CRUD de Quadras!")
public class QuadraResource {

	@Inject
	private QuadraService repository;
	
	@Inject
	private LocalService localRepository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Busca todas as quadras", notes = "Busca todas as quadras!")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public List<Quadra> listAllQuadras() {
		ArrayList<Quadra> quadras = repository.findAllQuadras();
		if (quadras == null || quadras.isEmpty()) throw new WebApplicationException(Response.Status.NOT_FOUND);
		return quadras;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/query")
	@ApiOperation(value = "Busca todas as quadras com query params", notes = "Busca todas as quadras com query params!")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public List<Quadra> listQuadrasPorParametros(@QueryParam("cidade") String cidade, @QueryParam("bairro") String bairro, @QueryParam("cep") String cep) {
		StringBuilder query = montaQuery(cidade, bairro, cep);
		Query hibernateQuery = setaParametros(cidade, bairro, cep, query);
		@SuppressWarnings("unchecked")
		ArrayList<Quadra> quadras = (ArrayList<Quadra>) hibernateQuery.list();
		if (quadras == null || quadras.isEmpty()) throw new WebApplicationException(Response.Status.NOT_FOUND);
		return quadras;
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Busca uma quadra pelo ID", notes = "Busca uma quadra pelo ID!")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public Quadra getQuadraById(@PathParam("id") Integer id) {
		Quadra quadra = repository.findById(id);
		if (quadra == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
		return quadra;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Grava uma quadra na base", notes = "Grava uma quadra na base!")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public Response saveQuadra(Quadra quadra) {
		return salvar(quadra);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Atualiza uma quadra na base", notes = "Atualiza uma quadra na base!")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public Response updateQuadra(Quadra quadra) {
		return salvar(quadra);
	}
	
	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Deleta uma quadra pelo ID", notes = "Deleta uma quadra pelo ID!")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public Response deleteQuadraById(@PathParam("id") Integer id) {
		try {
			repository.deletar(id);
			JsonObject value = Json.createObjectBuilder().add("Item Deletado", "Item Deletado " + id).build();
			return Response.status(200).entity(value).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	private Response salvar(Quadra quadra) {
		Response.ResponseBuilder builder = null;
		try {
			if(quadra.getIdLocal() == null) {
				return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponseEntity("Não é possível cadastrar uma quadra desvinculada de um local")).build();
			}
			quadra.setLocal(localRepository.findById(quadra.getIdLocal()));
			Quadra quadraSalvo = repository.salvar(quadra);
			return Response.status(200).entity(quadraSalvo).build();
		} catch (Exception e) {
			Map<String, String> responseObj = buildErrorResponseEntity(e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}
		return builder.build();
	}

	private Map<String, String> buildErrorResponseEntity(String error) {
		Map<String, String> responseObj = new HashMap<String, String>();
		responseObj.put("error", error);
		return responseObj;
	}

	private Query setaParametros(String cidade, String bairro, String cep, StringBuilder query) {
		Query hibernateQuery = repository.getHibernateQuery(query.toString());
		if (!StringUtils.isEmpty(cidade)) hibernateQuery.setParameter("cidade", cidade);
		if (!StringUtils.isEmpty(bairro)) hibernateQuery.setParameter("bairro", bairro);
		if (!StringUtils.isEmpty(cep)) hibernateQuery.setParameter("cep", cep);
		return hibernateQuery;
	}
	
	private StringBuilder montaQuery(String cidade, String bairro, String cep) {
		StringBuilder query = new StringBuilder("select q from Quadra q inner join q.local l ");
		query.append("where 1 = 1 ");
		if (!StringUtils.isEmpty(cidade)) query.append(" and q.local.cidade = :cidade");
		if (!StringUtils.isEmpty(bairro)) query.append(" and q.local.bairro = :bairro");
		if (!StringUtils.isEmpty(cep)) query.append(" and q.local.cep = :cep");
		return query;
	}
}