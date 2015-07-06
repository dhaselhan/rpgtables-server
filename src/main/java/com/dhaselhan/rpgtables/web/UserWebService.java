package com.dhaselhan.rpgtables.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.token.OAuthToken;
import org.json.JSONObject;

import com.dhaselhan.rpgtables.data.User;
import com.dhaselhan.rpgtables.security.UserService;
import com.dhaselhan.rpgtables.services.SessionService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.jersey.core.util.Base64;

@Path("/users")
public class UserWebService {

	@Context
	private UriInfo uriInfo;

	private static final String CLIENT_ID = "";
	
	private static final String CLIENT_SECRET = "";
	
	private static final String CALLBACK_PATH = "/users/oauth2callback";

	UserService userService;
	
	SessionService sessionService;

	public UserWebService() {
		userService = new UserService();
		sessionService = new SessionService();
	}

	@GET
	@Produces("text/html")
	public Response authenticate() {
		try {
			OAuthClientRequest request = OAuthClientRequest
					.authorizationProvider(OAuthProviderType.GOOGLE)
					.setClientId(CLIENT_ID)
					.setResponseType("code")
					.setScope(
							"openid email https://www.googleapis.com/auth/plus.login")
					.setRedirectURI(
							UriBuilder.fromUri(uriInfo.getBaseUri())
									.path(CALLBACK_PATH).build().toString())
					.buildQueryMessage();
			URI redirect = new URI(request.getLocationUri());
			return Response.seeOther(redirect).build();
		} catch (OAuthSystemException e) {
			throw new WebApplicationException(e);
		} catch (URISyntaxException e) {
			throw new WebApplicationException(e);
		}
	}

	@GET
	@Path("oauth2callback")
	public Response authorize(@QueryParam("code") String code,
			@QueryParam("error") String error) {

		if (error != null && !error.isEmpty()) {
			//Auth Failed!
			return Response.status(Status.FORBIDDEN).build();
		}
		try {
			// Request to exchange code for access token and id token
			OAuthClientRequest request = OAuthClientRequest
					.tokenProvider(OAuthProviderType.GOOGLE)
					.setCode(code)	
					.setClientId(CLIENT_ID)
					.setClientSecret(CLIENT_SECRET)
					.setRedirectURI(
							UriBuilder.fromUri(uriInfo.getBaseUri())
									.path(CALLBACK_PATH)
									.build()
									.toString())
					.setGrantType(GrantType.AUTHORIZATION_CODE)
					.buildBodyMessage();

			OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
			OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient
					.accessToken(request);

			// Get the access token from the response
			OAuthToken accessToken = oAuthResponse.getOAuthToken();

			// requires import com.google.gson.JsonPrimitive;
			String idToken = oAuthResponse.getParam("id_token");
			String jsonToken = deserialize(idToken);
			JSONObject identity = new JSONObject(jsonToken);
			String userEmail = (String) identity.get("email");
			User loggedInUser = findOrCreateUser(userEmail);
			sessionService.registerSession(idToken, loggedInUser);
			// Add code to notify application of authenticated user
			//Create User If Not Exist
			return Response.ok(accessToken).build();
		} catch (OAuthSystemException e) {
			throw new WebApplicationException(e);
		} catch (OAuthProblemException e) {
			throw new WebApplicationException(e);
		}
	}
	
	private User findOrCreateUser(String email) {
		User result = userService.findById(email);
		if(result == null) {
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
        JsonElement payload = parser.parse(StringUtils.newStringUtf8(Base64.decode(jwtPayloadSegment)));
        return payload.toString();
    }

    /**
     * @param tokenString The original encoded representation of a JWT
     * @return Three components of the JWT as an array of strings
     */
    private String[] splitTokenString(String tokenString) {
        String[] pieces = tokenString.split(Pattern.quote("."));
        if (pieces.length != 3) {
            throw new IllegalStateException("Expected JWT to have 3 segments separated by '"
                    + "." + "', but it has " + pieces.length + " segments");
        }
        return pieces;
    }

}
