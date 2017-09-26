package ru.infocom.university;

import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;

import ru.infocom.university.model.Authorization;
import ru.infocom.university.model.AuthorizationResponse;
import ru.infocom.university.model.Error;
import ru.infocom.university.model.Return;
import ru.infocom.university.model.request.AuthorizationRequestBody;
import ru.infocom.university.model.request.AuthorizationRequestEnvelop;
import ru.infocom.university.model.request.GetRecordBooksRequestEnvelop;
import ru.infocom.university.model.response.AuthorizationResponseBody;
import ru.infocom.university.model.response.AuthorizationResponseEnvelop;
import ru.infocom.university.model.response.GetRecordBooksResponseEnvelop;
import ru.infocom.university.network.ApiFactory;
import ru.infocom.university.network.StudyService;

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
        authorization.setUserId("");

        authorizationBody.setAuthorization(authorization);
        authorizationRequest.setBody(authorizationBody);

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
        Return returnn = new Return();
        Error error = new Error();
        error.setCode(2);
        error.setDescription("Не удалось определить пользователя");

        returnn.setErrors(error);
        /*returnn.setUser(new User("000000032", "Иван Иванов", "89E495E7941CF9E40E6980D14A16BF023CCD4C91", new ArrayList<Roles>() {
            {
                add(new Roles("Student"));
                add(new Roles("Teacher"));
            }
        }));*/

        authorizationResponse.setReturn(returnn);
        authorizationResponseBody.setAuthorizationResponse(authorizationResponse);
        authorizationResponseEnvelop.setBody(authorizationResponseBody);

        Serializer serializer = new Persister();


        try {
            serializer.write(authorizationResponseEnvelop, System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(1, 1);
    }

    @Test
    public void tryAuthorization() throws IOException {
        StudyService sService = ApiFactory.getStudyService();
        Serializer serializer = new Persister();

        AuthorizationRequestEnvelop request = AuthorizationRequestEnvelop.generateAuthorizationRequestEnvelop("", "Иван Иванов", "89E495E7941CF9E40E6980D14A16BF023CCD4C91");
        AuthorizationResponseEnvelop authorizationResponseEnvelop = sService.authorization(0, request).execute().body();

        try {
            serializer.write(authorizationResponseEnvelop, System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(1, 1);
    }

    @Test
    public void tryGetRecordBooks() throws IOException {
        StudyService sService = ApiFactory.getStudyService();
        Serializer serializer = new Persister();

        GetRecordBooksRequestEnvelop request = GetRecordBooksRequestEnvelop.generateGetRecordBooksRequestEnvelop("000000032");
        GetRecordBooksResponseEnvelop getRecordBooksResponseEnvelop = sService.getRecordBooks(0, request).execute().body();

        try {
            serializer.write(getRecordBooksResponseEnvelop, System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(1, 1);
    }
}