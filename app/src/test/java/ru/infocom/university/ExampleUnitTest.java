package ru.infocom.university;

import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;

import java.io.IOException;
import java.util.Date;

import ru.infocom.university.model.Authorization;
import ru.infocom.university.model.Error;
import ru.infocom.university.modules.schedule.model.GetSchedule;
import ru.infocom.university.model.Return;
import ru.infocom.university.model.ReturnContainer;
import ru.infocom.university.model.request.AuthorizationRequestEnvelop;
import ru.infocom.university.modules.academicPlan.model.request.CurriculumLoadRequestEnvelop;
import ru.infocom.university.model.request.CurriculumTermsRequestEnvelop;
import ru.infocom.university.modules.grades.model.request.EducationPerformanceRequestEnvelop;
import ru.infocom.university.model.request.RecordBooksRequestEnvelop;
import ru.infocom.university.modules.schedule.model.request.ScheduleRequestEnvelop;
import ru.infocom.university.model.response.AuthorizationResponseEnvelop;
import ru.infocom.university.modules.academicPlan.model.response.CurriculumLoadResponseEnvelop;
import ru.infocom.university.model.response.CurriculumTermsResponseEnvelop;
import ru.infocom.university.modules.grades.model.response.EducationPerformanceResponseEnvelop;
import ru.infocom.university.model.response.RecordBooksResponseEnvelop;
import ru.infocom.university.modules.schedule.model.response.ScheduleResponseEnvelop;
import ru.infocom.university.network.ApiFactory;
import ru.infocom.university.network.StudyService;
import ru.infocom.university.utils.DateFormatTransformer;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void authorizationRequest() {
        AuthorizationRequestEnvelop authorizationRequest = new AuthorizationRequestEnvelop();
        Authorization authorization = new Authorization();

        authorization.setLogin("Login");
        authorization.setPassword("Password");
        authorization.setUserId("");

        authorizationRequest.setAuthorization(authorization);

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
        ReturnContainer returnContainer = new ReturnContainer();
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

        returnContainer.setReturn(returnn);
        authorizationResponseEnvelop.setReturnContainer(returnContainer);

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

        AuthorizationRequestEnvelop request = AuthorizationRequestEnvelop.generate("", "Иван Иванов", "89E495E7941CF9E40E6980D14A16BF023CCD4C91");
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

        RecordBooksRequestEnvelop request = RecordBooksRequestEnvelop.generate("000000032");
        RecordBooksResponseEnvelop recordBooksResponseEnvelop = sService.getRecordBooks(0, request).execute().body();

        try {
            serializer.write(recordBooksResponseEnvelop, System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(1, 1);
    }

    @Test
    public void tryEducationalPerformance() throws IOException {
        StudyService sService = ApiFactory.getStudyService();
        Serializer serializer = new Persister();

        EducationPerformanceRequestEnvelop request = EducationPerformanceRequestEnvelop.generate("000000032", "000000010");
        EducationPerformanceResponseEnvelop educationPerformanceResponseEnvelop = sService.getEducationPerformance(0, request).execute().body();

        try {
            serializer.write(educationPerformanceResponseEnvelop, System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(1, 1);
    }

    @Test
    public void tryCurriculumTerm() throws IOException {
        StudyService sService = ApiFactory.getStudyService();
        Serializer serializer = new Persister();

        CurriculumTermsRequestEnvelop request = CurriculumTermsRequestEnvelop.generate("000000014");
        CurriculumTermsResponseEnvelop curriculumTermsResponseEnvelop = sService.getCurriculumTerms(0, request).execute().body();

        try {
            serializer.write(curriculumTermsResponseEnvelop, System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(1, 1);
    }

    @Test
    public void tryCurriculumLoad() throws IOException {
        StudyService sService = ApiFactory.getStudyService();
        Serializer serializer = new Persister();

        CurriculumLoadRequestEnvelop request = CurriculumLoadRequestEnvelop.generate("000000014", "000000002");
        CurriculumLoadResponseEnvelop curriculumLoadResponseEnvelop = sService.getCurriculumLoad(0, request).execute().body();

        try {
            serializer.write(curriculumLoadResponseEnvelop, System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(1, 1);
    }

    @Test
    public void GetSchedule() {
        RegistryMatcher m = new RegistryMatcher();
        m.bind(Date.class, new DateFormatTransformer());

        Serializer serializer = new Persister(m);

        GetSchedule schedule = new GetSchedule(
                "AcademicGroup",
                "e54ec3e3-94a4-11e7-8996-000c2936a65e:e54ec3cf-94a4-11e7-8996-000c2936a65e",
                "Full",
                new Date(),
                new Date());

        try {
            serializer.write(schedule, System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(1, 1);
    }

    @Test
    public void tryGetSchedule() throws IOException {
        StudyService sService = ApiFactory.getStudyService();
        Serializer serializer = new Persister();

        ScheduleRequestEnvelop request = ScheduleRequestEnvelop.generate(
                "AcademicGroup",
                "e54ec3e3-94a4-11e7-8996-000c2936a65e:e54ec3cf-94a4-11e7-8996-000c2936a65e",
                "Full",
                new Date(115, 8, 21),
                new Date(115, 8, 21));
        ScheduleResponseEnvelop scheduleResponseEnvelop = sService.getSchedule(0, request).execute().body();

        try {
            serializer.write(scheduleResponseEnvelop, System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(1, 1);
    }
}