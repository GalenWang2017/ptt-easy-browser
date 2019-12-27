package java_final_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Array;

public class myframe extends JFrame {
    myframe(){
        super("final project");
        setSize(640,640);
        setLayout(new GridLayout(1,1));
        chessCanvas[][] board=new chessCanvas[8][8];

        Panel panel=new Panel();
        panel.setLayout(new GridLayout(8,8));
        panel.setSize(640,640);
        panel.setVisible(true);

        for(int row=0;row<8;row++){
            for(int col=0;col<8;col++){
                if(row%2==0){
                    if(col%2==0){
                        board[row][col]=new chessCanvas(Color.WHITE);
                    }else{
                        board[row][col]=new chessCanvas(Color.BLACK);
                    }
                }else {
                    if (col % 2 == 0) {
                        board[row][col] = new chessCanvas(Color.BLACK);
                    } else {
                        board[row][col] = new chessCanvas(Color.WHITE);
                    }
                }

                panel.add(board[row][col]);
            }
        }
        board[0][0].addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                board[0][0].c=Color.BLUE;
                panel.revalidate();
                panel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        add(panel);
    }

}
