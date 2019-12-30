#!/bin/bash
nohup java -jar orderProductsAndReceiveOrderedProducts-0.0.1-SNAPSHOT.jar &
nohup java -jar showDeliveryReports-0.0.1-SNAPSHOT.jar &
nohup java -jar showStockReports-0.0.1-SNAPSHOT.jar &

