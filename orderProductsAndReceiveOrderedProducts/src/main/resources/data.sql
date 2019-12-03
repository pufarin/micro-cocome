/*
insert into stockitem (id, amount, minstock, maxstock, saleprice, productid, storeid) values (300,4,5,25,35,100,200);
insert into stockitem (id, amount, minstock, maxstock, saleprice, productid, storeid) values (301,30,5,25,20,101,200);
insert into stockitem (id, amount, minstock, maxstock, saleprice, productid, storeid) values (302,60,10,35,21,102,200);
insert into stockitem (id, amount, minstock, maxstock, saleprice, productid, storeid) values (303,15,10,35,18,103,200);
insert into stockitem (id, amount, minstock, maxstock, saleprice, productid, storeid) values (304,15,10,35,18,103,201);


insert into tradingenterprise(id, name) values (901, 'evilCorporation1');
insert into tradingenterprise(id, name) values (902, 'evilCorporation2');
insert into productsuplier (id,name) values (800,'apple_supiler');
insert into productsuplier (id,name) values (801,'orange_supiler');
insert into productsuplier (id,name) values (802,'banana_cabage_supiler');
insert into tradinge_products(trading_enterprise_id, product_supplier_id) values (901, 800);
insert into tradinge_products(trading_enterprise_id, product_supplier_id) values (901, 801);
insert into tradinge_products(trading_enterprise_id, product_supplier_id) values (901, 802);
insert into product (id, barcode, name, purchaseprice, productsuplierid) values (100, 12345, 'apple', 25, 800);
insert into product (id, barcode, name, purchaseprice, productsuplierid) values (101, 12346, 'orange', 15, 801);
insert into product (id, barcode, name, purchaseprice, productsuplierid) values (102, 12347, 'banana', 16, 802);
insert into product (id, barcode, name, purchaseprice, productsuplierid) values (103, 12348, 'cabage', 17, 802);
insert into store (id, location, name, tradingenterpriseid) values (200, 'viena1', 'store1', 901);
insert into store (id, location, name, tradingenterpriseid) values (201, 'viena2', 'store2', 901);


*/

/*  for testing*/

insert into productorder (id, delivery_date, ordering_date, store_id) values (5, '2019-10-06' ,'2019-10-04' , 200);
insert into productorder (id, delivery_date, ordering_date, store_id) values (6, '2019-10-07', '2019-10-04', 200);
insert into productorder (id, delivery_date, ordering_date, store_id) values (7, '2019-10-08', '2019-10-04', 200);
insert into productorder (id, delivery_date, ordering_date, store_id) values (8, '2019-10-06', '2019-10-04', 201);
insert into productorder (id, delivery_date, ordering_date, store_id) values (9, '2019-10-07', '2019-10-04', 201);
insert into productorder (id, delivery_date, ordering_date, store_id) values (10, '2019-10-08', '2019-10-04', 201);
insert into productorder (id, delivery_date, ordering_date, store_id) values (11, '2019-10-09', '2019-10-04', 200);

insert into orderentry (id, amount, product_id, product_order_id) values (106, 10, 100, 5);
insert into orderentry (id, amount, product_id, product_order_id) values (107, 20, 101, 6);
insert into orderentry (id, amount, product_id, product_order_id) values (108, 40, 102, 7);
insert into orderentry (id, amount, product_id, product_order_id) values (109, 99, 100, 8);
insert into orderentry (id, amount, product_id, product_order_id) values (110, 98, 101, 9);
insert into orderentry (id, amount, product_id, product_order_id) values (111, 97, 102, 10);
insert into orderentry (id, amount, product_id, product_order_id) values (112, 100, 103, 11);

