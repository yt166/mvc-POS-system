package model;

import dto.OrderDetailsdto;

import java.sql.SQLException;
import java.util.List;

public interface OrderDetailsModel {
    boolean saveOrderDetails(List<OrderDetailsdto>list) throws SQLException, ClassNotFoundException;
}
