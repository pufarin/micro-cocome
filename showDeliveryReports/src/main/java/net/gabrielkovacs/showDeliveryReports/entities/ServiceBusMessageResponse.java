package net.gabrielkovacs.showDeliveryReports.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceBusMessageResponse {


    private String correlationId;

    private String document;

    private Timestamp timeStamp;

    private String sender;

    private String eventName;
}
