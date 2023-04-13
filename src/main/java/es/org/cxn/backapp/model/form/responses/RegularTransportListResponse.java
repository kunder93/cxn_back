package es.org.cxn.backapp.model.form.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.org.cxn.backapp.model.persistence.PersistentRegularTransportEntity;

/**
 * Represents the form used by controller as response for requesting all
 * invoices.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public class RegularTransportListResponse implements Serializable {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -6122005225553512886L;

    /**
     * List with all individual invoices responses.
     */
    private List<RegularTransportResponse> regularTransportList = new ArrayList<>();

    /**
     * Response constructor with parameters provided.
     *
     * @param value the invoices entity list.
     */
    public RegularTransportListResponse(
            List<PersistentRegularTransportEntity> value
    ) {
        super();
        value.forEach(
                (
                        PersistentRegularTransportEntity e
                ) -> this.regularTransportList
                        .add(new RegularTransportResponse(e))
        );
    }

    /**
     *
     */
    public RegularTransportListResponse() {
        super();
    }

    public List<RegularTransportResponse> getRegularTransportList() {
        return new ArrayList<>(regularTransportList);
    }

    public void setRegularTransportList(
            List<RegularTransportResponse> regularTransportList
    ) {
        this.regularTransportList = new ArrayList<>(regularTransportList);
    }

}
