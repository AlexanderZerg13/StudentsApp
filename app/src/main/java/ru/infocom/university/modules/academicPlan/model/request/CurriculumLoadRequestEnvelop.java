package ru.infocom.university.modules.academicPlan.model.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import ru.infocom.university.modules.academicPlan.model.GetCurriculumLoad;

/**
 * Created by Alexander Pilipenko on 27.09.2017.
 */

@Root(name = "Envelope")
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope")
public class CurriculumLoadRequestEnvelop {

    public static CurriculumLoadRequestEnvelop generate(String curriculumId, String termId) {
        CurriculumLoadRequestEnvelop request = new CurriculumLoadRequestEnvelop();
        request.setGetCurriculumLoad(new GetCurriculumLoad(curriculumId, termId));
        return request;
    }

    @Namespace(reference = "http://sgu-infocom.ru/study")
    @Element(name = "GetCurriculumLoad")
    @Path("Body")
    private GetCurriculumLoad mGetCurriculumLoad;

    public GetCurriculumLoad getGetCurriculumLoad() {
        return mGetCurriculumLoad;
    }

    public void setGetCurriculumLoad(GetCurriculumLoad getCurriculumLoad) {
        mGetCurriculumLoad = getCurriculumLoad;
    }
}
