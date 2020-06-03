package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities;

import java.sql.Timestamp;

public class QueryResponse {

     private String payload;
     private String uuid;
     private Timestamp timeStamp;
     private  String messageType;

     public QueryResponse(String payload, String uuid, Timestamp timeStamp, String messageType) {
          this.payload = payload;
          this.uuid = uuid;
          this.timeStamp = timeStamp;
          this.messageType = messageType;
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

     public void setMessageType(String messageType) {
          this.messageType = messageType;
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
