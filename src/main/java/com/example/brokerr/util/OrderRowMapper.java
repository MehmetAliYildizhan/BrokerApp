package com.example.brokerr.util;

import com.example.brokerr.model.Customer;
import com.example.brokerr.model.Orders;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import com.example.brokerr.model.Asset;

public class OrderRowMapper implements RowMapper<Orders> {

    @Override
    public Orders mapRow(ResultSet rs, int rowNum) throws SQLException {
        Orders order = new Orders();
        order.setId(rs.getLong("id"));

        // Customer verisini almak için müşteri ID'sini alıp sorgulama yapabilirsiniz
        Long customerId = rs.getLong("customer_id");
        Customer customer = new Customer();
        customer.setId(customerId);
        order.setCustomerId(customer.getId());

        // Asset verisini almak için asset ID'sini alıp sorgulama yapabilirsiniz
        Long assetId = rs.getLong("asset_id");
        Asset asset = new Asset();
        asset.setId(assetId);
        order.setAssetId(asset.getId());

        order.setOrderSide(rs.getString("order_side"));
        order.setSize(rs.getBigDecimal("size"));
        order.setPrice(rs.getBigDecimal("price"));
        order.setStatus(rs.getString("status"));
        order.setCreateDate(rs.getObject("create_date", LocalDateTime.class));

        return order;
    }
}
