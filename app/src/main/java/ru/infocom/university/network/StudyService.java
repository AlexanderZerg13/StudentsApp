package ru.infocom.university.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.infocom.university.model.request.AuthorizationRequestEnvelop;
import ru.infocom.university.model.response.AuthorizationResponseEnvelop;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

public interface StudyService {

    @Headers({
            "Content-Type: application/xml"
    })
    @POST("{id}/Study.1cws")
    Call<AuthorizationResponseEnvelop> authorization(@Path("id") int universityId, @Body AuthorizationRequestEnvelop authorizationRequestEnvelop);
}
