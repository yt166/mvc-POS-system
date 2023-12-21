package controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dto.Customerdto;
import dto.Itemdto;
import dto.OrderDetailsdto;
import dto.Orderdto;
import dto.tm.OrderTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.CustomerModel;
import model.ItemModel;
import model.OrderDetailsModel;
import model.OrderModel;
import model.impl.CustomerModelImpl;
import model.impl.ItemModelImpl;
import model.impl.OrderModelImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderController {
    public AnchorPane PlaceOrderPane;
    public JFXButton btnBack;
    public JFXComboBox cmbCustomer;
    public JFXComboBox cmbItem;
    public JFXTextField txtName;
    public JFXTextField txtDes;
    public JFXTextField txtUnitPrize;
    public JFXTextField txtQty;
    public JFXButton btnAddToCart;
    public TreeTableColumn colCode;
    public TreeTableColumn colDes;
    public TreeTableColumn colQty;
    public TreeTableColumn colAmount;
    public TreeTableColumn colOption;
    public JFXButton btnPlaceOrder;
    public Label lblGenID;
    public Label lblSetAmount;
    public JFXTreeTableView<OrderTm> tblOrders;
    private List<Customerdto> customers;
    private List<Itemdto> items;
    private CustomerModel customerModel = new CustomerModelImpl();
    private ItemModel itemModel = new ItemModelImpl();
    private OrderModel orderModel = new OrderModelImpl();

    private double amount = 0.0;
    private  ObservableList<OrderTm> tmlist = FXCollections.observableArrayList();

    public void initialize(){
        colCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("code"));
        colDes.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        colAmount.setCellValueFactory(new TreeItemPropertyValueFactory<>("prize"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));

        generateID();
        loadCustomerID();
        loadItemCode();

        cmbCustomer.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, id) ->{
            for (Customerdto customerdto:customers) {
                if(customerdto.getCusID().equals(id)){
                    txtName.setText(customerdto.getCusName());
                }
            }
        } );

        cmbItem.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, code) ->{
            for(Itemdto itemdto : items){
                if(itemdto.getItemCode().equals(code)){
                    txtDes.setText(itemdto.getDescription());
                    txtUnitPrize.setText(String.format("%.2f",itemdto.getUnitPrize()));
                }
            }
        } );
    }

    private void loadItemCode() {
        try {
            items = itemModel.allItem();
            ObservableList list = FXCollections.observableArrayList();
            for(Itemdto itemdto:items){
                list.add(itemdto.getItemCode());
            }
            cmbItem.setItems(list);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCustomerID() {
        try {
            customers = customerModel.allCustomers();
            ObservableList list =FXCollections.observableArrayList();
            for(Customerdto customerdto : customers){
                list.add(customerdto.getCusID());
            }
            cmbCustomer.setItems(list);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateID() {
        try {
            Orderdto orderdto = orderModel.lastOrder();
            if(orderdto!= null){
                String id= orderdto.getOrderID();
                int num = Integer.parseInt(id.split("[D]")[1]);
                num++;
                lblGenID.setText(String.format("D%03d",num));
            }else{
                lblGenID.setText("D001");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) PlaceOrderPane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Home.fxml"))));
        stage.show();
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        try {
            double prize = itemModel.getItem(cmbItem.getValue().toString()).getUnitPrize()*Integer.parseInt(txtQty.getText());
            JFXButton btn = new JFXButton("Delete");

            OrderTm orderTm = new OrderTm(
                    cmbItem.getValue().toString(),
                    txtDes.getText(),
                    Integer.parseInt(txtQty.getText()),
                    prize,
                    btn
            );

            btn.setOnAction(actionEvent1 -> {
                tmlist.remove(orderTm);
                amount -= orderTm.getPrize();
                tblOrders.refresh();
                lblSetAmount.setText(String.format("%.2f",amount));
            });

            boolean isExist = false;
            for(OrderTm order: tmlist){
                if(order.getCode().equals(orderTm.getCode())){
                    order.setQty(order.getQty()+orderTm.getQty());
                    order.setPrize(order.getPrize()+orderTm.getPrize());
                    isExist = true;
                    amount+=orderTm.getPrize();
                }
            }

            if(!isExist){
                tmlist.add(orderTm);
                amount+=orderTm.getPrize();
            }

            TreeItem<OrderTm> treeItem = new RecursiveTreeItem<>(tmlist, RecursiveTreeObject::getChildren);
            tblOrders.setRoot(treeItem);
            tblOrders.setShowRoot(false);

            txtQty.clear();
            lblSetAmount.setText(String.format("%.2f",amount));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        List<OrderDetailsdto> list = new ArrayList<>();
        for (OrderTm orderTm:tmlist) {
            list.add(new OrderDetailsdto(
                    lblGenID.getText(),
                    orderTm.getCode(),
                    orderTm.getQty(),
                    orderTm.getPrize()/orderTm.getQty()
            ));
        }
        boolean isSaved = false;
        isSaved = orderModel.saveOrder(new Orderdto(
                lblGenID.getText(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd")),
                cmbCustomer.getValue().toString(),
                list
        ));

        if(isSaved){
            new Alert(Alert.AlertType.INFORMATION,"Order Saved").show();
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Something Wents Wrong").show();
        }
    }
}
