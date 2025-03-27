
package es.org.cxn.backapp.service;

import es.org.cxn.backapp.service.exceptions.RecoverPasswordServiceException;

public interface RecoverPasswordService {

    void resetPassword(String token, String newPassword, String dni) throws RecoverPasswordServiceException;

    void sendToken(String email, String userDni) throws RecoverPasswordServiceException;

}
