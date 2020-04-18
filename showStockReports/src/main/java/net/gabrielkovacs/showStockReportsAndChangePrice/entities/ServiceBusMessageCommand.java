package net.gabrielkovacs.showStockReportsAndChangePrice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ServiceBusMessageCommand {

    @Id
    private String correlationId;

    private String command;

    private Timestamp timeStamp;

    private String sender;
}
