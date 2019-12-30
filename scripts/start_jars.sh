#!/bin/bash
java -jar /home/ec2-user/deploy/orderProductsAndReceiveOrderedProducts-0.0.1-SNAPSHOT.jar 
java -jar /home/ec2-user/deploy/showDeliveryReports-0.0.1-SNAPSHOT.jar 
java -jar /home/ec2-user/deploy/showStockReports-0.0.1-SNAPSHOT.jar 
echo "finished working on starting the jars"