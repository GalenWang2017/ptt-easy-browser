package final_project;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class loadURL {
    public static Document loadHTMLContent(String url){
        Document doc = null;
        try{
            String str_url="https://www.ptt.cc"+url;
            URL loadurl=new URL(str_url);
            HttpURLConnection urlConnection=(HttpURLConnection) loadurl.openConnection();
            String cookie_str="over18=yes";//send is over 18
            urlConnection.setRequestProperty("Cookie",cookie_str);
            urlConnection.connect();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String input_line="";
            StringBuilder html_content=new StringBuilder();
            while ((input_line = bufferedReader.readLine()) != null) {
                html_content.append(String.format("%s\n",input_line));
            }
            doc= Jsoup.parse(html_content.toString());
            bufferedReader.close();
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public static ArrayList<board> loadBoard(String url){
        Document doc=loadHTMLContent(url);
        ArrayList<board> boards=new ArrayList<board>();
        int boards_size=doc.getElementsByClass("b-ent").size();

        for(int index=0;index<boards_size;index++){
            String b_name,b_usernum,b_class,b_title,b_url;
            Document board_info=Jsoup.parse(doc.getElementsByClass("b-ent").get(index).toString());
            b_name=board_info.getElementsByClass("board-name").text();
            b_usernum=board_info.getElementsByClass("board-nuser").text();
            b_class=board_info.getElementsByClass("board-class").text();
            b_title=board_info.getElementsByClass("board-title").text();
            b_url=board_info.getElementsByClass("board").attr("href").toString();
            board board=new board(b_name,b_class,b_usernum,b_title,b_url);
            boards.add(board);
        }
        return boards;
    }

    public static ArrayList<article> loadAriticle(String url){

        ArrayList<article> articles=new ArrayList<article>();
        Document all_articles=loadHTMLContent(url);

        int articles_size=all_articles.getElementsByClass("r-ent").size();
        for(int index=0;index<articles_size;index++){
            Document article_rent=Jsoup.parse(all_articles.getElementsByClass("r-ent").get(index).toString());
            String title=article_rent.getElementsByClass("title").text();
            String link=link=article_rent.getElementsByTag("a").attr("href").toString();
            String author="";
            String date=article_rent.getElementsByClass("date").text().toString();
            String push_num="";
            String same_author_link="";
            String same_title_link="";
            article article;
            if(title.equals("") || link.equals("")){
                title="本文已被刪除";
                link="";
                author="";
                push_num="";
                same_author_link="";
                same_title_link="";
            }else if((!title.contains("刪除")) && (!title.contains("已被"))){
                author=article_rent.getElementsByClass("author").text();
                push_num=article_rent.getElementsByClass("nrec").text().toString();
                same_author_link=article_rent.getElementsByClass("item").get(1).getElementsByTag("a").attr("href").toString();
                same_title_link=article_rent.getElementsByClass("item").get(0).getElementsByTag("a").attr("href").toString();
            }else{
                link="";
                author="";
                push_num="";
                same_author_link="";
                same_title_link="";
            }
            article=new article(link,title,author,date,push_num,same_title_link,same_author_link,"");
            articles.add(article);
        }

        return articles;
    }
    public static String loadContent(String contenturl){
        Document doc_content=loadHTMLContent(contenturl);
        Document doc=Jsoup.parse(doc_content.getElementById("main-content").toString());
//        System.out.println(doc.toString());
        StringBuilder stringBuilder=new StringBuilder();
        doc.toString().lines().forEach(s -> stringBuilder.append(" "+Jsoup.parse(s).text()+"\n"));
//        System.out.println(stringBuilder.toString());
        return  stringBuilder.toString();
    }
    public static String[] loadBtn(String url){
        Document btn_doc=loadHTMLContent(url);
        //string[0] = oldest article
        //string[1] = up page
        //string[2] = down page
        //string[3] = newest article
        String[] btn_href=new String[4];
            for(int i=0;i<btn_doc.getElementsByClass("btn wide").size();i++){

                if(btn_doc.getElementsByClass("btn wide").get(i).text().equals("‹ 上頁")){
                    btn_href[1]=btn_doc.getElementsByClass("btn wide").get(i).attr("href").toString();
                }else if(btn_doc.getElementsByClass("btn wide").get(i).text().equals("下頁 ›")){
                    btn_href[2]=btn_doc.getElementsByClass("btn wide").get(i).attr("href").toString();
                }
                else if(btn_doc.getElementsByClass("btn wide").get(i).text().equals("最新")){
                    btn_href[3]=btn_doc.getElementsByClass("btn wide").get(i).attr("href").toString();
                }else if(btn_doc.getElementsByClass("btn wide").get(i).text().equals("最舊")){
                    btn_href[0]=btn_doc.getElementsByClass("btn wide").get(i).attr("href").toString();
                }

            }
            for(int i=0;i<btn_doc.getElementsByClass("btn wide disabled").size();i++){

                if(btn_doc.getElementsByClass("btn wide disabled").get(i).text().equals("‹ 上頁")){
                    btn_href[1]="";
                }else if(btn_doc.getElementsByClass("btn wide disabled").get(i).text().equals("下頁 ›")){
                    btn_href[2]="";
                }
                else if(btn_doc.getElementsByClass("btn wide disabled").get(i).text().equals("最新")){
                    btn_href[3]="";
                }else if(btn_doc.getElementsByClass("btn wide disabled").get(i).text().equals("最舊")){
                    btn_href[0]="";
                }
            }


        return btn_href;

    }
}
