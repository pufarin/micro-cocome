#!/bin/bash
nohup java -jar /home/ec2-user/deploy/orderProductsAndReceiveOrderedProducts-0.0.1-SNAPSHOT.jar &
nohup java -jar /home/ec2-user/deploy/showDeliveryReports-0.0.1-SNAPSHOT.jar &
nohup java -jar /home/ec2-user/deploy/showStockReports-0.0.1-SNAPSHOT.jar &