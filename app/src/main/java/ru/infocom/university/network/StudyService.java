package ru.infocom.university.network;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.infocom.university.model.request.AuthorizationRequestEnvelop;
import ru.infocom.university.modules.academicPlan.model.request.CurriculumLoadRequestEnvelop;
import ru.infocom.university.model.request.CurriculumTermsRequestEnvelop;
import ru.infocom.university.modules.grades.model.request.EducationPerformanceRequestEnvelop;
import ru.infocom.university.model.request.RecordBooksRequestEnvelop;
import ru.infocom.university.model.response.AuthorizationResponseEnvelop;
import ru.infocom.university.modules.academicPlan.model.response.CurriculumLoadResponseEnvelop;
import ru.infocom.university.model.response.CurriculumTermsResponseEnvelop;
import ru.infocom.university.modules.grades.model.response.EducationPerformanceResponseEnvelop;
import ru.infocom.university.model.response.RecordBooksResponseEnvelop;
import ru.infocom.university.modules.schedule.model.response.ScheduleResponseEnvelop;
import rx.Observable;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

public interface StudyService {

    @POST("{id}/Study.1cws")
    Observable<AuthorizationResponseEnvelop> authorization(@Path("id") int universityId, @Body AuthorizationRequestEnvelop request);

    @POST("{id}/Study.1cws")
    Observable<RecordBooksResponseEnvelop> getRecordBooks(@Path("id") int universityId, @Body RecordBooksRequestEnvelop request);

    @POST("{id}/Study.1cws")
    Observable<EducationPerformanceResponseEnvelop> getEducationPerformance(@Path("id") int universityId, @Body EducationPerformanceRequestEnvelop request);

    @POST("{id}/Study.1cws")
    Observable<CurriculumTermsResponseEnvelop> getCurriculumTerms(@Path("id") int universityId, @Body CurriculumTermsRequestEnvelop request);

    @POST("{id}/Study.1cws")
    Observable<CurriculumLoadResponseEnvelop> getCurriculumLoad(@Path("id") int universityId, @Body CurriculumLoadRequestEnvelop request);

    @POST("{id}/Study.1cws")
    Observable<ScheduleResponseEnvelop> getSchedule(@Path("id") int universityId, @Body ru.infocom.university.modules.schedule.model.request.ScheduleRequestEnvelop request);

    @POST("{id}/Study.1cws")
    Observable<ScheduleResponseEnvelop> getSchedule(@Path("id") int universityId, @Body ru.infocom.university.modules.scheduleV1.model.request.ScheduleRequestEnvelop request);
}
