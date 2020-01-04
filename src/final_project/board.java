package final_project;

public class board {
    String b_name,b_class,b_usernum,b_title,b_url;
    public board(String b_name,String b_class,String b_usernum,String b_title,String b_url){
        setB_name(b_name);
        setB_class(b_class);
        setB_usernum(b_usernum);
        setB_title(b_title);
        setB_url(b_url);
    }

    public void setB_name(String b_name) {
        this.b_name = b_name;
    }

    public void setB_class(String b_class) {
        this.b_class = b_class;
    }

    public void setB_title(String b_title) {
        this.b_title = b_title;
    }

    public void setB_usernum(String b_usernum) {
        this.b_usernum = b_usernum;
    }

    public void setB_url(String b_url) {
        this.b_url = b_url;
    }

    public String getB_class() {
        return b_class;
    }

    public String getB_name() {
        return b_name;
    }

    public String getB_title() {
        return b_title;
    }

    public String getB_url() {
        return b_url;
    }

    public String getB_usernum() {
        return b_usernum;
    }

    public String toString(){
        return String.format("%-20s%10s\t%s\t%s",getB_name(),getB_usernum(),getB_class(),getB_title());
    }
}
