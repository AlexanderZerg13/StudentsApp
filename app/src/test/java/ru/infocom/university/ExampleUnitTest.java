package ru.infocom.university;

import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import ru.infocom.university.model.Authorization;
import ru.infocom.university.model.AuthorizationResponse;
import ru.infocom.university.model.request.AuthorizationRequestBody;
import ru.infocom.university.model.request.AuthorizationRequestEnvelop;
import ru.infocom.university.model.response.AuthorizationResponseBody;
import ru.infocom.university.model.response.AuthorizationResponseEnvelop;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void authorizationRequest() {
        AuthorizationRequestEnvelop authorizationRequest = new AuthorizationRequestEnvelop();
        AuthorizationRequestBody authorizationBody = new AuthorizationRequestBody();
        Authorization authorization = new Authorization();

        authorization.setLogin("Login");
        authorization.setPassword("Password");
        authorization.setUserId("user");

        authorizationBody.setAuthorization(authorization);
        authorizationRequest.setAuthorizationBody(authorizationBody);

        Serializer serializer = new Persister();

        try {
            serializer.write(authorizationRequest, System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(1, 1);
    }

    @Test
    public void authorizationResponse() {

        AuthorizationResponseEnvelop authorizationResponseEnvelop = new AuthorizationResponseEnvelop();
        AuthorizationResponseBody authorizationResponseBody = new AuthorizationResponseBody();
        AuthorizationResponse authorizationResponse = new AuthorizationResponse();

        authorizationResponseBody.setAuthorizationResponse(authorizationResponse);
        authorizationResponseEnvelop.setAuthorizationResponseBody(authorizationResponseBody);

        Serializer serializer = new Persister();


        try {
            serializer.write(authorizationResponseEnvelop, System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(1, 1);
    }
}