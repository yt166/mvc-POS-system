package model;

import dto.Customerdto;

import java.sql.SQLException;
import java.util.List;

public interface CustomerModel {
    boolean saveCustomer(Customerdto cdto) throws SQLException, ClassNotFoundException;
    boolean updateCustomer(Customerdto cdto) throws SQLException, ClassNotFoundException;
    boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException;
    List<Customerdto> allCustomers() throws SQLException, ClassNotFoundException;
    Customerdto searchCustomer(String id);
    Customerdto lastCustomer() throws SQLException, ClassNotFoundException;
}
