package controller;

import db.conexion;
import function.navigation;
import function.time;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import model.laporanUangMasukBulananTable;
import model.uangMasukModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class LaporanUangMasukBulananController implements Initializable {
    
    ObservableList<String> comboJenis = FXCollections.observableArrayList("Todo","La cesta","Hotel");  
    ObservableList<String> comboBulan = FXCollections.observableArrayList("January","February"
            ,"March","April","May","June","July","August","September","October","November","December");
    
    @FXML
    private TableView<laporanUangMasukBulananTable> tableUangMasuk;
    
    @FXML
    private TableColumn<laporanUangMasukBulananTable, String> columnNo;
    
    @FXML
    private TableColumn<laporanUangMasukBulananTable, String> columnTanggal;
    
    @FXML
    private TableColumn<laporanUangMasukBulananTable, String> columnBakul;
    @FXML
    private TableColumn<laporanUangMasukBulananTable, String> columnHotel;
    @FXML
    private TableColumn<laporanUangMasukBulananTable, String> columnUang;
    
    private ObservableList<laporanUangMasukBulananTable> data;
    
    @FXML
    private ComboBox bulan;
    
    @FXML
    private TextField tahun;
    
    @FXML
    private Label total_uang_masuk, total_uang_masuk_bakul, total_uang_masuk_hotel;
    
    @FXML
    private Button btnOutput;
    
    @FXML
    private ContextMenu contextOutput;
    
    conexion kon = new conexion();
    uangMasukModel model = new uangMasukModel();
    NumberFormat num = NumberFormat.getInstance();
    navigation nav = new navigation();
    time time = new time();
        
    private void setBulan(){
        bulan.setValue(time.tanggalBulan());
        bulan.setItems(comboBulan);
    }
    
    private void setTahun(){
        tahun.setText(time.tanggalTahun());
    }
    
    private void setStyleTable(){
        columnNo.setStyle("-fx-alignment: CENTER");
        columnBakul.setStyle("-fx-alignment: CENTER");
        columnHotel.setStyle("-fx-alignment: CENTER");
        columnUang.setStyle("-fx-alignment: CENTER");
        columnTanggal.setStyle("-fx-alignment: CENTER");
    }
    
    private void loadTable(){
        try {
            nav.animationFade(tableUangMasuk);
            model.queryLaporanBulanan(bulan.getSelectionModel().getSelectedItem().toString(), tahun.getText());
            int no=0;
            data=FXCollections.observableArrayList();
            kon.res=kon.stat.executeQuery(model.queryLoad);
            while(kon.res.next()){
                no++;
                String uangBakulFormat=num.format(Integer.parseInt(kon.res.getString(1).toString()));
                String uangHotelFormat=num.format(Integer.parseInt(kon.res.getString(2).toString()));
                String uangFormat=num.format(Integer.parseInt(kon.res.getString(3).toString()));
                data.add(new laporanUangMasukBulananTable(String.valueOf(no),
                        kon.res.getString(4), uangBakulFormat, uangHotelFormat, uangFormat ));
            }
            columnNo.setCellValueFactory(new PropertyValueFactory<>("no"));
            columnTanggal.setCellValueFactory(new PropertyValueFactory<>("fecha"));
            columnBakul.setCellValueFactory(new PropertyValueFactory<>("dinero"));
            columnHotel.setCellValueFactory(new PropertyValueFactory<>("dinero del hotel"));
            columnUang.setCellValueFactory(new PropertyValueFactory<>("dinero"));
            tableUangMasuk.setItems(null);
            tableUangMasuk.setItems(data);
            model.queryLaporanBulananTotal(bulan.getSelectionModel().getSelectedItem().toString(), tahun.getText());
            kon.res=kon.stat.executeQuery(model.queryLoad);
            while(kon.res.next()){
                if(kon.res.getString(1)==null){
                    total_uang_masuk_bakul.setText("-");
                }
                else{
                    total_uang_masuk_bakul.setText(num.format(Integer.parseInt(kon.res.getString(1).toString())));
                }
                if(kon.res.getString(2)==null){
                    total_uang_masuk_hotel.setText("-");
                }
                else{
                    total_uang_masuk_hotel.setText(num.format(Integer.parseInt(kon.res.getString(2).toString())));
                }
                
                if(kon.res.getString(3)==null){
                    total_uang_masuk.setText("-");
                }
                else{
                    total_uang_masuk.setText(num.format(Integer.parseInt(kon.res.getString(3).toString())));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }    
    
    @FXML
    private void printClicked(ActionEvent event){
        Properties properties = new Properties();
        JasperReport jasRep;
        JasperPrint jasPri;
        JasperDesign jasDes;
        
        try {
            properties.load(new FileInputStream("setting.properties"));
            String driverName = properties.getProperty("driverName");
            try {
                Class.forName(driverName);
                File report = new File("src/report/ReportUangMasukBulanan.jrxml");
                jasDes = JRXmlLoader.load(report);
                HashMap parameter = new HashMap();
                parameter.put("bulan_tahun", bulan.getSelectionModel().getSelectedItem().toString()+"/"+tahun.getText());
                parameter.put("bulan_tahun_text", bulan.getSelectionModel().getSelectedItem().toString()+" "+tahun.getText());
                parameter.put("uang_bakul", total_uang_masuk_bakul.getText());
                parameter.put("uang_hotel", total_uang_masuk_hotel.getText());
                parameter.put("total", total_uang_masuk.getText());
                jasRep = JasperCompileManager.compileReport(jasDes);
                jasPri = JasperFillManager.fillReport(jasRep, parameter, kon.con);
                JasperViewer.viewReport(jasPri, false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } catch (Exception e) {
        }
    }
    
    @FXML
    private void openContext(MouseEvent event){
        contextOutput.show(btnOutput,event.getScreenX(), event.getScreenY());
    }
    
    @FXML
    private void saveXLSX(ActionEvent event) throws SQLException, FileNotFoundException, IOException{
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(bulan.getSelectionModel().getSelectedItem().toString()+" "+tahun.getText());
        XSSFRow header = sheet.createRow(1);
        XSSFCell cell = header.createCell(1);
        XSSFRow title = sheet.createRow(0);
        XSSFCell cellTitle = header.createCell(0);
        XSSFCellStyle cs = wb.createCellStyle();
        XSSFCellStyle cs2 = wb.createCellStyle();
        XSSFCellStyle cs3 = wb.createCellStyle();
        XSSFCellStyle csTitle = wb.createCellStyle();
        XSSFFont f = wb.createFont();
        XSSFFont f2 = wb.createFont();
        XSSFFont fTitle = wb.createFont();
        f.setBold(true);
        fTitle.setBold(true);
        f.setFontHeightInPoints((short) 12);
        f2.setFontHeightInPoints((short) 12);
        fTitle.setFontHeightInPoints((short) 12);
        cs.setAlignment(HorizontalAlignment.CENTER);
        cs2.setAlignment(HorizontalAlignment.CENTER);
        cs3.setAlignment(HorizontalAlignment.LEFT);
        csTitle.setAlignment(HorizontalAlignment.LEFT);
        cs.setFont(f);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setBorderBottom(BorderStyle.THIN);
        cs2.setFont(f2);
        cs2.setBorderRight(BorderStyle.THIN);
        cs2.setBorderLeft(BorderStyle.THIN);
        cs2.setBorderTop(BorderStyle.THIN);
        cs2.setBorderBottom(BorderStyle.THIN);
        cs3.setBorderRight(BorderStyle.THIN);
        cs3.setBorderLeft(BorderStyle.THIN);
        cs3.setBorderTop(BorderStyle.THIN);
        cs3.setBorderBottom(BorderStyle.THIN);
        csTitle.setFont(fTitle);
        cellTitle = title.createCell(1);
        cellTitle.setCellStyle(csTitle);
        cellTitle.setCellValue("Informe mensual de entrada de dinero "+bulan.getSelectionModel().getSelectedItem().toString()+" "+tahun.getText());
        cell = header.createCell(0);
        cell.setCellStyle(cs);
        cell.setCellValue("No");
        cell = header.createCell(1);
        cell.setCellStyle(cs);
        cell.setCellValue("Fecha");
        cell = header.createCell(2);
        cell.setCellStyle(cs);
        cell.setCellValue("Dinero entrando en Bakul");
        cell = header.createCell(3);
        cell.setCellStyle(cs);
        cell.setCellValue("Dinero que ingresa a los hoteles");
        cell = header.createCell(4);
        cell.setCellStyle(cs);
        cell.setCellValue("Cuota de entrada total");
        
        sheet.autoSizeColumn(0);
        sheet.setColumnWidth(1, 4320);
        sheet.setColumnWidth(2, 5760);
        sheet.setColumnWidth(3, 5760);
        sheet.setColumnWidth(4, 5760);
        
        model.queryLaporanBulanan(bulan.getSelectionModel().getSelectedItem().toString(), tahun.getText());
        int no=1;
        int index=0;
        kon.res=kon.stat.executeQuery(model.queryLoad);
        while(kon.res.next()){
            no++;
            index++;
            String uangBakulFormat=num.format(Integer.parseInt(kon.res.getString(1).toString()));
            String uangHotelFormat=num.format(Integer.parseInt(kon.res.getString(2).toString()));
            String uangFormat=num.format(Integer.parseInt(kon.res.getString(3).toString()));
            XSSFRow row = sheet.createRow(no);
            row.createCell(0).setCellValue(no);
            cell=row.createCell(0);
            cell.setCellStyle(cs2);
            cell.setCellValue(index);
            cell=row.createCell(1);
            cell.setCellStyle(cs2);
            cell.setCellValue(kon.res.getString(4));
            cell=row.createCell(2);
            cell.setCellStyle(cs2);
            cell.setCellValue(uangBakulFormat);
            cell=row.createCell(3);
            cell.setCellStyle(cs2);
            cell.setCellValue(uangHotelFormat);
            cell=row.createCell(4);
            cell.setCellStyle(cs2);
            cell.setCellValue(uangFormat);
        }
        model.queryLaporanBulananTotal(bulan.getSelectionModel().getSelectedItem().toString(), tahun.getText());
        kon.res=kon.stat.executeQuery(model.queryLoad);
        while(kon.res.next()){
            no++;
            XSSFRow row = sheet.createRow(no);
            cell=row.createCell(1);
            cell.setCellStyle(cs);
            cell.setCellValue("Total");
            if(kon.res.getString(1)==null){
                cell=row.createCell(2);
                cell.setCellStyle(cs);
                cell.setCellValue("0");
            }
            else{
                cell=row.createCell(2);
                cell.setCellStyle(cs);
                cell.setCellValue(num.format(Integer.parseInt(kon.res.getString(1))));
            }
                
            if(kon.res.getString(2)==null){
                cell=row.createCell(3);
                cell.setCellStyle(cs);
                cell.setCellValue("0");
            }
            else{
                cell=row.createCell(3);
                cell.setCellStyle(cs);
                cell.setCellValue(num.format(Integer.parseInt(kon.res.getString(2))));
            }
            
            if(kon.res.getString(3)==null){
                cell=row.createCell(4);
                cell.setCellStyle(cs);
                cell.setCellValue("0");
            }
            else{
                cell=row.createCell(4);
                cell.setCellStyle(cs);
                cell.setCellValue(num.format(Integer.parseInt(kon.res.getString(3))));
            }
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save to Excel");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Microsoft Office Excel 2010", "*.xlsx"));
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            TrayNotification tray = new TrayNotification();
            FileOutputStream fileout = new FileOutputStream(selectedFile.getAbsoluteFile());
            wb.write(fileout);
            fileout.close();
            tray.setNotificationType(NotificationType.CUSTOM);
            tray.setTitle("Save Success");
            tray.setMessage("Archivo guardado correctamente...");
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.millis(1000));
            tray.setRectangleFill(Color.valueOf("#4183D7"));
            tray.setImage(new Image("/img/icons8_Ok_96px.png"));
        }
        
    }
        
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setBulan();
        setTahun();
        setStyleTable();
        kon.db();
        num.setMaximumFractionDigits(3);
        loadTable();
    }    
    
    @FXML
    private void refreshClicked(){
        loadTable();
    }
    
}
