/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chain.reaction;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 */
public class AlertBox {
        public static void display(String title,String Massage){
            Stage Mywindow= new Stage();
            Mywindow.initModality(Modality.APPLICATION_MODAL);
            Mywindow.setTitle(title);
            Mywindow.setMinWidth(250);
            Label l1=new Label();
            l1.setText(Massage);
            Button b1 = new Button("Try Again");
            b1.setOnAction(e -> Mywindow.close());
            
            VBox layout= new VBox(10);
            layout.getChildren().addAll(l1,b1);
            layout.setAlignment(Pos.CENTER);
            
            Scene s=new Scene(layout,250 ,100 , Color.RED);
            
            Mywindow.setScene(s);
            Mywindow.show();
            
            
        }

    
}
