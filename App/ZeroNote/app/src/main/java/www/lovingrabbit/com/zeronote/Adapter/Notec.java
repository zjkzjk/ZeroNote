package www.lovingrabbit.com.zeronote.Adapter;

public class Notec {
    String title,count,updatetime;
    int id;
    public Notec(String nTitle,String nCount,int id,String updatetime){
        title = nTitle;
        count = nCount;
        this.id = id;
        this.updatetime = updatetime;
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
