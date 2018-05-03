package www.lovingrabbit.com.zeronote.Adapter;

public class Notec {
    String title,count,updatetime;
    int id;
    boolean showId;
    public Notec(String nTitle,String nCount,int id,String updatetime){
        title = nTitle;
        count = nCount;
        this.id = id;
        this.updatetime = updatetime;
    }

    public Notec(String nTitle,String nCount,int id,String updatetime,boolean showId){
        title = nTitle;
        count = nCount;
        this.id = id;
        this.updatetime = updatetime;
        this.showId = showId;
    }

    public void setShowId(boolean showId) {
        this.showId = showId;
    }

    public boolean getShowId() {
        return showId;
    }

    public String getTitle() {
        return title;
    }

    public String getCount() {
        return count;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public int getId() {
        return id;
    }
}
