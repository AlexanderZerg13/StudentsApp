package ru.infocom.university.network;

import android.content.res.Resources;
import android.util.Log;
import org.simpleframework.xml.core.ElementException;
import java.net.ConnectException;
import retrofit2.HttpException;
import ru.infocom.university.R;

public abstract class ExceptionUtils {
    public static String getErrorText(Throwable throwable, Resources resources) {
        Log.i("ExceptionUtils", "getErrorText: " + throwable);
        String error = ExceptionUtils.getErrorTextReq(throwable, resources);
        if (error == null) {
            error = ExceptionUtils.getErrorTextReq(throwable.getCause(), resources);
        }

        if (error == null) {
            return resources.getString(R.string.server_error);
        } else {
            return error;
        }
    }

    private static String getErrorTextReq(Throwable throwable, Resources resources) {
        if (throwable instanceof ConnectException) {
            return resources.getString(R.string.no_server_response);
        } else if (throwable instanceof HttpException) {
            return resources.getString(R.string.server_error);
        } else if (throwable instanceof ElementException) {
            return resources.getString(R.string.response_can_not_be_parse);
        } else if (throwable instanceof EmptyDataException) {
            return resources.getString(R.string.no_data);
        }
        return null;
    }
}
