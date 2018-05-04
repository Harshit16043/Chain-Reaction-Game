/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chain.reaction;

/**
 *
 * 
 */


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import javafx.animation.PathTransition;
import javafx.scene.shape.Circle;



public class ChainReaction extends Application implements EventHandler<ActionEvent>{
    
	
	
    static Stage window;
    static Scene home,player,currgame,winner;
    static Button Back,Start1,Start2,Resume,Setting,Save,UNDO;
    static String[] colors = {"RED", "BLUE", "GREEN", "YELLOW", "MAGENTA", "BLACK", "WHITE","AQUA"};
    static Color strockcolor=Color.BROWN;
    static Grid grid;
    
    static Color[] C={Color.RED,Color.BLUE,Color.GREEN,Color.YELLOW,Color.MAGENTA,Color.BLACK,Color.WHITE,Color.AQUA};
    static playercolor playerc;
    static int current_player=0,n,t=0,fg=0,f=0;
    static List<Integer> a;
    static Rectangle rect;
    static savecells game;
    
    
    public static void sserialize(savecells g) throws IOException{
        ObjectOutputStream out =null;
	try{
            
            out =new ObjectOutputStream(new FileOutputStream("game.txt"));
            out.writeObject(g);
        }
	finally{
            out.close();
	}
    }
	
    
    public static savecells sdeserialize() throws IOException,ClassNotFoundException{
        ObjectInputStream in=null;
        try{
            in=new ObjectInputStream(new FileInputStream("game.txt"));
            savecells p= (savecells) in.readObject();
            return p;
        }
        catch (Exception e) {
            // TODO: handle exception
            return null;
        }
        finally {
            in.close();
        }
    }
	
	
    
    
    
    
    public static void cserialize(playercolor p) throws IOException{
        ObjectOutputStream out =null;	
        try{
            out =new ObjectOutputStream(new FileOutputStream("color.txt"));
            out.writeObject(p);
        }	
	finally{
            out.close();
	}
    }

	
    public static playercolor cdeserialize() throws IOException,ClassNotFoundException{
	ObjectInputStream in=null;	
	try{
            in=new ObjectInputStream(new FileInputStream("color.txt"));
            playercolor p= (playercolor) in.readObject();
            return p;
	}
	finally {
            in.close();
        }
    }


