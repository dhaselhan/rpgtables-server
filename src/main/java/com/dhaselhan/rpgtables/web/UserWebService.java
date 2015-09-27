package com.dhaselhan.rpgtables.web;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.binary.StringUtils;

import com.dhaselhan.rpgtables.model.DataTable;
import com.dhaselhan.rpgtables.model.User;
import com.dhaselhan.rpgtables.security.UserService;
import com.dhaselhan.rpgtables.services.SessionService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@Path("/users")
public class UserWebService {

	@Context
	private UriInfo uriInfo;

	private static final String CLIENT_ID = "411637336320-sn1ru1021fg37ass1kuev363csejn3dk.apps.googleusercontent.com";

	//private static final String APPS_DOMAIN_NAME = "localhost";

	UserService userService;

	SessionService sessionService;

	public UserWebService() {
		userService = UserService.getUserService();
		sessionService = SessionService.getSessionService();
	}
	
	@GET
	@Path("{id}/tables")
	public Response getUsersTables(@PathParam("id") String userName) {
		User user = userService.findById(userName);
		Collection<DataTable> usersTables = user.getUsersTables();
		return Response.status(200).entity(usersTables).build();
	}

	@POST
	@Path("login")
	public Response login(String token) {
		String accessToken = validateToken(token);
		return Response.status(200).entity(accessToken).build();
	}

	private String validateToken(String token) {
		try {
			JsonFactory jsonFactory = new JacksonFactory();
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
					new NetHttpTransport(), jsonFactory).setAudience(
					Arrays.asList(CLIENT_ID)).build();

			GoogleIdToken idToken = verifier.verify(token);
			if (idToken != null) {
				Payload payload = idToken.getPayload();
				//if (payload.getHostedDomain().equals(APPS_DOMAIN_NAME)) {
					User user = findOrCreateUser(payload.getEmail());
					Date expiryTime = new Date(payload.getExpirationTimeSeconds() * 1000);
					sessionService.registerSession(payload.getAccessTokenHash(), user, expiryTime);
					System.out.println("User ID: " + payload.getSubject());
					return payload.getAccessTokenHash();
				//} else {
					//System.out.println("Invalid ID token.");
				//}
			} else {
				System.out.println("Invalid ID token.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private User findOrCreateUser(String email) {
		User result = userService.findById(email);
		if (result == null) {
			User user = new User();
			user.setUsername(email);
			result = userService.createUser(user);
		}
		return result;
	}

	public String deserialize(String tokenString) {
		String[] pieces = splitTokenString(tokenString);
		String jwtPayloadSegment = pieces[1];
		JsonParser parser = new JsonParser();
		JsonElement payload = parser.parse(StringUtils.newStringUtf8(Base64
				.decodeBase64(jwtPayloadSegment)));
		return payload.toString();
	}

	/**
	 * @param tokenString
	 *            The original encoded representation of a JWT
	 * @return Three components of the JWT as an array of strings
	 */
	private String[] splitTokenString(String tokenString) {
		String[] pieces = tokenString.split(Pattern.quote("."));
		if (pieces.length != 3) {
			throw new IllegalStateException(
					"Expected JWT to have 3 segments separated by '" + "."
							+ "', but it has " + pieces.length + " segments");
		}
		return pieces;
	}

}
