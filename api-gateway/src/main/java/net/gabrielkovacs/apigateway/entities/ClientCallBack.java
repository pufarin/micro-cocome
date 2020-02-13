package net.gabrielkovacs.apigateway.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ClientCallBack {

    public ClientCallBack() {
    }

    public ClientCallBack(String uuid, String call_back) {
        this.uuid = uuid;
        this.call_back = call_back;
    }

    @Id
    private String uuid;

    private String call_back;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCall_back() {
        return call_back;
    }

    public void setCall_back(String call_back) {
        this.call_back = call_back;
    }
}
