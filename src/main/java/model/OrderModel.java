package model;

import dto.Orderdto;

import java.sql.SQLException;

public interface OrderModel {
    boolean saveOrder(Orderdto orderdto);
    Orderdto lastOrder() throws SQLException, ClassNotFoundException;
}
