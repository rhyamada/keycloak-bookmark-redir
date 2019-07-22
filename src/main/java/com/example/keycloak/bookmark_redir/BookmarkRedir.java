/*
*/
package com.example.keycloak.bookmark_redir;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.keycloak.Config.Scope;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel.Requirement;
import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.provider.ProviderConfigProperty;

public class BookmarkRedir implements AuthenticatorFactory {
    static class Bookmark implements Authenticator {
        @Override
        public void authenticate(AuthenticationFlowContext context) {
            MultivaluedMap<String, String> params = context.getHttpRequest().getUri().getQueryParameters();
            if (params.containsKey("bookmarked")) {
                ClientModel client = (ClientModel) context.getRealm().getClientByClientId(params.getFirst("client_id"));
                String url = client.getBaseUrl();
                if (!url.isEmpty()) {
                    context.forceChallenge(Response.status(302).header("Location", url).build());
                    return;
                }
            }
            context.attempted();
        }

        @Override
        public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
            return false;
        }

        @Override
        public boolean requiresUser() {
            return false;
        }

        @Override
        public void action(AuthenticationFlowContext context) {
        }

        @Override
        public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        }

        @Override
        public void close() {
        }
    }

    static final Bookmark SINGLETON = new Bookmark();

    @Override
    public Authenticator create(KeycloakSession session) {
        return SINGLETON;
    }

    @Override
    public String getId() {
        return "bookmark-redirect";
    }

    public static final Requirement[] REQUIREMENT_CHOICES = { Requirement.ALTERNATIVE, Requirement.DISABLED };

    @Override
    public Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public String getDisplayType() {
        return "Bookmark redirect";
    }

    @Override
    public String getReferenceCategory() {
        return "Bookmark redirect";
    }

    @Override
    public String getHelpText() {
        return "Detect bookmark and redirects back client:Base URL";
    }

    @Override
    public void init(Scope config) {
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return new ArrayList<ProviderConfigProperty>();
    }
}