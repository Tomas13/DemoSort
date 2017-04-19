package kazpost.kz.mobterminal.data.network.model.findplan;

/**
 * Created by root on 4/19/17.
 */

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

@Root(name = "sch:FindPlanRequest", strict = true)
@Order(attributes =  {"session"})
public class FindPlanData {


    @Element(name = "sch:SessionId", required = true)
    @Attribute(name = "session")
    private String sessionId;

    @Element(name = "sch:ParcelBarcode", required = true)
    private String parcelBarcode;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getParcelBarcode() {
        return parcelBarcode;
    }

    public void setParcelBarcode(String parcelBarcode) {
        this.parcelBarcode = parcelBarcode;
    }
}
