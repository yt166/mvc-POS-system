package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.SQLException;

public class ItemController {
    public void btnBackOnAction(ActionEvent actionEvent) {
    }

    public void btnReportOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design = JRXmlLoader.load("src/main/resources/reports/itemReport.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException | ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
