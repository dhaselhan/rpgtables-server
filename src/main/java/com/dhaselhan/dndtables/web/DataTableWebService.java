package com.dhaselhan.dndtables.web;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.dhaselhan.dndtables.data.DataTable;
import com.dhaselhan.dndtables.services.DataTablePersistenceService;

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
	@Path("/")
	public Response getAllTables() {
		Collection<DataTable> tables = dataTablePersistenceService
				.findAllTables();

		return Response.status(200).entity(tables).build();
	}

}
