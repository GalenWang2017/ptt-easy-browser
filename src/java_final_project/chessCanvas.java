package java_final_project;

import java.awt.*;

public class chessCanvas extends Canvas {
//    int x_position,y_position;
        Color c;
    chessCanvas(Color c){
//        this.x_position=x_position;
//        this.y_position=y_position;
        this.c=c;
    }
    public void paint(Graphics graphics){
//        graphics.drawRect(200,80,640,640);
//        for(int row=0;row<8;row++){
//            for(int col=0;col<8;col++){
//                if(row%2==0){
//                    if(col%2==0){
//                        graphics.setColor(Color.WHITE);
//                    }else{
//                        graphics.setColor(Color.BLACK);
//
//                    }
//                }else{
//                    if(col%2==0){
//                        graphics.setColor(Color.BLACK);
//                    }else{
//                        graphics.setColor(Color.WHITE);
//                    }
//                }
//                graphics.fillRect(x_position,y_position,80,80);
//                x_position+=80;
//            }
//
//            y_position+=80;
//            x_position=200;
//        }

        graphics.drawRect(0,0,80,80);
        graphics.setColor(c);
        graphics.fillRect(0,0,80,80);

    }
}
