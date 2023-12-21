package model.impl;

import db.DBConnection;
import dto.Orderdto;
import model.OrderDetailsModel;
import model.OrderModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderModelImpl implements OrderModel {
    OrderDetailsModel orderDetailsModel = new OrderDetailsModelImpl();
    @Override
    public boolean saveOrder(Orderdto orderdto) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            String sql ="INSERT INTO orders VALUE (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,orderdto.getOrderID());
            preparedStatement.setString(2,orderdto.getDate());
            preparedStatement.setString(3,orderdto.getCusID());

            if(preparedStatement.executeUpdate()>0){
                boolean saved = orderDetailsModel.saveOrderDetails(orderdto.getList());
                if(saved){
                    connection.commit();
                    return true;
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public Orderdto lastOrder() throws SQLException, ClassNotFoundException {
        String sql ="SELECT*FROM orders ORDER BY id DESC LIMIT 1";
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet set = preparedStatement.executeQuery();

        if(set.next()){
            return new Orderdto(
                set.getString(1),set.getString(2),set.getString(3),null
            );
        }
        return null;
    }
}
