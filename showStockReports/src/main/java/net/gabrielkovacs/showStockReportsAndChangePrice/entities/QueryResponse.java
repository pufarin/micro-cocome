package net.gabrielkovacs.showStockReportsAndChangePrice.entities;

import java.sql.Timestamp;

public class QueryResponse {

     private String payload;
     private String uuid;
     private Timestamp timeStamp;
     private  String messageType = "response";

     public QueryResponse(String payload, String uuid, Timestamp timeStamp) {
          this.payload = payload;
          this.uuid = uuid;
          this.timeStamp = timeStamp;
     }

     public String getPayload() {
          return payload;
     }

     public void setPayload(String payload) {
          this.payload = payload;
     }

     public String getUuid() {
          return uuid;
     }

     public void setUuid(String uuid) {
          this.uuid = uuid;
     }

     public Timestamp getTimeStamp() {
          return timeStamp;
     }

     public void setTimeStamp(Timestamp timeStamp) {
          this.timeStamp = timeStamp;
     }

     public String getMessageType() {
          return messageType;
     }

     @Override
     public String toString() {
          return "QueryResponse{" +
                  "payload='" + payload + '\'' +
                  ", uuid='" + uuid + '\'' +
                  ", timeStamp=" + timeStamp +
                  ", messageType='" + messageType + '\'' +
                  '}';
     }
}
