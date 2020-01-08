package final_project;

public class article {
    String link,title,author,date;
    String push_num;
    String same_title_article_link,same_author_article_link;
    String board;
    public article(String link,String title,String author,String date,String push_num,String same_title,String same_author,String board){
        setLink(link);
        setTitle(title);
        setAuthor(author);
        setDate(date);
        setPush_num(push_num);
        setSame_author_article_link(same_author);
        setSame_title_article_link(same_title);
        setBoard(board);
    }
    public void setLink(String link){
        this.link=link;
    }public void setTitle(String title){
        this.title=title;
    }
    public void setAuthor(String author){
        this.author=author;
    }
    public void setDate(String date){
        this.date=date;
    }
    public void setPush_num(String push_num){
        this.push_num=push_num;
    }
    public void setSame_author_article_link(String same_author_article_link) {
        this.same_author_article_link = same_author_article_link;
    }
    public void setSame_title_article_link(String same_title_article_link) {
        this.same_title_article_link = same_title_article_link;
    }
    public void setBoard(String board){
        this.board=board;
    }

    public String getLink(){
        return link;
    }
    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public String getDate(){
        return date;
    }
    public String getPush_num(){
        return push_num;
    }
    public String getSame_author_article_link() {
        return same_author_article_link;
    }

    public String getSame_title_article_link() {
        return same_title_article_link;
    }
    public String getBoard(){
        return board;
    }

    public String toString(){
        if(board.equals("")){
            return String.format("%s   %s\t\t\t%s\n%s",getPush_num(),getTitle(),getDate(),getAuthor());
        }else {
            return String.format("%s   %s\t\t\t%s\n%s @ %s",getPush_num(),getTitle(),getDate(),getAuthor(),getBoard());
        }
    }
}
