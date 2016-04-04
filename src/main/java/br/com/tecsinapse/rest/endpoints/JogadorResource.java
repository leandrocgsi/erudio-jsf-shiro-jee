package br.com.tecsinapse.rest.endpoints;

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

import br.com.tecsinapse.model.Jogador;
import br.com.tecsinapse.model.Local;
import br.com.tecsinapse.service.JogadorService;

@Stateless
@Path("/jogador")
@Api(value = "/jogador", description = "Permite realizar operações CRUD de Jogadores!")
public class JogadorResource {

	@Inject
	private JogadorService repository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Busca todos os jogadores", notes = "Busca todos os jogadores!")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public List<Jogador> listAllJogadores() {
		ArrayList<Jogador> jogadores = repository.findAllJogadores();
		if (jogadores == null || jogadores.isEmpty()) throw new WebApplicationException(Response.Status.NOT_FOUND);
		return jogadores;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/query")
	@ApiOperation(value = "Busca todos os jogadores com query params", notes = "Busca todos os jogadores com query params!")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public List<Jogador> listJogadoresPorParametros(@QueryParam("cidade") String cidade, @QueryParam("bairro") String bairro, @QueryParam("cep") String cep) {
		StringBuilder query = montaQuery(cidade, bairro, cep);
		Query hibernateQuery = setaParametros(cidade, bairro, cep, query);
		@SuppressWarnings("unchecked")
		ArrayList<Jogador> jogadores = (ArrayList<Jogador>) hibernateQuery.list();
		if (jogadores == null || jogadores.isEmpty()) throw new WebApplicationException(Response.Status.NOT_FOUND);
		return jogadores;
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Busca uma jogador pelo ID", notes = "Busca uma jogador pelo ID!")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public Jogador getJogadorById(@PathParam("id") Integer id) {
		Jogador jogador = repository.findById(id.longValue());
		if (jogador == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
		return jogador;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Grava uma jogador na base", notes = "Grava uma jogador na base!")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public Response saveJogador(Jogador jogador) {
		return salvar(jogador);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gravaLocaisJogos")
	@ApiOperation(value = "Vincula um jogador a um local de jogos", notes = "Vincula um jogador a um local de jogos!")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public Response gravaLocaisJogos(Integer id, List<Local> locais) {
		Response.ResponseBuilder builder = null;
		try {
			repository.gravaLocaisJogos(locais, id);
			JsonObject value = Json.createObjectBuilder().add("Vinculação realizada", "O jogador de código " + id + " foi vinculado com sucesso!").build();
			return Response.status(200).entity(value).build();
		} catch (Exception e) {
			Map<String, String> responseObj = buildErrorResponseEntity(e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}
		return builder.build();
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/seguirLocaisJogos")
	@ApiOperation(value = "Possibilita a um jogador seguir local de jogos", notes = "Possibilita a um jogador seguir local de jogos!")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public Response seguirLocaisJogos(Integer id, List<Local> locais, Boolean silencioso) {
		Response.ResponseBuilder builder = null;
		try {
			repository.seguirLocaisJogos(locais, id, silencioso);
			JsonObject value = Json.createObjectBuilder().add("Vinculação realizada", "Agora o jogador de código " + id + " está seguindo o local!").build();
			return Response.status(200).entity(value).build();
		} catch (Exception e) {
			Map<String, String> responseObj = buildErrorResponseEntity(e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}
		return builder.build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Atualiza uma jogador na base", notes = "Atualiza uma jogador na base!")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public Response updateJogador(Jogador jogador) {
		return salvar(jogador);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/bloquearJogador/{id:[0-9][0-9]*}")
	@ApiOperation(value = "Bloqueia o acesso de um Jogador ao sistema", notes = "Bloqueia o acesso de um Jogador ao sistema!")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public Response bloquearJogador(@PathParam("id") Integer id) {
		Jogador jogador = repository.findById(id.longValue());
		String mensagem = "O jogador de código " + id + " foi vinculado com sucesso!";
		if (jogador == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
		return bloquearDesbloquearJogador(jogador, mensagem, false);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/desbloquearJogador/{id:[0-9][0-9]*}")
	@ApiOperation(value = "Desbloqueia o acesso de um Jogador ao sistema", notes = "Desbloqueia o acesso de um Jogador ao sistema!")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public Response desbloquearJogador(@PathParam("id") Integer id) {
		Jogador jogador = repository.findById(id.longValue());
		String mensagem = "O jogador de código " + id + " foi desbloqueado com sucesso!";
		if (jogador == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
		return bloquearDesbloquearJogador(jogador, mensagem, true);
	}
	
	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Deleta uma jogador pelo ID", notes = "Deleta uma jogador pelo ID!")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Erro interno no servidor")})
	public Response deleteJogadorById(@PathParam("id") Integer id) {
		try {
			repository.deletar(id.longValue());
			JsonObject value = Json.createObjectBuilder().add("Item Deletado", "Item Deletado " + id).build();
			return Response.status(200).entity(value).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}

	private Response bloquearDesbloquearJogador(Jogador jogador, String mensagem, boolean ativo) {
		Response.ResponseBuilder builder = null;
		try {
			repository.bloquearDesbloquearJogador(jogador, ativo);
			JsonObject value = Json.createObjectBuilder().add("Vinculação/Desvinculação realizada", mensagem).build();
			return Response.status(200).entity(value).build();
		} catch (Exception e) {
			Map<String, String> responseObj = buildErrorResponseEntity(e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}
		return builder.build();
	}
	
	private Response salvar(Jogador jogador) {
		Response.ResponseBuilder builder = null;
		try {
			Jogador jogadorSalvo = repository.save(jogador);
			return Response.status(200).entity(jogadorSalvo).build();
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
		StringBuilder query = new StringBuilder("select q from Jogador q inner join q.local l ");
		query.append("where 1 = 1 ");
		if (!StringUtils.isEmpty(cidade)) query.append(" and q.local.cidade = :cidade");
		if (!StringUtils.isEmpty(bairro)) query.append(" and q.local.bairro = :bairro");
		if (!StringUtils.isEmpty(cep)) query.append(" and q.local.cep = :cep");
		return query;
	}
}