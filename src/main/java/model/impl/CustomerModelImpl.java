package model.impl;

import db.DBConnection;
import dto.Customerdto;
import model.CustomerModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModelImpl implements CustomerModel {
    @Override
    public boolean saveCustomer(Customerdto cdto) throws SQLException, ClassNotFoundException {
        String sql="INSERT INTO Customer VALUES(?,?,?,?)";
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);

        preparedStatement.setString(1, cdto.getCusID());
        preparedStatement.setString(2, cdto.getCusName());
        preparedStatement.setString(3, cdto.getCusAddress());
        preparedStatement.setDouble(4,cdto.getCusSalary());

        return preparedStatement.executeUpdate()>0;
    }

    @Override
    public boolean updateCustomer(Customerdto cdto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Customer SET name=?, address=?, salary=? WHERE id=?";
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);

        preparedStatement.setString(1, cdto.getCusName());
        preparedStatement.setString(2, cdto.getCusAddress());
        preparedStatement.setDouble(3,cdto.getCusSalary());
        preparedStatement.setString(4, cdto.getCusID());

        return preparedStatement.executeUpdate()>0 ;
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        String sql ="DELETE FROM Customer WHERE id=?";
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1,id);
        return preparedStatement.executeUpdate()>0;
    }


    @Override
    public List<Customerdto> allCustomers() throws SQLException, ClassNotFoundException {
        List<Customerdto> list = new ArrayList<>() ;
        String sql = "SELECT * FROM customer";
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet set = preparedStatement.executeQuery();
        while (set.next()){
            list.add(new Customerdto(
                    set.getString(1),
                    set.getString(2),
                    set.getString(3),
                    set.getDouble(4)
            ));
        }
        return list;
    }

    @Override
    public Customerdto searchCustomer(String id) {
        return null;
    }

    @Override
    public Customerdto lastCustomer() throws SQLException, ClassNotFoundException {
        String sql ="SELECT * FROM customer ORDER BY id DESC LIMIT 1";
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet set = preparedStatement.executeQuery();

        if(set.next()){
            return new Customerdto(set.getString(1),
                    set.getString(2),
                    set.getString(3),
                    set.getDouble(4));
        }
        return null;
    }
}
