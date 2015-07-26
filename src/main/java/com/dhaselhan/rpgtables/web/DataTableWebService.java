package com.dhaselhan.rpgtables.web;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.dhaselhan.rpgtables.data.DataTable;
import com.dhaselhan.rpgtables.services.DataTablePersistenceService;

@Path("/table")
public class DataTableWebService {

	DataTablePersistenceService dataTablePersistenceService;

	public DataTableWebService() {
		dataTablePersistenceService = new DataTablePersistenceService();
	}

	@GET
	@Path("/{id}")
	public Response getTable(@PathParam("id") String id) {
		DataTable result = dataTablePersistenceService.findById(id);
		if (result != null) {
			return Response.status(200).entity(result).build();
		} else {
			return Response.status(404).build();
		}
	}
	
	@GET
	@Path("/sample")
	public Response getTable() {
		DataTable result = dataTablePersistenceService.createTestTable();
		if (result != null) {
			return Response.status(200).entity(result).build();
		} else {
			return Response.status(404).build();
		}
	}
	
	@GET
	@Path("/empty")
	public Response getEmptyTable() {
		DataTable result = dataTablePersistenceService.createEmptyTable();
		if (result != null) {
			return Response.status(200).entity(result).build();
		} else {
			return Response.status(404).build();
		}
	}

	@POST
	@Path("/{id}")
	public Response saveTable(DataTable updatedTable) {
		DataTable savedTable = dataTablePersistenceService.saveTable(updatedTable);
		return Response.status(200).entity(savedTable).build();
	}

	@POST
	@Path("/create")
	public Response createTable(DataTable newTable) {
		DataTable savedTable = dataTablePersistenceService.saveTable(newTable);
		return Response.status(200).entity(savedTable).build();
	}

	@GET
	@Path("/")
	public Response getAllTables() {
		Collection<DataTable> tables = dataTablePersistenceService
				.findAllTables();

		return Response.status(200).entity(tables).build();
	}
	
	@GET
	@Path("/recent")
	public Response getRecentlyModifiedTables() {
		Collection<DataTable> tables = dataTablePersistenceService
				.findRecentlyUpdatedTables(5);

		return Response.status(200).entity(tables).build();
	}
	

	@GET
	@Path("{username}/tables")
	public Response findTablesByUser(@PathParam("username") String username) {
		Collection<DataTable> tables = dataTablePersistenceService.findAllTablesByUsername(username);

		return Response.status(200).entity(tables).build();
	}

}
