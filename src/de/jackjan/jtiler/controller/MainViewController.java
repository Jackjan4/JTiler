/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jackjan.jtiler.controller;

import de.jackjan.jtiler.Tiler;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.imageio.ImageIO;

/**
 *
 * @author jackjan
 */
public class MainViewController implements Initializable {
    
    @FXML
    private Button btnStart;
    @FXML
    private Button btnBrowse;
    @FXML
    private ListView listPics;
    @FXML
    private Button btnExport;
    @FXML
    private TextField txtExport;
    @FXML
    private Button btnClear;
    @FXML
    private ProgressIndicator progImages;
    
    private Tiler tiler;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        tiler = new Tiler();
        tiler.setOnSucceeded(handler -> onTilingFinished((BufferedImage) handler.getSource().getValue()));
        tiler.setOnFailed(handler -> onTilingFailed(handler.getSource().getMessage()));
        
        progImages.progressProperty().bind(tiler.progressProperty());
    }
    
    @FXML
    public void onBtnStartClick(ActionEvent evt) {
        tiler.setImages(listPics.getItems());
        progImages.setVisible(true);
        tiler.restart();
    }
    
    @FXML
    public void onBtnListClearClick(ActionEvent evt) {
        listPics.getItems().clear();
    }
    
    @FXML
    public void onBtnBrowseClick(ActionEvent evt) {
        FileChooser dia = new FileChooser();
        dia.setTitle("Choose images");
        dia.setSelectedExtensionFilter(new ExtensionFilter("Image files", ".png", ".jpg"));
        List<File> files = dia.showOpenMultipleDialog(btnBrowse.getScene().getWindow());
        
        listPics.getItems().addAll(files);
        
    }
    
    public void onTilingFinished(BufferedImage img) {
        try {
            ImageIO.write(img, "png", new File(txtExport.getText()));
            System.out.println("Tiling successfully finished!");
            progImages.setVisible(false);
        } catch (IOException ex) {
            
        }  
    }
    
    public void onTilingFailed(String msg)
    {
        System.out.println(msg);
    }
    
    @FXML
    public void onBtnExportClick(ActionEvent evt) {
        FileChooser dia = new FileChooser();
        dia.setTitle("Save export image");
        File f = dia.showSaveDialog(btnExport.getScene().getWindow());
        txtExport.setText(f.toString());
    }
    
}
