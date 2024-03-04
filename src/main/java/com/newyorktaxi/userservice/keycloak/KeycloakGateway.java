package com.newyorktaxi.userservice.keycloak;

import com.newyorktaxi.userservice.usecase.params.UserParams;
import jakarta.ws.rs.core.Response;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KeycloakGateway {

    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    @NonFinal
    @Value("${keycloak.realm}")
    String realm;

    Keycloak keycloak;

    public String createKeycloakUser(UserParams userParams) {
        final List<UserRepresentation> list = keycloak.realm(realm).users().list();
        List<RoleRepresentation> roleRepresentations = keycloak.realm(realm).users().get(list.get(0).getId())
                .roles().realmLevel().listEffective();
        List<RoleRepresentation> newyorktaxiclient = keycloak.realm(realm).clients().findAll().stream()
                .filter(clientRepresentation -> clientRepresentation.getClientId().equals("newyorktaxiclient"))
                .findAny()
                .map(clientRepresentation -> keycloak.realm(realm).users().get(list.get(0).getId())
                        .roles().clientLevel(clientRepresentation.getId()).listEffective())
                .get();
        List<RoleRepresentation> roleRepresentations1 = keycloak.realm(realm).users().get(list.get(0).getId())
                .roles().clientLevel("newyorktaxiclient").listEffective();
        final CredentialRepresentation passwordCredentials = setCredentialRepresentation(userParams.getPassword());
        final UserRepresentation userRepresentation = buildUserRepresentation(userParams);
        final UsersResource usersResource = keycloak.realm(realm).users();
        final Response createUser = usersResource.create(userRepresentation);

        final String userId = CreatedResponseUtil.getCreatedId(createUser);

        final UserResource userResource = usersResource.get(userId);
        userResource.resetPassword(passwordCredentials);
        return userId;
    }

    public void setUserRole(String userId, String roleName) {
        final RoleRepresentation roleRepresentation = keycloak.realm(realm)
                .roles()
                .get(roleName)
                .toRepresentation();

        final UserResource userResource = keycloak.realm(realm)
                .users()
                .get(userId);

        userResource.roles()
                .realmLevel()
                .add(List.of(roleRepresentation));
    }

    private CredentialRepresentation setCredentialRepresentation(String password) {
        final CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);
        return credentialRepresentation;
    }

    private UserRepresentation buildUserRepresentation(UserParams userParams) {
        final UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userParams.getUsername());
        userRepresentation.setFirstName(userParams.getFirstName());
        userRepresentation.setLastName(userParams.getLastName());
        userRepresentation.setEmail(userParams.getEmail());
        userRepresentation.setEnabled(true);
        return userRepresentation;
    }
}
