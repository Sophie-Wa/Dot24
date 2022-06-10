package com.example.dot24;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Data{
    String zhanghao=new String();
    String passwd=new String();

    ArrayList<Integer> paimian=new ArrayList<Integer>();//牌面
    ArrayList<Integer>value=new ArrayList<Integer>();//实际值
    public Data(String zhanghao,String passwd){
        this.zhanghao=zhanghao;
        this.passwd=passwd;
    }
    public void fapai(int num){
        paimian.add(num);
        if (num%13>10||num%13==0)
            value.add(1);//如果JQK，等于1
        else
            value.add(num%13);//否则值为%13余数
    }
    public Integer getvlaue(){//返回总value
        Integer sum=0;
        for(int i=0;i<value.size();i++){
            sum+=value.get(i);
        }
        return sum;
    }
    public boolean fa_not(){//判断是否需要发牌（13张牌平均值为4.23，即三张牌和小于19时发牌）
        if(getvlaue()<=18){
            return true;
        }
        else
            return false;
    }
    public int Is_not(String zhanghao,String passwd){//判断是否登录成功
        if(this.zhanghao.equals(zhanghao)&&this.passwd.equals(passwd))
            return 1;//登录
        else if (this.zhanghao.equals(zhanghao))
            return 0;//账号/密码错误
        else return -1;//未注册
    }

}
public class HelloApplication extends Application {
    static ArrayList<Data> data=new ArrayList<Data>();//存储所有用户数据；
    static ArrayList<Integer> dealed=new ArrayList<>();
    static int sum=0;//玩家人数
    static int user=-1;//玩家索引
    @Override
    public void start(Stage stage) throws IOException {
//        BorderPane group1=new BorderPane();
        BorderPane group1=new BorderPane();



        Scene scene = new Scene(group1, 800,500,Color.GREEN);

        Login(group1);
//        Display(group1);
//        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setTitle("Dot24");
        stage.setScene(scene);
        stage.show();
    }
    public void Login(BorderPane g1){//登陆界面
        g1.getChildren().clear();
        Label lb1=new Label("账号");lb1.setStyle("-fx-text-fill: rgb(0,245,255);" + "    -fx-font-size:30;");
        Label lb2=new Label("密码");lb2.setStyle("-fx-text-fill: rgb(0,245,255);" + "    -fx-font-size:30;");
        TextField tf1=new TextField();tf1.setStyle("-fx-background-color:rgba(255, 250, 250,0.8);" + "    -fx-font-size:20;" + "    -fx-width:150;" + "    -fx-height:20;");
        PasswordField tf2=new PasswordField();tf2.setStyle("-fx-background-color:rgba(255, 250, 250,0.8);" + "    -fx-font-size:20;" + "    -fx-width:150;" + "    -fx-height:20;");
        tf1.setPromptText("请输入11位//8位账号");//为方便测试，txt文件内存放的是4位账号
        tf1.setTooltip(new Tooltip("请输入11位//8位账号"));
        tf2.setPromptText("请输入密码");
        tf2.setTooltip(new Tooltip("请输入密码"));
        Button bt1=new Button("注册");bt1.setStyle("-fx-font-size:15;" + "-fx-background-color:rgba(255, 250, 250,0.8);" + "-fx-border-color:rgba(0,0,0,0);");
        Button bt2=new Button("登录");bt2.setStyle("-fx-font-size:15;" + "-fx-background-color:rgba(255, 250, 250,0.8);" + "-fx-border-color:rgba(0,0,0,0);");
        bt1.setCursor(Cursor.HAND);
        bt2.setCursor(Cursor.HAND);
        HBox hb1=new HBox();
        HBox hb2=new HBox();
        HBox hb3=new HBox(20);
        hb1.getChildren().addAll(lb1,tf1);
        hb2.getChildren().addAll(lb2,tf2);
        hb3.getChildren().addAll(bt1,bt2);
        VBox vbox1=new VBox(20);
        hb1.setAlignment(Pos.CENTER);
        hb2.setAlignment(Pos.CENTER);
        hb3.setAlignment(Pos.CENTER);
        vbox1.getChildren().addAll(hb1,hb2,hb3);
        vbox1.setMinHeight(120);
        vbox1.setMinWidth(220);
        vbox1.setAlignment(Pos.CENTER);
        vbox1.setStyle("-fx-padding:30;");
      //  g1.getChildren().add(vbox1);
g1.setStyle("-fx-background-color:green;");
        g1.setCenter(vbox1);
     //   g1.setBackground(new Background(Color.GREEN));
        bt2.setOnAction(e->{
            int i=whichone(tf1,tf2);
           if( display(i,tf1,tf2,bt2)==1)//如果登陆成功的话，发牌，（在弹出窗口设置了优先级，不处理完，下层窗口不会响应）
               Display(g1);
        });

    }//登陆界面
    public void Display(BorderPane g1){//显示游戏主界面
        g1.getChildren().clear();
//        g1.minWidth(700);
//        g1.minHeight(500);
        Image bk1=new Image("file:card/b1fv.png");
        ImageView im11=new ImageView(bk1);
        HBox imagehbox1=new HBox();imagehbox1.setMinWidth(216);imagehbox1.setMinHeight(96);imagehbox1.setStyle("-fx-background-color:rgb(227,227,227);");
        HBox imagehbox2=new HBox();imagehbox2.setMinWidth(216);imagehbox2.setMinHeight(96);imagehbox2.setStyle("-fx-background-color:rgb(227,227,227);");
        HBox imagehbox3=new HBox();imagehbox3.setMinWidth(216);imagehbox3.setMinHeight(96);imagehbox3.setStyle("-fx-background-color:rgb(227,227,227);");
        HBox userimagehbox4=new HBox();userimagehbox4.setMinWidth(216);userimagehbox4.setMinHeight(96);userimagehbox4.setStyle("-fx-background-color:rgb(227,227,227);");
        Label label1=new Label("电脑玩家1");label1.setStyle("-fx-text-fill: rgb(0,245,255);-fx-font-size:20;");HBox box1=new HBox(label1);box1.setAlignment(Pos.TOP_CENTER);box1.setStyle("-fx-background-color:rgb(189,189,189);");
        Label label2=new Label("电脑玩家2");label2.setStyle("-fx-text-fill: rgb(0,245,255);-fx-font-size:20;");HBox box2=new HBox(label2);box2.setAlignment(Pos.TOP_CENTER);box2.setStyle("-fx-background-color:rgb(189,189,189);");
        Label label3=new Label("电脑玩家3");label3.setStyle("-fx-text-fill: rgb(0,245,255);-fx-font-size:20;");HBox box3=new HBox(label3);box3.setAlignment(Pos.TOP_CENTER);box3.setStyle("-fx-background-color:rgb(189,189,189);");
        Label userlabel=new Label("你");userlabel.setStyle("-fx-text-fill: rgb(0,245,255);-fx-font-size:20;");HBox userbox=new HBox(userlabel);userbox.setAlignment(Pos.TOP_CENTER);userbox.setStyle("-fx-background-color:rgb(189,189,189);");
        VBox computerVbox1=new VBox(box1,imagehbox1);//computerVbox1.setStyle("-fx-border-color:black");
        VBox computerVbox2=new VBox(box2,imagehbox2);
        VBox computerVbox3=new VBox(box3,imagehbox3);
        VBox userVbox4=new VBox(userbox,userimagehbox4);

        HBox UsersHbox1=new HBox(20,computerVbox1);UsersHbox1.setStyle("-fx-padding:0");
        HBox UsersHbox2=new HBox(20,userVbox4);UsersHbox2.setStyle("-fx-padding:0");

        Button dealbutton=new Button("发牌");dealbutton.setStyle("-fx-font-size:15;" + "-fx-background-color:rgba(255, 250, 250,0.8);" + "-fx-border-color:rgba(0,0,0,0);");dealbutton.setCursor(Cursor.HAND);
        Button openbutton=new Button("开牌");openbutton.setStyle("-fx-font-size:15;" + "-fx-background-color:rgba(255, 250, 250,0.8);" + "-fx-border-color:rgba(0,0,0,0);");openbutton.setCursor(Cursor.HAND);
        UsersHbox1.setAlignment(Pos.CENTER);
        UsersHbox2.setAlignment(Pos.CENTER);
        HBox buttonbox=new HBox(50,openbutton,dealbutton);buttonbox.setAlignment(Pos.CENTER);
        VBox Allvbox=new VBox(30,UsersHbox1,UsersHbox2,buttonbox);Allvbox.setStyle("-fx-padding:0");
        Allvbox.setAlignment(Pos.CENTER);
//        g1.getChildren().addAll(Allvbox);
        g1.setCenter(Allvbox);
        //发一次牌按钮
        Button deal1button=new Button("发牌");deal1button.setStyle("-fx-font-size:15;" + "-fx-background-color:rgba(255, 250, 250,0.8);" + "-fx-border-color:rgba(0,0,0,0);");deal1button.setCursor(Cursor.HAND);
        sunum(UsersHbox1,UsersHbox2,computerVbox2,computerVbox3);//判断几个玩家
        dealbutton.setOnAction(e->{//发牌按钮执行以下内容
            fapai(1,3);
            carddeal(imagehbox1,3);//computer1展示背面
            fapai(0,3);
            cardshow(userimagehbox4,0);//玩家展示正面
            if(sum>=3){
                fapai(2,3);//computer2展示背面
                carddeal(imagehbox2,3);
            }
            if(sum>=4){
                fapai(3,3);//computer3展示背面
                carddeal(imagehbox3,3);
            }
            buttonbox.getChildren().remove(dealbutton);
            buttonbox.getChildren().add(deal1button);
        });
            deal1button.setOnAction(e->{//在发牌后，改变按钮，只发一张牌
                fapai(1,1);
                carddeal(imagehbox1,1);//computer1展示背面
                fapai(0,1);
                cardshow(userimagehbox4,0);//玩家展示正面
                if(sum>=3){
                    fapai(2,1);
                    carddeal(imagehbox2,1);//computer2展示背面
                }
                if(sum>=4){
                    fapai(3,1);
                    carddeal(imagehbox3,1);//computer3展示背面
                }
            });
            openbutton.setOnAction(e->{
                cardshow(imagehbox1,1);
                cardshow(imagehbox2,2);
                cardshow(imagehbox3,3);
            });

//       Label tt1=new Label("TEST");tt1.setStyle("-fx-text-fill:red;-fx-font-size:20;");
//        HBox test=new HBox(tt1);
//        test.setAlignment(Pos.CENTER);
//       computerVbox1.getChildren().add(test);
    }//显示游戏主界面
    public void sunum(HBox UsersHbox1,HBox UsersHbox2,VBox computerVbox2,VBox computerVbox3){//判断几个玩家
        if(sum>=3){
            UsersHbox1.getChildren().add(computerVbox2);
        }
        if(sum>=4){
            UsersHbox2.getChildren().add(computerVbox3);
        }
    }
    public void cardshow(HBox box,int n){//显示牌面
        box.getChildren().clear();
        String file="file:card/";
        int i=0;
        while (i<data.get(n).paimian.size()){
            System.out.println(i);
            box.getChildren().add(new ImageView(new Image(file+data.get(n).paimian.get(i)+".png")));
            i++;
        }
    }
    public void carddeal(HBox box,int n){//显示电脑未开牌

        String file="file:card/b1fv.png";
        if (n==3){
            box.getChildren().clear();
            ImageView img1=new ImageView(new Image(file));
            ImageView img2=new ImageView(new Image(file));
            ImageView img3=new ImageView(new Image(file));
            box.getChildren().addAll(img1,img2,img3);
        } else if (n==1) {
            box.getChildren().add(new ImageView(new Image(file)));
        }
    }
    public void fapai(int i,int n){//发牌，三张还是一张
        int num=0;
        int j=0;
        while (j<n){
            num=(int)(Math.random()*52+1);

            if(Repeat(num)){
                System.out.println("对象"+i+"张数"+j+"num"+num);
                j++;
                data.get(i).fapai(num);
                dealed.add(num);
            }
        }

    }//发牌，三张还是一张
    public boolean Repeat(int n){//判断是否重复
        int j=0;
        while(j<dealed.size()){
            if (n==dealed.get(j)){
                return false;
            }
            j++;
        }
        return true;
    }//判断是否重复
    public int whichone(TextField a,TextField b){//判断输入的账号数据是否存在/错误类型
        String zh=a.getText();
        String mm=b.getText();
        int i=0;
        int type=-1;
        while (i<data.size()){
            int t=data.get(i).Is_not(zh,mm);
            if(t==1){
             user=i;
             type=1;
             break;
            }else if (t==0){
                type=0;
                break;
            }
            else {
                i++;
                continue;
            }

        }
        return type;
    }//判断输入数据是否存在/错误类型
    public static int  display(int type, TextField a,TextField b,Button login){//弹出窗口
        String t=new String();

        Stage window = new Stage();
        window.setTitle("提示");
        //modality要使用Modality.APPLICATION_MODEL
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(300);
        window.setMinHeight(150);
        Button button = new Button("确定");
        Button button2=new Button("2");
        Button button3=new Button("3");
        Button button4=new Button("4");

        Label label=new Label();
        label.setStyle("-fx-font-size:20;");
        button.setStyle("-fx-font-size:20;");
        button2.setStyle("-fx-font-size:20;");
        button3.setStyle("-fx-font-size:20;");
        button4.setStyle("-fx-font-size:20;");
        HBox hBox1=new HBox(10);
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        //layout.setLayoutX(50);
        if(type==-1){
            t="账号不存在!";
            hBox1.getChildren().add(button);
           // login.setDisable(true);
        }
        else if (type==0){
            t="账号或密码错误!";
            hBox1.getChildren().add(button);
        }
        else if(type==1){
            t="登陆成功,请选择玩家数:";
            hBox1.getChildren().addAll(button2,button3,button4);
        }
        button.setOnAction(e ->{
            window.close();
            a.setText("");
            b.setText("");
        } );
        button2.setOnAction(e->{
            window.close();
            sum=2;
        });
        button3.setOnAction(e->{
            window.close();
            sum=3;
        });
        button4.setOnAction(e->{
            window.close();
            sum=4;
        });
        label.setText(t);
        layout.getChildren().addAll(label,hBox1);
        label.setAlignment(Pos.TOP_CENTER);
        hBox1.setAlignment(Pos.BOTTOM_CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();//使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
        return type;
    }//弹出提示窗口
    public static void readFileByLines(String fileName) {//读入数据
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int i=0;
            while ((tempString = reader.readLine())!=null){
                String []tem=tempString.split("\\s+");
                data.add(new Data(tem[0],tem[1]));
                System.out.println(data.get(i).zhanghao+"\s"+data.get(i).passwd);
                i++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }//读入数据
    public static void main(String[] args) {
        readFileByLines("Users.txt");
//        File file = new File("./");
//        System.out.println(file.getAbsoluteFile());
       launch();
    }
}