    static EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
    	@Override
        public void handle(MouseEvent mouseEvent) {
            game.setA(a);
            game.t=t;
            if(fg==1)
                game.fg=1;
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();
            if(grid.type==0){
                for(int i=0;i<6;i++){
                    for(int j=0;j<9;j++){
                        if((grid.cells.get(i).get(j).checkX((int)x))&&((grid.cells.get(i).get(j).checkY((int)y)))){
                            game.player=current_player;
                            for(int su=0;su<game.getX();su++){
                                for (int ha = 0; ha < game.getY(); ha++) {
                                    game.c[su][ha].copy(grid.cells.get(su).get(ha));
                                }
                            }
                            try {
                                sserialize(game);
                            } catch (IOException ex) {
                                Logger.getLogger(ChainReaction.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            grid.cells.get(i).get(j).add(i, j,12,C[playerc.color[current_player]]);
                            if(f==1){
                                f=0;
                            }
                            else{
                                t++;
                                if(t==a.size()){
                                    t=0;
                                    fg=1;
                                }
                                current_player=a.get(t);
                            }
                            break;
                        }
                    }
                }
            }
            else if(grid.type==1){
                for(int i=0;i<10;i++){
                    for(int j=0;j<15;j++){
                        if((grid.cells.get(i).get(j).checkX((int)x))&&(grid.cells.get(i).get(j).checkY((int) y))){
                            for(int su=0;su<game.getX();su++){
                                for (int ha = 0; ha < game.getY(); ha++) {
                                    game.c[su][ha].copy(grid.cells.get(su).get(ha));
                                }
                            }
                            try {
                                sserialize(game);
                            } catch (IOException ex) {
                                Logger.getLogger(ChainReaction.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            grid.cells.get(i).get(j).add(i, j,10,C[playerc.color[current_player]]);
                            if(f==1){
                                f=0;
                            }
                            else{
                                t++;
                                if(t==a.size()){
                                    t=0;
                                    fg=1;
                                }
                                current_player=a.get(t);
                            }
                            break;
                        }
                    }
                }
            }
            if((fg==1)&&(!count(C[playerc.color[current_player]]))){
                while(!count(C[playerc.color[current_player]])){
                    a.remove(t);
                    if(t==a.size())
                        t=0;
                    current_player=a.get(t);
                }
            }
            if(a.size()==1){
                game=null;
                try {
                    sserialize(game);
                } catch (IOException ex) {
                    Logger.getLogger(ChainReaction.class.getName()).log(Level.SEVERE, null, ex);
                }
                Button b = new Button("HOME");
                b.setLayoutX(100);
                b.setLayoutY(125);
                b.setPrefWidth(100);
                b.setPrefHeight(25);
                b.setOnAction(e -> window.setScene(home));
                Group root=new Group();
                Label l = new Label("Player "+(a.get(0)+1)+" Wins");
                l.setLayoutX(100);
                l.setLayoutY(50);
                l.setPrefWidth(100);
                l.setPrefHeight(50);
                root.getChildren().add(l);
                root.getChildren().add(b);
                winner=new Scene(root, 300, 300, Color.YELLOW);
                window.setScene(winner);
            }
            rect.setFill(C[playerc.color[current_player]]);
        }
        
    };
    
    
    
    public static void save(playercolor p) throws IOException{
	int flag=0;
            for(int i=0;i<8;i++){
		for(int j=i+1;j<8;j++){
                    if(p.color[i]==p.color[j])
			flag=1;
                }
            }
	if(flag==0){
            cserialize(p);
            window.setScene(home);
	}
        else{
            AlertBox.display("CAN'T SAVE","Choose unique color for everyone");
        }
    }
	

	
    public static int findcolor(Color x){
        int i;
        
	for(i=0;i<8;i++){
            
            if(x==C[i]){
		return i;
            }
	}
	return 0;
    }
	
	
    public static int find(String x){
	int i;
	for(i=0;i<8;i++){
            if(x.equals(colors[i])){
		return i;
            }
	}
	return i;
    }
    
    
    public static boolean count(Color c){
        int x=0;
        if(grid.type==0){
            for(int i=0;i<6;i++){
                for(int j=0;j<9;j++){
                    if(grid.cells.get(i).get(j).color==c){
                        x++;
                        
                    }
                }
            }
        }
        if(grid.type==1){
            for(int i=0;i<10;i++){
                for(int j=0;j<15;j++){
                    if(grid.cells.get(i).get(j).color==c)
                        x++;
                }
            }
        }
        if(x>0)
            return true;
        return false;
    }
    
    
    
    public static void play(int x,int p){
        window.setScene(currgame);	
	n=p;
        fg=0;
        current_player=0;
	a = new ArrayList<>();
        for(int i=0;i<n;i++)
            a.add(i);   
    }   
    
	
	
	
	
    public static void main(String[] args) throws IOException{
    	launch(args);
    }

    
    
    public static void undo() {
        try {
            game=sdeserialize();
            t--;
            fg=game.fg;
            if(t==-1){
                t=a.size()-1;
            }
            current_player=a.get(t);
        
            int r=0,size=0;
            if(game.x==6){
                r=12;
                size=50;
            }
            else{
                r=10;
                size=35;
            }
            for(int i=0;i<game.getX();i++){
                for (int j = 0; j < game.getY(); j++) {
                    grid.cells.get(i).get(j).Spheres.getChildren().clear();
                }
            }
            for(int i=0;i<game.getX();i++){
                for (int j = 0; j < game.getY(); j++) {
                    grid.cells.get(i).get(j).copy(game.c[i][j],i,j,r);
                }
            }
            rect.setFill(C[playerc.color[current_player]]);
        } catch (Exception ex) {
            AlertBox.display("NOT AVAILIBLE","Data is not Availible");
        }

    }
        
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
	// Window Initialize
	window=primaryStage;
	window.setTitle("Chain Reaction");
	Group root3;
        
        
	//Initializing Buttons
	Start1 = new Button("9X6");
	Start2 = new Button("15X10");
	Resume = new Button("RESUME");
	Setting = new Button("PLAYER SETTING");
        
        
	//Making Home Screen
	ChoiceBox<Integer> no_of_player= new ChoiceBox<>();
	no_of_player.getItems().addAll(2,3,4,5,6,7,8);
	no_of_player.setValue(2);
	no_of_player.setPrefWidth(325);
	no_of_player.setLayoutX(25);
	no_of_player.setLayoutY(25);
        
        
	Start1.setPrefHeight(25);
	Start1.setPrefWidth(150);
	Start1.setLayoutX(25);
	Start1.setLayoutY(75);
	Start1.setOnAction(e -> {
            try {
                fg=0;
                game= new savecells(6, 9);
                playerc=cdeserialize();
                grid=new Grid();
                currgame = new Scene(grid.root,350,550);
                currgame.setOnMouseClicked(mouseHandler);
                currgame.setFill(Color.CYAN);
                play(1,no_of_player.getValue());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                playerc=new playercolor();
                try {
                    cserialize(playerc);
                } catch (IOException ex) {
                    System.out.println("can't Save");
                }
            }
	});
        
        
	Start2.setPrefHeight(25);
	Start2.setPrefWidth(150);
	Start2.setLayoutX(200);
	Start2.setLayoutY(75);
	Start2.setOnAction(e -> {
            try {
                fg=0;
                game=new savecells(10, 15);
                playerc=cdeserialize();
		grid=new Grid(2);
                currgame = new Scene(grid.root,400,590);
                currgame.setOnMouseClicked(mouseHandler);
                currgame.setFill(Color.CYAN);
                play(2,no_of_player.getValue());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                playerc=new playercolor();
                try {
                    cserialize(playerc);
                } catch (IOException ex) {
                    System.out.println("can't Save");
                }
            }
        });
        
        
	Resume.setPrefHeight(25);
	Resume.setPrefWidth(325);
	Resume.setLayoutX(25);
	Resume.setLayoutY(125);
	Resume.setOnAction(this);
        
        
	Setting.setPrefHeight(25);
	Setting.setPrefWidth(150);
	Setting.setLayoutX(100);
	Setting.setLayoutY(350);
	Setting.setOnAction(this);
        
        
	root3 = new Group();
	root3.getChildren().addAll(Setting,Start1,Start2,Resume,no_of_player);
	home=new Scene(root3,400,400);
	home.setFill(Color.AQUAMARINE);
        
        
        
	//Display Start
	window.setScene(home);
	window.show();
    }

    
    @Override
    public void handle(ActionEvent event) {
	// TODO Auto-generated method stub
        
        if(event.getSource()==Resume){
            try {
                game=sdeserialize();
                playerc=cdeserialize();
                t=game.t;
                a=game.A;
                current_player=game.player;
                fg=game.fg;
                int r=0,size=0;
                if(game.x==6){
                    r=12;
                    size=50;
                    grid=new Grid();
                }
                else{
                    r=10;
                    size=35;
                    grid=new Grid(1);
                }
                for(int i=0;i<game.getX();i++){
                    for (int j = 0; j < game.getY(); j++) {
                        grid.cells.get(i).get(j).copy(game.c[i][j],i,j,r);
                    }
                }
                rect.setFill(C[playerc.color[current_player]]);
                if(game.x==6){
                    currgame = new Scene(grid.root,350,550);
                    currgame.setOnMouseClicked(mouseHandler);
                    currgame.setFill(Color.CYAN);
                }
                else{
                    currgame = new Scene(grid.root,400,590);
                    currgame.setOnMouseClicked(mouseHandler);
                    currgame.setFill(Color.CYAN);
                }
                window.setScene(currgame);
            } catch (Exception ex) {
                AlertBox.display("NOT AVAILIBLE","Data is not Availible");
            }
            
	}
	if(event.getSource()==Setting){
            try {
		playercolor p=cdeserialize();
		Group root = new Group();
		ChoiceBox<String> p1= new ChoiceBox<>();
		ChoiceBox<String> p2= new ChoiceBox<>();
		ChoiceBox<String> p3= new ChoiceBox<>();
		ChoiceBox<String> p4= new ChoiceBox<>();
		ChoiceBox<String> p5= new ChoiceBox<>();
		ChoiceBox<String> p6= new ChoiceBox<>();
		ChoiceBox<String> p7= new ChoiceBox<>();
		ChoiceBox<String> p8= new ChoiceBox<>();
		
		Save=new Button("SAVE");
		Save.setPrefHeight(25);
		Save.setPrefWidth(225);
		Save.setLayoutX(25);
		Save.setLayoutY(425);
		Save.setOnAction(e -> {
                    try {
			p.color[0]=find(p1.getValue());
                        p.color[1]=find(p2.getValue());
			p.color[2]=find(p3.getValue());
			p.color[3]=find(p4.getValue());
			p.color[4]=find(p5.getValue());
			p.color[5]=find(p6.getValue());
			p.color[6]=find(p7.getValue());
			p.color[7]=find(p8.getValue());
			save(p);
			} catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
			}
		});
		
		Label l1 = new Label("Player 1");
		Label l2 = new Label("Player 2");
		Label l3 = new Label("Player 3");
		Label l4 = new Label("Player 4");
		Label l5 = new Label("Player 5");
		Label l6 = new Label("Player 6");
		Label l7 = new Label("Player 7");
		Label l8 = new Label("Player 8");
		
		l1.setPrefHeight(25);
		l2.setPrefHeight(25);
		l3.setPrefHeight(25);
		l4.setPrefHeight(25);
		l5.setPrefHeight(25);
		l6.setPrefHeight(25);
		l7.setPrefHeight(25);
		l8.setPrefHeight(25);
		
		l1.setPrefWidth(100);
		l2.setPrefWidth(100);
		l3.setPrefWidth(100);
		l4.setPrefWidth(100);
		l5.setPrefWidth(100);
		l6.setPrefWidth(100);
		l7.setPrefWidth(100);
		l8.setPrefWidth(100);
		
		l1.setLayoutX(25);
		l2.setLayoutX(25);
		l3.setLayoutX(25);
		l4.setLayoutX(25);
		l5.setLayoutX(25);
		l6.setLayoutX(25);
		l7.setLayoutX(25);
		l8.setLayoutX(25);
		
		l1.setLayoutY(25);
		l2.setLayoutY(75);
		l3.setLayoutY(125);
		l4.setLayoutY(175);
		l5.setLayoutY(225);
		l6.setLayoutY(275);
		l7.setLayoutY(325);
		l8.setLayoutY(375);
		
		p1.getItems().addAll(colors);
		p2.getItems().addAll(colors);
		p3.getItems().addAll(colors);
		p4.getItems().addAll(colors);
		p5.getItems().addAll(colors);
		p6.getItems().addAll(colors);
		p7.getItems().addAll(colors);
		p8.getItems().addAll(colors);
		
		p1.setPrefHeight(25);
		p2.setPrefHeight(25);
		p3.setPrefHeight(25);
		p4.setPrefHeight(25);
		p5.setPrefHeight(25);
		p6.setPrefHeight(25);
		p7.setPrefHeight(25);
		p8.setPrefHeight(25);
		
		p1.setPrefWidth(100);
		p2.setPrefWidth(100);
		p3.setPrefWidth(100);
		p4.setPrefWidth(100);
		p5.setPrefWidth(100);
		p6.setPrefWidth(100);
		p7.setPrefWidth(100);
		p8.setPrefWidth(100);
		
		p1.setLayoutX(150);
		p2.setLayoutX(150);
		p3.setLayoutX(150);
		p4.setLayoutX(150);
		p5.setLayoutX(150);
		p6.setLayoutX(150);
		p7.setLayoutX(150);
		p8.setLayoutX(150);
		
		p1.setLayoutY(25);
		p2.setLayoutY(75);
		p3.setLayoutY(125);
		p4.setLayoutY(175);
		p5.setLayoutY(225);
		p6.setLayoutY(275);
		p7.setLayoutY(325);
		p8.setLayoutY(375);
		
		p1.setValue(colors[p.color[0]]);
		p2.setValue(colors[p.color[1]]);
		p3.setValue(colors[p.color[2]]);
		p4.setValue(colors[p.color[3]]);
		p5.setValue(colors[p.color[4]]);
		p6.setValue(colors[p.color[5]]);
		p7.setValue(colors[p.color[6]]);
		p8.setValue(colors[p.color[7]]);
		
		root.getChildren().addAll(l1,l2,l3,l4,l5,l6,l7,l8,p1,p2,p3,p4,p5,p6,p7,p8,Save);
		player=new Scene(root,300,500);
		player.setFill(Color.AQUA);
		window.setScene(player);
		
		
		
            } catch (Exception e) {
		// TODO Auto-generated catch block
		playercolor p = new playercolor();
                try {
                    cserialize(p);
		} catch (Exception e1) {
                    // TODO Auto-generated catch block
                    System.out.println("Can't save");
		}
            }
        }
    }
    
    
    
    @SuppressWarnings("serial")
    static class Grid implements Serializable{
	Group root;
	List<List<Cell>> cells;
	int type;
        
	public Grid() {
            // Making 9 X 6 Grid
            root=new Group();
            type=0;
            
            Back = new Button("BACK");
            Back.setPrefHeight(25);
            Back.setPrefWidth(100);
            Back.setLayoutX(225);
            Back.setLayoutY(500);
            Back.setOnAction(e -> window.setScene(home));
            
            UNDO = new Button("UNDO");
            UNDO.setPrefHeight(25);
            UNDO.setPrefWidth(100);
            UNDO.setLayoutX(25);
            UNDO.setLayoutY(500);
            UNDO.setOnAction(e -> undo());
            
            rect=new Rectangle(150,500,50,25);
            rect.setStroke(Color.BLACK);
            rect.setFill(C[playerc.color[0]]);
            
            cells =new ArrayList<>();
            for(int i=0;i<6;i++){
                List<Cell> A = new ArrayList<>();
                for(int j=0;j<9;j++){
                    Cell b=new Cell(i*50+25,j*50+25,50,50);
                    b.cell.setFill(Color.LIGHTGRAY);
                    b.cell.setStroke(strockcolor);
                    b.cell.setStrokeWidth(1);
                    b.setX(i*50+25, i*50+75);
                    b.setY(j*50+25, j*50+75);
                    
                    
                    if(((i==0)&&(j==0))||((i==5)&&(j==0))||((i==0)&&(j==8))||((i==5)&&(j==8))){
                        b.setMass(2);
                        
                    }
                    else if((i==0)||(i==5)||(j==0)||(j==8)){
                        b.setMass(3);
                        
                    }
                    else{
                        b.setMass(4);
                    }
                    A.add(b);
                }
                cells.add(A);
            }
            for(int i=0;i<6;i++){
                for(int j=0;j<9;j++){
                    root.getChildren().add(cells.get(i).get(j).cell);
                }
            }
            root.getChildren().add(Back);
            root.getChildren().add(UNDO);
            root.getChildren().add(rect);
            
            
        }
        Grid(int x){
            //Making 15 X 10 Grid
            root=new Group();
            
            type=1;
            
            cells =new ArrayList<>();
            
            Back = new Button("BACK");
            Back.setPrefHeight(25);
            Back.setPrefWidth(100);
            Back.setLayoutX(275);
            Back.setLayoutY(555);
            Back.setOnAction(e -> window.setScene(home));
            
            UNDO = new Button("UNDO");
            UNDO.setPrefHeight(25);
            UNDO.setPrefWidth(100);
            UNDO.setLayoutX(25);
            UNDO.setLayoutY(555);
            UNDO.setOnAction(e -> undo());
            
            rect=new Rectangle(150,555,100,25);
            rect.setStroke(Color.BLACK);
            rect.setFill(C[playerc.color[0]]);
            
            for(int i=0;i<10;i++){
                List<Cell> A = new ArrayList<>();
		for(int j=0;j<15;j++){
                    Cell b=new Cell(i*35+25,j*35+15,35,35);
                    b.cell.setFill(Color.LIGHTGRAY);
                    b.cell.setStroke(strockcolor);
                    b.cell.setStrokeWidth(0.5);
                    b.setX(i*35+25, i*35+60);
                    b.setY(j*35+15, j*35+40);
                    if(((i==0)&&(j==0))||((i==9)&&(j==0))||((i==0)&&(j==14))||((i==9)&&(j==14))){
                        b.setMass(2);
                    }
                    else if((i==0)||(i==9)||(j==0)||(j==14)){
			b.setMass(3);
                    }
                    else{
			b.setMass(4);
                    }
                    A.add(b);
		}
		cells.add(A);
            }
            
            
            for(int i=0;i<10;i++){
		for(int j=0;j<15;j++){
                    root.getChildren().add(cells.get(i).get(j).cell);
		}
            }
            
            root.getChildren().add(Back);
            root.getChildren().add(UNDO);
            root.getChildren().add(rect);
            
            
        }
    }

	
	
    static class Cell{
	int xs,xe,ys,ye;
	Rectangle cell;
	int critical_mass;
	int current_mass;
        Color color;
        Group Spheres=new Group();
	public Cell(int a,int b,int c,int d) {
		// TODO Auto-generated constructor stub
		cell=new Rectangle(a,b,c,d);
		current_mass=0;
	}
	public int getCMass(){
		return current_mass;
	}
	public void setMass(int a){
		critical_mass=a;
	}
	public int getMMass(){
		return critical_mass;
	}
	public void setX(int a,int b){
		xs=a;
		xe=b;
	}
	public void setY(int a,int b){
		ys=a;
		ye=b;
	}
	public boolean checkX(int a){
		return ((xs<a)&&(a<xe));
	}
	public boolean checkY(int a){
		return ((ys<a)&&(a<ye));
	}
	public int getX1(){
		return (xs+xe)/2;
	}
	public int getY1(){
		return (ys+ye)/2;
	}
        public void add(int x,int y,int radius,Color color1){
            if(grid.cells.get(x).get(y).current_mass==0){
                Sphere s = new Sphere(radius);
                s.setTranslateX(grid.cells.get(x).get(y).getX1());
                s.setTranslateY(grid.cells.get(x).get(y).getY1());
                s.setMaterial(new PhongMaterial(color1));
                Spheres.getChildren().add(s);
                if(!grid.root.getChildren().contains(Spheres))
                    grid.root.getChildren().add(Spheres);
                current_mass++;
                color=color1;
            }
            else if(this.color!=color1){
                
                f=1;
            }
            else if(grid.cells.get(x).get(y).current_mass>=grid.cells.get(x).get(y).critical_mass-1){
                if(grid.cells.get(x).get(y).critical_mass==2){
                    int p=grid.cells.get(x).get(y).getX1();
                    int q=grid.cells.get(x).get(y).getY1();
                    Spheres.getChildren().clear();
                    Sphere s1 = new Sphere(radius);
                    s1.setTranslateX(p-radius/2);
                    s1.setTranslateY(q);
                    s1.setMaterial(new PhongMaterial(color1));
                    
                    Sphere s2 = new Sphere(radius);
                    s2.setTranslateX(p+radius/2);
                    s2.setTranslateY(q);
                    s2.setMaterial(new PhongMaterial(color1));
                    
                    Spheres.getChildren().addAll(s1,s2);
                    
                    
                }
                else if(grid.cells.get(x).get(y).critical_mass==3){
                    int p=grid.cells.get(x).get(y).getX1();
                    int q=grid.cells.get(x).get(y).getY1();
                    Spheres.getChildren().clear();
                    Sphere s1 = new Sphere(radius);
                    s1.setTranslateX(p-radius/2);
                    s1.setTranslateY(q-3*Math.sqrt(3));
                    s1.setMaterial(new PhongMaterial(color1));
                    Sphere s2 = new Sphere(radius);
                    s2.setTranslateX(p+radius/2);
                    s2.setTranslateY(q-3*Math.sqrt(3));
                    s2.setMaterial(new PhongMaterial(color1));
                    Sphere s3 = new Sphere(radius);
                    s3.setTranslateX(p);
                    s3.setTranslateY(q+3*Math.sqrt(3));
                    s3.setMaterial(new PhongMaterial(color1));
                
                
                    Spheres.getChildren().addAll(s1,s2,s3);
                }
                else if(grid.cells.get(x).get(y).critical_mass==4){
                    int p=grid.cells.get(x).get(y).getX1();
                    int q=grid.cells.get(x).get(y).getY1();
                    Spheres.getChildren().clear();
                    Sphere s1 = new Sphere(radius);
                    s1.setTranslateX(p-radius/2);
                    s1.setTranslateY(q);
                    s1.setMaterial(new PhongMaterial(color1));
                    Sphere s2 = new Sphere(radius);
                    s2.setTranslateX(p+radius/2);
                    s2.setTranslateY(q);
                    s2.setMaterial(new PhongMaterial(color1));
                    Sphere s3 = new Sphere(radius);
                    s3.setTranslateX(p);
                    s3.setTranslateY(q+6*Math.sqrt(3));
                    s3.setMaterial(new PhongMaterial(color1));
                    Sphere s4 = new Sphere(radius);
                    s4.setTranslateX(p);
                    s4.setTranslateY(q-6*Math.sqrt(3));
                    s4.setMaterial(new PhongMaterial(color));
                
                    Spheres.getChildren().addAll(s1,s2,s3,s4);
                }
                current_mass=0;
                explode(x,y,radius,color);
                   
            }
            else if(grid.cells.get(x).get(y).current_mass==1){
                int p=grid.cells.get(x).get(y).getX1();
                int q=grid.cells.get(x).get(y).getY1();
                Spheres.getChildren().clear();
                Sphere s1 = new Sphere(radius);
                s1.setTranslateX(p-radius/2);
                s1.setTranslateY(q);
                s1.setMaterial(new PhongMaterial(color1));
                Sphere s2 = new Sphere(radius);
                s2.setTranslateX(p+radius/2);
                s2.setTranslateY(q);
                s2.setMaterial(new PhongMaterial(color1));
                Spheres.getChildren().addAll(s1,s2);
                
                Circle c = new Circle(p, q, radius/2);
                PathTransition t1=new PathTransition();
                t1.setNode(s1);
                t1.setPath(c);
                t1.setCycleCount(PathTransition.INDEFINITE);
                
                PathTransition t2=new PathTransition();
                t2.setNode(s2);
                t2.setPath(c);
                t2.setCycleCount(PathTransition.INDEFINITE);
                if(critical_mass==3){
                    t1.setDuration(Duration.millis(500));
                    t2.setDuration(Duration.millis(500));
                    t2.setDelay(Duration.millis(250));
                }
                else{
                    t1.setDuration(Duration.seconds(1));
                    t2.setDuration(Duration.seconds(1));
                    t2.setDelay(Duration.millis(500));
                }
                t1.play();
                t2.play();
                current_mass++;
            }
            else if(grid.cells.get(x).get(y).current_mass==2){
                int p=grid.cells.get(x).get(y).getX1();
                int q=grid.cells.get(x).get(y).getY1();
                Spheres.getChildren().clear();
                Sphere s1 = new Sphere(radius);
                s1.setTranslateX(p-radius/2);
                s1.setTranslateY(q-3*Math.sqrt(radius/4));
                s1.setMaterial(new PhongMaterial(color1));
                Sphere s2 = new Sphere(radius);
                s2.setTranslateX(p+radius/2);
                s2.setTranslateY(q-3*Math.sqrt(radius/4));
                s2.setMaterial(new PhongMaterial(color1));
                Sphere s3 = new Sphere(radius);
                s3.setTranslateX(p);
                s3.setTranslateY(q+3*Math.sqrt(radius/4));
                s3.setMaterial(new PhongMaterial(color1));
                
                Circle c = new Circle(p, q, 3*Math.sqrt(radius/4));
                PathTransition t1=new PathTransition();
                t1.setNode(s1);
                t1.setDuration(Duration.millis(750));
                t1.setPath(c);
                t1.setCycleCount(PathTransition.INDEFINITE);
                
                PathTransition t2=new PathTransition();
                t2.setNode(s2);
                t2.setDuration(Duration.millis(750));
                t2.setPath(c);
                t2.setCycleCount(PathTransition.INDEFINITE);
                
                PathTransition t3=new PathTransition();
                t3.setNode(s3);
                t3.setDuration(Duration.millis(750));
                t3.setPath(c);
                t3.setCycleCount(PathTransition.INDEFINITE);
                t1.play();
                t2.setDelay(Duration.millis(250));
                t3.setDelay(Duration.millis(500));
                t2.play();
                t3.play();
                Spheres.getChildren().addAll(s1,s2,s3);
                current_mass++;
            }
        }
        public void explode(int x,int y,int radius,Color color1){
            this.current_mass=0;
            Spheres.getChildren().clear();
            
            if(grid.cells.get(x).get(y).critical_mass==2){
                if((x==0)&&(y==0)){
                    grid.cells.get(x+1).get(y).replace(x+1,y,radius,color1);
                    grid.cells.get(x).get(y+1).replace(x,y+1,radius,color1);
                }
                else if(x==0){
                    grid.cells.get(x+1).get(y).replace(x+1,y,radius,color1);
                    grid.cells.get(x).get(y-1).replace(x,y-1,radius,color1);
                }
                else if(y==0){
                    grid.cells.get(x-1).get(y).replace(x-1,y,radius,color1);
                    grid.cells.get(x).get(y+1).replace(x,y+1,radius,color1);
                }
                else{
                    grid.cells.get(x-1).get(y).replace(x-1,y,radius,color1);
                    grid.cells.get(x).get(y-1).replace(x,y-1,radius,color1);
                }
                
            }
            else if(grid.cells.get(x).get(y).critical_mass==3){
                if(grid.type==0){
                    if(x==0){
                        grid.cells.get(x+1).get(y).replace(x+1,y,radius,color1);
                        grid.cells.get(x).get(y+1).replace(x,y+1,radius,color1);
                        grid.cells.get(x).get(y-1).replace(x,y-1,radius,color1);
                    }
                    else if(y==0){
                        grid.cells.get(x+1).get(y).replace(x+1,y,radius,color1);
                        grid.cells.get(x-1).get(y).replace(x-1,y,radius,color1);
                        grid.cells.get(x).get(y+1).replace(x,y+1,radius,color1);
                    }
                    else if(x==5){
                        grid.cells.get(x).get(y+1).replace(x,y+1,radius,color1);
                        grid.cells.get(x-1).get(y).replace(x-1,y,radius,color1);
                        grid.cells.get(x).get(y-1).replace(x,y-1,radius,color1);
                    }
                    else{
                        grid.cells.get(x+1).get(y).replace(x+1,y,radius,color1);
                        grid.cells.get(x-1).get(y).replace(x-1,y,radius,color1);
                        grid.cells.get(x).get(y-1).replace(x,y-1,radius,color1);
                    }
                    
                }
                else{
                    if(x==0){
                        grid.cells.get(x+1).get(y).replace(x+1,y,radius,color1);
                        grid.cells.get(x).get(y+1).replace(x,y+1,radius,color1);
                        grid.cells.get(x).get(y-1).replace(x,y-1,radius,color1);
                    }
                    else if(y==0){
                        grid.cells.get(x+1).get(y).replace(x+1,y,radius,color1);
                        grid.cells.get(x-1).get(y).replace(x-1,y,radius,color1);
                        grid.cells.get(x).get(y+1).replace(x,y+1,radius,color1);
                    }
                    else if(x==9){
                        grid.cells.get(x).get(y+1).replace(x,y+1,radius,color1);
                        grid.cells.get(x-1).get(y).replace(x-1,y,radius,color1);
                        grid.cells.get(x).get(y-1).replace(x,y-1,radius,color1);
                    }
                    else{
                        grid.cells.get(x+1).get(y).replace(x+1,y,radius,color1);
                        grid.cells.get(x-1).get(y).replace(x-1,y,radius,color1);
                        grid.cells.get(x).get(y-1).replace(x,y-1,radius,color1);
                    }
                }
            }
            else if(grid.cells.get(x).get(y).critical_mass==4){
                grid.cells.get(x-1).get(y).replace(x-1,y,radius,color1);
                grid.cells.get(x+1).get(y).replace(x+1,y,radius,color1);
                grid.cells.get(x).get(y-1).replace(x,y-1,radius,color1);
                grid.cells.get(x).get(y+1).replace(x,y+1,radius,color1);
            }
            Spheres=new Group();
            color=null;
        }
        public void replace(int x,int y,int radius,Color color1){
            this.color=color1;
            if(grid.cells.get(x).get(y).current_mass==0){
                Sphere s = new Sphere(radius);
                s.setTranslateX(grid.cells.get(x).get(y).getX1());
                s.setTranslateY(grid.cells.get(x).get(y).getY1());
                s.setMaterial(new PhongMaterial(color));
                Spheres.getChildren().add(s);
                if(!grid.root.getChildren().contains(Spheres)){
                    grid.root.getChildren().add(Spheres);
                }
                current_mass++;
            }
            else if(grid.cells.get(x).get(y).current_mass>=grid.cells.get(x).get(y).critical_mass-1){
                if(grid.cells.get(x).get(y).critical_mass==2){
                    int p=grid.cells.get(x).get(y).getX1();
                    int q=grid.cells.get(x).get(y).getY1();
                    Spheres.getChildren().clear();
                    Sphere s1 = new Sphere(radius);
                    s1.setTranslateX(p-radius/2);
                    s1.setTranslateY(q);
                    s1.setMaterial(new PhongMaterial(color1));
                    Sphere s2 = new Sphere(radius);
                    s2.setTranslateX(p+radius/2);
                    s2.setTranslateY(q);
                    s2.setMaterial(new PhongMaterial(color1));
                    Spheres.getChildren().addAll(s1,s2);
                    current_mass++;
                }
                else if(grid.cells.get(x).get(y).critical_mass==3){
                    int p=grid.cells.get(x).get(y).getX1();
                    int q=grid.cells.get(x).get(y).getY1();
                    Spheres.getChildren().clear();
                    Sphere s1 = new Sphere(radius);
                    s1.setTranslateX(p-radius/2);
                    s1.setTranslateY(q-3*Math.sqrt(3));
                    s1.setMaterial(new PhongMaterial(color1));
                    Sphere s2 = new Sphere(radius);
                    s2.setTranslateX(p+radius/2);
                    s2.setTranslateY(q-3*Math.sqrt(3));
                    s2.setMaterial(new PhongMaterial(color1));
                    Sphere s3 = new Sphere(radius);
                    s3.setTranslateX(p);
                    s3.setTranslateY(q+3*Math.sqrt(3));
                    s3.setMaterial(new PhongMaterial(color1));
                
                
                    Spheres.getChildren().addAll(s1,s2,s3);
                    current_mass++;
                }
                else if(grid.cells.get(x).get(y).critical_mass==4){
                    int p=grid.cells.get(x).get(y).getX1();
                    int q=grid.cells.get(x).get(y).getY1();
                    Spheres.getChildren().clear();
                    Sphere s1 = new Sphere(radius);
                    s1.setTranslateX(p-radius/2);
                    s1.setTranslateY(q);
                    s1.setMaterial(new PhongMaterial(color1));
                    Sphere s2 = new Sphere(radius);
                    s2.setTranslateX(p+radius/2);
                    s2.setTranslateY(q);
                    s2.setMaterial(new PhongMaterial(color1));
                    Sphere s3 = new Sphere(radius);
                    s3.setTranslateX(p);
                    s3.setTranslateY(q+6*Math.sqrt(3));
                    s3.setMaterial(new PhongMaterial(color1));
                    Sphere s4 = new Sphere(radius);
                    s4.setTranslateX(p);
                    s4.setTranslateY(q-6*Math.sqrt(3));
                    s4.setMaterial(new PhongMaterial(color1));
                
                    Spheres.getChildren().addAll(s1,s2,s3,s4);
                    current_mass++;
                }
                explode(x, y, radius, color1);
                
                
            }
            else if(grid.cells.get(x).get(y).current_mass==1){
                
                int p=grid.cells.get(x).get(y).getX1();
                int q=grid.cells.get(x).get(y).getY1();
                Spheres.getChildren().clear();
                Sphere s1 = new Sphere(radius);
                s1.setTranslateX(p-radius/2);
                s1.setTranslateY(q);
                s1.setMaterial(new PhongMaterial(color1));
                Sphere s2 = new Sphere(radius);
                s2.setTranslateX(p+radius/2);
                s2.setTranslateY(q);
                s2.setMaterial(new PhongMaterial(color1));
                
                Circle c = new Circle(p, q, radius/2);
                PathTransition t1=new PathTransition();
                t1.setNode(s1);
                t1.setPath(c);
                t1.setCycleCount(PathTransition.INDEFINITE);
                
                PathTransition t2=new PathTransition();
                t2.setNode(s2);
                t2.setPath(c);
                t2.setCycleCount(PathTransition.INDEFINITE);
                if(critical_mass==3){
                    t1.setDuration(Duration.millis(400));
                    t2.setDuration(Duration.millis(400));
                    t2.setDelay(Duration.millis(200));
                }
                else{
                    t1.setDuration(Duration.seconds(1));
                    t2.setDuration(Duration.seconds(1));
                    t2.setDelay(Duration.millis(500));
                }
                t1.play();
                t2.play();
                Spheres.getChildren().addAll(s1,s2);
                current_mass++;
            }
            else if(grid.cells.get(x).get(y).current_mass==2){
                int p=grid.cells.get(x).get(y).getX1();
                int q=grid.cells.get(x).get(y).getY1();
                Spheres.getChildren().clear();
                Sphere s1 = new Sphere(radius);
                s1.setTranslateX(p-radius/2);
                s1.setTranslateY(q-3*Math.sqrt(3));
                s1.setMaterial(new PhongMaterial(color1));
                Sphere s2 = new Sphere(radius);
                s2.setTranslateX(p+radius/2);
                s2.setTranslateY(q-3*Math.sqrt(3));
                s2.setMaterial(new PhongMaterial(color));
                Sphere s3 = new Sphere(radius);
                s3.setTranslateX(p);
                s3.setTranslateY(q+3*Math.sqrt(3));
                s3.setMaterial(new PhongMaterial(color));
                
                Circle c = new Circle(p, q, 3*Math.sqrt(radius/4));
                PathTransition t1=new PathTransition();
                t1.setNode(s1);
                t1.setDuration(Duration.millis(750));
                t1.setPath(c);
                t1.setCycleCount(PathTransition.INDEFINITE);
                
                PathTransition t2=new PathTransition();
                t2.setNode(s2);
                t2.setDuration(Duration.millis(750));
                t2.setPath(c);
                t2.setCycleCount(PathTransition.INDEFINITE);
                
                PathTransition t3=new PathTransition();
                t3.setNode(s3);
                t3.setDuration(Duration.millis(750));
                t3.setPath(c);
                t3.setCycleCount(PathTransition.INDEFINITE);
                t1.play();
                t2.setDelay(Duration.millis(250));
                t3.setDelay(Duration.millis(500));
                t2.play();
                t3.play();
                
                Spheres.getChildren().addAll(s1,s2,s3);
                current_mass++;
            }
            color=color1;
        }
        public void copy(savecell c,int x,int y,int radius){
            current_mass=0;
            for (int i = 0; i < c.current_mass; i++) {
                Color color1=C[playerc.color[c.color]];
                replace(x, y, radius, color1);
            }
        }
        
        
        }
    
    
    static class savecell implements  Serializable{
        int xs,xe,ys,ye;
        int critical_mass;
        int current_mass;
        int color;

        public savecell() {
            color=-1;
        }
        
        
        public savecell(int a, int b,int c, int d) {
            xs=a;
            xe=b;
            ys=c;
            ye=d;
            
        }
        
        public void set(int a, int b,int c, int d) {
            xs=a;
            xe=b;
            ys=c;
            ye=d;
        }
        
        public void copy(Cell c){
            current_mass=c.current_mass;
            color=findcolor(c.color);
            critical_mass=c.critical_mass;
        }
        
            
    }
    
    
    static class savecells implements Serializable{
        int x,y,t;
        int player,fg=0;
        savecell[][] c;
        List<Integer> A;

        public savecells() {
            
        }
        public savecells(int a,int b) {
            x=a;
            y=b;
            c=new savecell[x][y];
            for (int i = 0; i < x; i++) {
                for(int j=0;j<y;j++){
                    c[i][j]=new savecell();
                }
            }
        }
        public int getX(){
            return x;
        }
        public int getY(){
            return y;
        }    
        public void setA(List<Integer> a){
            A=a;
        }
        }
    
    
    @SuppressWarnings("serial")
    static class playercolor implements Serializable{
	int[] color=new int[8];
	playercolor(){
            color[0]=0;
            color[1]=1;
            color[2]=2;
            color[3]=3;
            color[4]=4;
            color[5]=5;
            color[6]=6;
            color[7]=7;
	}
    }
}
