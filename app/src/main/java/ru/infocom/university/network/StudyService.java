package ru.infocom.university.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.infocom.university.model.request.AuthorizationRequestEnvelop;
import ru.infocom.university.model.request.CurriculumLoadRequestEnvelop;
import ru.infocom.university.model.request.CurriculumTermsRequestEnvelop;
import ru.infocom.university.model.request.EducationPerformanceRequestEnvelop;
import ru.infocom.university.model.request.RecordBooksRequestEnvelop;
import ru.infocom.university.model.response.AuthorizationResponseEnvelop;
import ru.infocom.university.model.response.CurriculumLoadResponseEnvelop;
import ru.infocom.university.model.response.CurriculumTermsResponseEnvelop;
import ru.infocom.university.model.response.EducationPerformanceResponseEnvelop;
import ru.infocom.university.model.response.RecordBooksResponseEnvelop;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

public interface StudyService {

    @POST("{id}/Study.1cws")
    Call<AuthorizationResponseEnvelop> authorization(@Path("id") int universityId, @Body AuthorizationRequestEnvelop request);

    @POST("{id}/Study.1cws")
    Call<RecordBooksResponseEnvelop> getRecordBooks(@Path("id") int universityId, @Body RecordBooksRequestEnvelop request);

    @POST("{id}/Study.1cws")
    Call<EducationPerformanceResponseEnvelop> getEducationPerformance(@Path("id") int universityId, @Body EducationPerformanceRequestEnvelop request);

    @POST("{id}/Study.1cws")
    Call<CurriculumTermsResponseEnvelop> getCurriculumTerms(@Path("id") int universityId, @Body CurriculumTermsRequestEnvelop request);

    @POST("{id}/Study.1cws")
    Call<CurriculumLoadResponseEnvelop> getCurriculumLoad(@Path("id") int universityId, @Body CurriculumLoadRequestEnvelop request);

}
