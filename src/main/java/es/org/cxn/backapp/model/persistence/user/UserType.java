package es.org.cxn.backapp.model.persistence.user;

/**
 * kind of member that users can be.
 *
 * @author Santi
 *
 */
public enum UserType {
    /**
     * Socio numerario, cuota de 30, mayor de 18 independiente economicamente.
     */
    SOCIO_NUMERO,
    /**
     * Socio aspirante, menor de 18, sin voto en junta.
     */
    SOCIO_ASPIRANTE,
    /**
     * Socio honorario, cuota de 0, sin voto en junta.
     */
    SOCIO_HONORARIO,
    /**
     * Depende economicamente de socio de numero, cuota 0, sin voto en junta.
     */
    SOCIO_FAMILIAR
}
