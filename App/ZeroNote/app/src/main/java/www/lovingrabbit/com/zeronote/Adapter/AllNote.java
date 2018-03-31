package www.lovingrabbit.com.zeronote.Adapter;

public class AllNote {
    String title,article;
    public AllNote(String mTitle,String mArticle){
        title = mTitle;
        article = mArticle;
    }

    public String getArticle() {
        return article;
    }

    public String getTitle() {
        return title;
    }
}
