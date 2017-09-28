package ru.infocom.university.network;

import android.support.annotation.NonNull;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

import ru.arturvasilov.rxloader.RxUtils;
import ru.infocom.university.data.AuthorizationObject;
import ru.infocom.university.model.RecordBook;
import ru.infocom.university.model.Return;
import ru.infocom.university.model.Roles;
import ru.infocom.university.model.User;
import ru.infocom.university.model.request.AuthorizationRequestEnvelop;
import ru.infocom.university.model.request.RecordBooksRequestEnvelop;
import rx.Observable;

/**
 * Created by Alexander Pilipenko on 28.09.2017.
 */

public class DataRepository {
    AuthorizationObject authorizationObject;

    @NonNull
    public Observable<AuthorizationObject> authorization(@NonNull String login, @NonNull String password) {
        String passwordSha1 = new String(Hex.encodeHex(DigestUtils.sha1(password)));

        return ApiFactory.getStudyService()
                .authorization(0, AuthorizationRequestEnvelop.generate("", login, passwordSha1))
                .flatMap(authorizationResponse -> {
                    Return returnObject = authorizationResponse.getReturnContainer().getReturn();
                    if (returnObject.getUser() != null) {
                        User user = returnObject.getUser();
                        authorizationObject = new AuthorizationObject();
                        authorizationObject.setId(user.getUserId());
                        authorizationObject.setName(user.getLogin());
                        authorizationObject.setPassword(user.getPasswordHash());
                        List<Roles> listRoles = user.getRolesList();
                        if (listRoles.size() == 2) {
                            authorizationObject.setRole(AuthorizationObject.Role.BOTH);
                        } else {
                            authorizationObject.setRole(
                                    AuthorizationObject.Role.valueOf(listRoles.get(0).getRole().toUpperCase()));
                        }
                        return Observable.just(user);
                    } else {
                        return Observable.error(new AuthorizationException("Invalid authorization"));
                    }
                })
                .flatMap(user ->
                        ApiFactory.getStudyService().getRecordBooks(0, RecordBooksRequestEnvelop.generate(user.getUserId()))
                )
                .flatMap(recordBooksResponseEnvelop -> {
                    Return returnObject = recordBooksResponseEnvelop.getReturnContainer().getReturn();

                    List<RecordBook> list = returnObject.getRecordBooksList();
                    if (list != null) {
                        authorizationObject.setRecordBooks(list);
                        return Observable.just(authorizationObject);
                    } else {
                        return Observable.error(new AuthorizationException("Invalid authorization"));
                    }
                })
                .compose(RxUtils.async());
    }
}
