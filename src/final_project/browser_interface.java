package final_project;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class browser_interface extends JFrame {
    JList article_list;
    JList board_list;
    JButton home_btn;
    JButton articles_btn;
    JButton uppage_btn;
    JButton downpage_btn;
    JButton newest_btn;
    JButton oldest_btn;
    String now_board_url;
    String now_board;
    JTextArea article_content;
    JTextField search_textfield;
    JMenuBar menuBar;
    JMenu menu;
    JList history_list;
    public browser_interface(){
        super("ptt實業坊簡易版");
        ScrollPane list_scrollPane=new ScrollPane();
        ScrollPane content_scrollPane=new ScrollPane();
        article_content=new JTextArea();
        article_content.setMaximumSize(new Dimension(600,750));
        article_content.setText("");
        article_content.setEditable(false);
        article_content.setLineWrap(true);

        //搜尋功能匡設定
        search_textfield=new JTextField();
        search_textfield.setSize(500,45);
        search_textfield.setLocation(20,50);
        search_textfield.setEnabled(false);

        //讀取初始頁面
        ArrayList<board> boards=loadURL.loadBoard("/bbs/");
        String[] btn_link=new String[4];

        //設定JList的model跟那個格線
        DefaultListModel<article> article_model=new DefaultListModel();
        DefaultListModel<board> board_model=new DefaultListModel();
        DefaultListModel<article> history_model=new DefaultListModel<article>();
        article_list=new JList(article_model);
        board_list=new JList(board_model);
        history_list=new JList(history_model);
        board_model.addAll(boards);


        //設定按鈕樣式
        Dimension btn_dimension=new Dimension(80,45);
        uppage_btn=new JButton("上頁");
        uppage_btn.setSize(btn_dimension);
        uppage_btn.setLocation(910,0);
        downpage_btn=new JButton("下頁");
        downpage_btn.setSize(btn_dimension);
        downpage_btn.setLocation(995,0);
        newest_btn=new JButton("最新");
        newest_btn.setSize(btn_dimension);
        newest_btn.setLocation(1080,0);
        oldest_btn=new JButton("最舊");
        oldest_btn.setSize(btn_dimension);
        oldest_btn.setLocation(825,0);
        home_btn=new JButton("ptt實業坊");
        home_btn.setSize(140,45);
        home_btn.setLocation(20,0);
        articles_btn=new JButton();
        articles_btn.setSize(140,45);
        articles_btn.setLocation(170,0);

        setAllPageControlbtnEnable(false);
        articles_btn.setEnabled(false);


        DefaultListCellRenderer cellRenderer=new DefaultListCellRenderer(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            }
        };

        article_list.setCellRenderer(cellRenderer);
        board_list.setCellRenderer(cellRenderer);
        article_list.setFixedCellHeight(100);
        board_list.setFixedCellHeight(80);

        history_list.setFixedCellHeight(100);
        history_list.setCellRenderer(cellRenderer);


        //設定list的動作
        article_list.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                //雙擊可以讀取文章內容
                if(mouseEvent.getClickCount()==2){
                    article_content.setText("");
                    String url=article_model.get(article_list.getSelectedIndex()).getLink();
                    article_model.get(article_list.getSelectedIndex()).setBoard(now_board);
                    insertHistory(article_model.get(article_list.getSelectedIndex()));
                    Thread get_arti_content=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String content=loadURL.loadContent(url);
                            article_content.setText(content);
                        }
                    });
                    if(!url.equals("")){
                        get_arti_content.start();
                    }else {
                        article_content.setText(article_model.get(article_list.getSelectedIndex()).getTitle());
                    }

                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                //點右鍵可以搜尋同標題文章或同作者文章
                if(SwingUtilities.isRightMouseButton(mouseEvent)&& article_list.isSelectedIndex(article_list.getSelectedIndex())){
                    int selectedart=article_list.getSelectedIndex();
                    JPopupMenu m_right_clicked_menu=new JPopupMenu();
                    JMenuItem same_title=new JMenuItem("搜尋同標題文章");
                    JMenuItem same_author=new JMenuItem("搜尋看板內"+article_model.get(selectedart).getAuthor()+"的文章");

                    same_title.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            String url=article_model.get(selectedart).getSame_title_article_link();
                            article_model.removeAllElements();
                            Thread get_same_title=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    article_model.addAll(loadURL.loadAriticle(url));
                                }
                            });
                            Thread get_btn=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String[] new_btn_link=loadURL.loadBtn(url);
                                    btn_link[0]=new_btn_link[0];
                                    btn_link[1]=new_btn_link[1];
                                    btn_link[2]=new_btn_link[2];
                                    btn_link[3]=new_btn_link[3];
                                    isSetEnabled(btn_link);
                                }
                            });
                            get_same_title.start();
                            get_btn.start();
                            articles_btn.setText("回 "+now_board+" 看板");
                        }
                    });
                    same_author.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            String url=article_model.get(selectedart).getSame_author_article_link();
                            article_model.removeAllElements();
                            Thread get_same_author=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    article_model.addAll(loadURL.loadAriticle(url));
                                }
                            });
                            Thread get_btn=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String[] new_btn_link=loadURL.loadBtn(url);
                                    btn_link[0]=new_btn_link[0];
                                    btn_link[1]=new_btn_link[1];
                                    btn_link[2]=new_btn_link[2];
                                    btn_link[3]=new_btn_link[3];
                                    isSetEnabled(btn_link);
                                }
                            });
                            get_same_author.start();
                            get_btn.start();
                            articles_btn.setText("回 "+now_board+" 看板");
                        }
                    });

                    m_right_clicked_menu.add(same_title);
                    m_right_clicked_menu.add(same_author);
                    if(!article_model.get(selectedart).getLink().equals("")){
                        m_right_clicked_menu.show(article_list,mouseEvent.getX(),mouseEvent.getY());
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
        article_list.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                Point point=mouseEvent.getPoint();
                article_list.setSelectedIndex(article_list.locationToIndex(point));
            }
        });
        board_list.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                //雙擊進入看板
                if(mouseEvent.getClickCount()==2){
                    article_model.removeAllElements();
                    Thread loadarticle_list=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            article_model.addAll(loadURL.loadAriticle(board_model.get(board_list.getSelectedIndex()).getB_url()));
                        }
                    });
                    Thread loadbtn=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String[] new_btn_link=loadURL.loadBtn(board_model.get(board_list.getSelectedIndex()).getB_url());
                            btn_link[0]=new_btn_link[0];
                            btn_link[1]=new_btn_link[1];
                            btn_link[2]=new_btn_link[2];
                            btn_link[3]=new_btn_link[3];
                            isSetEnabled(btn_link);
                        }
                    });
                    loadarticle_list.start();
                    loadbtn.start();
                    articles_btn.setText("看板 "+board_model.get(board_list.getSelectedIndex()).getB_name());
                    articles_btn.setEnabled(true);
                    now_board_url=board_model.get(board_list.getSelectedIndex()).getB_url();
                    now_board=board_model.get(board_list.getSelectedIndex()).getB_name();
                    list_scrollPane.remove(board_list);
                    list_scrollPane.add(article_list);
                    list_scrollPane.revalidate();
                    search_textfield.setEnabled(true);
                    search_textfield.setSelectionStart(0);
                    search_textfield.setText("搜尋本看版文章");

                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
        board_list.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                Point point=mouseEvent.getPoint();
                board_list.setSelectedIndex(board_list.locationToIndex(point));
            }
        });

        history_list.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                //雙擊讀取文章
                if(mouseEvent.getClickCount()==2){
                    article_content.setText("");
                    String url=history_model.get(history_list.getSelectedIndex()).getLink();
                    insertHistory(history_model.get(history_list.getSelectedIndex()));
                    Thread get_arti_content=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String content=loadURL.loadContent(url);
                            article_content.setText(content);
                        }
                    });
                    if(!url.equals("")){
                        get_arti_content.start();
                    }else {
                        article_content.setText(history_model.get(history_list.getSelectedIndex()).getTitle());
                    }
                    history_model.removeAllElements();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<article> his_list=getHistory();
                            history_model.addAll(his_list);
                        }
                    }).start();

                    list_scrollPane.remove(article_list);
                    list_scrollPane.remove(board_list);
                    list_scrollPane.add(history_list);
                    list_scrollPane.validate();

                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
        history_list.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                Point point=mouseEvent.getPoint();
                history_list.setSelectedIndex(history_list.locationToIndex(point));
            }
        });


        //上頁button的動作
        uppage_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setArticle_contentClear();
                article_model.removeAllElements();
                Thread get_new_articles=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        article_model.addAll(loadURL.loadAriticle(btn_link[1]));
                    }
                });
                Thread get_new_btn=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String[] new_link=loadURL.loadBtn(btn_link[1]);
                        btn_link[0]=new_link[0];
                        btn_link[1]=new_link[1];
                        btn_link[2]=new_link[2];
                        btn_link[3]=new_link[3];
                        isSetEnabled(btn_link);
                    }
                });
                get_new_articles.start();
                get_new_btn.start();

            }
        });
        //下頁button的動作
        downpage_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setArticle_contentClear();
                article_model.removeAllElements();
                Thread get_new_articles=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        article_model.addAll(loadURL.loadAriticle(btn_link[2]));
                    }
                });
                Thread get_new_btn=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String[] new_link=loadURL.loadBtn(btn_link[2]);
                        btn_link[0]=new_link[0];
                        btn_link[1]=new_link[1];
                        btn_link[2]=new_link[2];
                        btn_link[3]=new_link[3];
                        isSetEnabled(btn_link);
                    }
                });
                get_new_articles.start();
                get_new_btn.start();
            }
        });
        //最新button的動作
        newest_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setArticle_contentClear();
                article_model.removeAllElements();
                Thread get_new_articles=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        article_model.addAll(loadURL.loadAriticle(btn_link[3]));
                    }
                });
                Thread get_new_btn=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String[] new_link=loadURL.loadBtn(btn_link[3]);
                        btn_link[0]=new_link[0];
                        btn_link[1]=new_link[1];
                        btn_link[2]=new_link[2];
                        btn_link[3]=new_link[3];
                        isSetEnabled(btn_link);
                    }
                });
                get_new_articles.start();
                get_new_btn.start();

            }
        });
        //最舊button的動作
        oldest_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setArticle_contentClear();
                article_model.removeAllElements();
                Thread get_new_articles=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        article_model.addAll(loadURL.loadAriticle(btn_link[0]));
                    }
                });
                Thread get_new_btn=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String[] new_link=loadURL.loadBtn(btn_link[0]);
                        btn_link[0]=new_link[0];
                        btn_link[1]=new_link[1];
                        btn_link[2]=new_link[2];
                        btn_link[3]=new_link[3];
                        isSetEnabled(btn_link);
                    }
                });
                get_new_articles.start();
                get_new_btn.start();
            }
        });
        //首頁按鈕的動作
        home_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setArticle_contentClear();
                setAllPageControlbtnEnable(false);
                list_scrollPane.remove(article_list);
                list_scrollPane.remove(history_list);
                list_scrollPane.remove(board_list);
                list_scrollPane.add(board_list);
                list_scrollPane.revalidate();
                articles_btn.setText("");
                articles_btn.setEnabled(false);
                now_board_url="";
                now_board="";
                search_textfield.setEnabled(false);
                search_textfield.setText("");

            }
        });
        //看板按鈕的動作
        articles_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setArticle_contentClear();
                article_model.removeAllElements();
                Thread return_articles=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        article_model.addAll(loadURL.loadAriticle(now_board_url));
                    }
                });
                Thread return_btn_status=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String[] return_btn=loadURL.loadBtn(now_board_url);
                        btn_link[0]=return_btn[0];
                        btn_link[1]=return_btn[1];
                        btn_link[2]=return_btn[2];
                        btn_link[3]=return_btn[3];
                        isSetEnabled(btn_link);
                    }
                });
                return_articles.start();
                return_btn_status.start();
                search_textfield.setText("搜尋本看板文章");
                article_content.setText("");
                articles_btn.setText(now_board+"看板");
                list_scrollPane.remove(history_list);
                list_scrollPane.remove(article_list);
                list_scrollPane.add(article_list);
                list_scrollPane.revalidate();
            }
        });

        //search article
        search_textfield.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode()==KeyEvent.VK_ENTER){
                    if(!search_textfield.getText().equals("")){
                        article_model.removeAllElements();
                        String search_url="/bbs/"+now_board+"/search?q="+search_textfield.getText();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                article_model.addAll(loadURL.loadAriticle(search_url));
                            }
                        }).start();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String[] new_btn_link=loadURL.loadBtn(search_url);
                                btn_link[0]=new_btn_link[0];
                                btn_link[1]=new_btn_link[1];
                                btn_link[2]=new_btn_link[2];
                                btn_link[3]=new_btn_link[3];
                                isSetEnabled(btn_link);
                            }
                        }).start();
                        search_textfield.setSelectionStart(0);
                        articles_btn.setText("回 "+now_board+" 看板");
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });

        menuBar=new JMenuBar();
        menu=new JMenu("歷史紀錄");
        JMenuItem history_menu_item=new JMenuItem("歷史紀錄");
        history_menu_item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                history_model.removeAllElements();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<article> his_list=getHistory();
                        history_model.addAll(his_list);
                    }
                }).start();
                search_textfield.setEnabled(false);
                search_textfield.setText("");
                list_scrollPane.remove(article_list);
                list_scrollPane.remove(board_list);
                list_scrollPane.add(history_list);
                list_scrollPane.validate();
            }
        });
        menu.add(history_menu_item);
        menuBar.add(menu);

        //設定介面
        setLayout(null);
        setJMenuBar(menuBar);
        add(uppage_btn);
        add(oldest_btn);
        add(newest_btn);
        add(downpage_btn);
        add(home_btn);
        add(articles_btn);
        add(search_textfield);

        list_scrollPane.add(board_list);
        list_scrollPane.setLocation(20,110);
        list_scrollPane.setSize(500,750);
        content_scrollPane.add(article_content);
        content_scrollPane.setSize(610,750);
        content_scrollPane.setLocation(550,110);
        add(list_scrollPane);
        add(content_scrollPane);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1300,950);
        setLocation(100,50);
    }

    //設定哪個button是不能按的
    public void isSetEnabled(String[] link){
        if(link[1].equals("")){
            uppage_btn.setEnabled(false);
        }else {
            uppage_btn.setEnabled(true);
        }
        if(link[2].equals("")){
            downpage_btn.setEnabled(false);
        }else {
            downpage_btn.setEnabled(true);
        }
        if(link[3].equals("")){
            newest_btn.setEnabled(false);
        }else {
            newest_btn.setEnabled(true);
        }
        if(link[0].equals("")){
            oldest_btn.setEnabled(false);
        }else {
            oldest_btn.setEnabled(true);
        }
    }
    //設定全部按鈕都不能按
    public void setAllPageControlbtnEnable(Boolean b){
        newest_btn.setEnabled(b);
        oldest_btn.setEnabled(b);
        downpage_btn.setEnabled(b);
        uppage_btn.setEnabled(b);
    }
    public void setArticle_contentClear(){
        article_content.setText("");
    }

    //連接mongoDB儲存歷史紀錄
    public void insertHistory(article article){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MongoClient mongoClient = new MongoClient("localhost",27017);
                MongoDatabase mongoDatabase = mongoClient.getDatabase("browse_history");
                MongoCollection<Document> mongoCollection= mongoDatabase.getCollection("history");
                JSONObject history_object=new JSONObject();
                history_object.put("title",article.getTitle());
                history_object.put("author",article.getAuthor());
                history_object.put("date", article.getDate());
                history_object.put("link",article.getLink());
                history_object.put("push_num",article.getPush_num());
                history_object.put("board",article.getBoard());
                mongoCollection.insertOne(Document.parse(history_object.toString()));

            }
        }).start();
    }
    public ArrayList<article> getHistory(){
        ArrayList<article> articles=new ArrayList<>();
        int count=0;

        MongoClient mongoClient = new MongoClient("localhost",27017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("browse_history");
        MongoCollection<Document> mongoCollection= mongoDatabase.getCollection("history");
        FindIterable<Document> fIterable=mongoCollection.find();
        MongoCursor<Document> cursor=fIterable.iterator();
        while (cursor.hasNext()){
            Document gethistory=cursor.next();
            article article=new article(gethistory.get("link").toString(),
                    gethistory.get("title").toString(),
                    gethistory.get("author").toString(),
                    gethistory.get("date").toString(),
                    gethistory.get("push_num").toString(),
                    "","",gethistory.get("board").toString());
            articles.add(0,article);
            count++;
        }
        for(int i=count;i>20;i--){
            articles.remove(articles.size()-1);
        }
        return articles;
    }
}
