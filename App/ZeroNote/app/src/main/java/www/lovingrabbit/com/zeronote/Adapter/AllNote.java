package www.lovingrabbit.com.zeronote.Adapter;

public class AllNote {
    int id;
    String title,article,updatetime;
    public boolean isSelect;
    public AllNote(String mTitle,String mArticle,int id,String updatetime){
        title = mTitle;
        article = mArticle;
        this.id = id;
        this.updatetime = updatetime;
    }

    public String getArticle() {
        return article;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public boolean isSelect() {
        return isSelect;
    }
    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}
