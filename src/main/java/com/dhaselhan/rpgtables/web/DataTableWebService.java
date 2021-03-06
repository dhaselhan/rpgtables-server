package com.dhaselhan.rpgtables.web;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;

import com.dhaselhan.rpgtables.filter.UserNameFilter;
import com.dhaselhan.rpgtables.model.DataTable;
import com.dhaselhan.rpgtables.model.User;
import com.dhaselhan.rpgtables.security.UserService;
import com.dhaselhan.rpgtables.services.DataTablePersistenceService;

@Path("/table")
public class DataTableWebService {

	private DataTablePersistenceService dataTablePersistenceService;

	private UserService userService;

	public DataTableWebService() {
		userService = UserService.getUserService();
		dataTablePersistenceService = DataTablePersistenceService.getDataTablePersistenceService();
	}

	@GET
	@Path("/{id}")
	public Response getTable(@PathParam("id") int id, @Context HttpServletRequest request) {
		DataTable result = dataTablePersistenceService.findById(id);
		if (result != null) {
			String userName = (String) request.getAttribute(UserNameFilter.USERNAME);
			if (result.getOwner() != null && result.getOwner().getUsername().equals(userName)) {
				result.setCanEdit(true);
			}
			return Response.status(HttpStatus.SC_OK).entity(result).build();
		} else {
			return Response.status(404).build();
		}
	}

	@POST
	@Path("/{id}")
	public Response saveTable(DataTable updatedTable, @Context HttpServletRequest request) {
		DataTable result = dataTablePersistenceService.findById(updatedTable.getId());
		if (result != null) {
			String userName = (String) request.getAttribute(UserNameFilter.USERNAME);
			if (result.getOwner() != null && result.getOwner().getUsername().equals(userName)) {
				DataTable savedTable = dataTablePersistenceService.saveTable(updatedTable);
				return Response.status(HttpStatus.SC_OK).entity(savedTable).build();
			}
		}
		return Response.status(HttpStatus.SC_FORBIDDEN).build();
	}

	@POST
	@Path("/create")
	public Response createTable(DataTable newTable, @Context HttpServletRequest request) {
		String userName = (String) request.getAttribute(UserNameFilter.USERNAME);
		if (userName != null) {
			User currentUser = userService.findById(userName);
			currentUser.getUsersTables().add(newTable);
			newTable.setOwner(currentUser);
		}
		DataTable result = dataTablePersistenceService.saveTable(newTable);
		return Response.status(HttpStatus.SC_OK).entity(result).build();
	}
	
	@GET
	@Path("/sample")
	public Response getTable() {
		DataTable result = dataTablePersistenceService.createTestTable();
		if (result != null) {
			return Response.status(HttpStatus.SC_OK).entity(result).build();
		} else {
			return Response.status(404).build();
		}
	}
	
	@GET
	@Path("/empty")
	public Response getEmptyTable() {
		DataTable result = dataTablePersistenceService.createEmptyTable();
		if (result != null) {
			result.setCanEdit(true);
			return Response.status(HttpStatus.SC_OK).entity(result).build();
		} else {
			return Response.status(404).build();
		}
	}

	@GET
	@Path("/")
	public Response getAllTables() {
		Collection<DataTable> tables = dataTablePersistenceService
				.findAllTables();

		return Response.status(HttpStatus.SC_OK).entity(tables).build();
	}
	
	@GET
	@Path("/recent")
	public Response getRecentlyModifiedTables() {
		Collection<DataTable> tables = dataTablePersistenceService
				.findRecentlyUpdatedTables(5);

		return Response.status(HttpStatus.SC_OK).entity(tables).build();
	}
	

	@GET
	@Path("/user/{username}")
	public Response findTablesByUser(@PathParam("username") String username) {
		Collection<DataTable> tables = dataTablePersistenceService.findAllTablesByUsername(username);

		return Response.status(HttpStatus.SC_OK).entity(tables).build();
	}

}
