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

import br.com.erudio.model.Local;
import br.com.erudio.service.LocalService;
import br.com.erudio.util.DateUtil;

@Stateless
@Path("/local")
@Api(value = "/local", description = "Permite realizar operações CRUD de Locais!")
public class LocalResource {

	@Inject
	private LocalService repository;
	
	@Inject
	private DateUtil dateUtil;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Busca todos os Locais", notes = "Busca todos os Locais!")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public List<Local> listAllLocals() {
		ArrayList<Local> locais = repository.findAllLocais();
		if (locais == null || locais.isEmpty()) throw new WebApplicationException(Response.Status.NOT_FOUND);
		return locais;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/query")
	@ApiOperation(value = "Busca Locais com query params", notes = "Busca Locais com query params!")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public List<Local> listLocaisPorParametros(@QueryParam("cidade") String cidade, @QueryParam("bairro") String bairro, @QueryParam("cep") String cep) {
		StringBuilder query = montaQuery(cidade, bairro, cep);
		Query hibernateQuery = setaParametros(cidade, bairro, cep, query);
		@SuppressWarnings("unchecked")
		ArrayList<Local> locais = (ArrayList<Local>) hibernateQuery.list();
		if (locais == null || locais.isEmpty()) throw new WebApplicationException(Response.Status.NOT_FOUND);
		return locais;
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Busca um Local pelo ID", notes = "Busca um Local pelo ID!")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public Local getLocalById(@PathParam("id") Integer id) {
		Local local = repository.findById(id);
		if (local == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
		return local;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Grava um Local na base", notes = "Grava um Local na base!")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public Response saveLocal(Local local) {
		return salvar(local);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Atualiza um Local na base", notes = "Atualiza um Local na base!")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public Response updateLocal(Local local) {
		return salvar(local);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/aprovarLocal/{id:[0-9][0-9]*}")
	@ApiOperation(value = "Aprova um Local recém cadastrado", notes = "Aprova um Local recém cadastrado!")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public Response aprovarLocal(@PathParam("id") Integer id) {
		Local local = repository.findById(id);
		if (local == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
		local.setStatus(true);
		return salvar(local);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Coloca um Local em destaque até uma data específica", notes = "Coloca um Local em destaque até uma data específica!")
	@Path("/destacarLocalAte/{id:[0-9][0-9]*}/{ano:[0-9][0-9]*}/{mes:[0-9][0-9]*}/{dia:[0-9][0-9]*}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public Response destacarLocalAte(
			@PathParam("id") Integer id,
			@PathParam("ano") Integer ano,
			@PathParam("mes") Integer mes,
			@PathParam("dia") Integer dia) {
		
		Local local = repository.findById(id);
		if (local == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
		String data = ano + "-" + dateUtil.getStrNumber(mes) + "-" + dateUtil.getStrNumber(dia);
		local.setDestaqueAte(dateUtil.obterDataAPartirDeString(data));;
		return salvar(local);
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Deleta um Local pelo ID", notes = "Deleta um Local pelo ID!")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public Response deleteLocalById(@PathParam("id") Integer id) {
		try {
			repository.deletar(id);
			JsonObject value = Json.createObjectBuilder().add("Item Deletado", "Item Deletado " + id).build();
			return Response.status(200).entity(value).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	private Response salvar(Local local) {
		Response.ResponseBuilder builder = null;
		try {
			// Validar
			Local localSalvo = repository.salvar(local);
			return Response.status(200).entity(localSalvo).build();
		} catch (Exception e) {
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("error", e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}
		return builder.build();
	}

	private Query setaParametros(String cidade, String bairro, String cep, StringBuilder query) {
		Query hibernateQuery = repository.getHibernateQuery(query.toString());
		if (!StringUtils.isEmpty(cidade)) hibernateQuery.setParameter("cidade", cidade);
		if (!StringUtils.isEmpty(bairro)) hibernateQuery.setParameter("bairro", bairro);
		if (!StringUtils.isEmpty(cep)) hibernateQuery.setParameter("cep", cep);
		return hibernateQuery;
	}
	
	private StringBuilder montaQuery(String cidade, String bairro, String cep) {
		StringBuilder query = new StringBuilder("from Local l where 1 = 1");
		if (!StringUtils.isEmpty(cidade)) query.append(" and l.cidade = :cidade");
		if (!StringUtils.isEmpty(bairro)) query.append(" and l.bairro = :bairro");
		if (!StringUtils.isEmpty(cep)) query.append(" and l.cep = :cep");
		return query;
	}
}