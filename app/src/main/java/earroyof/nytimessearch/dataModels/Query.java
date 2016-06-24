package earroyof.nytimessearch.dataModels;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by earroyof on 6/22/16.
 */
public class Query implements Parcelable {
    private Context context; // this will be lost once passed as a parcelable
    public String query;
    public String material;
    public String newsDesk;
    public String section;

    private String[] matArray;
    private String[] newsArray;
    private String[] sectionArray;

    private boolean[] matSelect;
    private boolean[] newsSelect;
    private boolean[] sectionSelect;

    // date fields

    private int day;
    private int month;
    private int year;

    // Order field

    private int order;


    public Query() {
        fieldSetup("");
    }

    public Query(String query) {
        fieldSetup(query);
    }


    public Query(String query, Context context) {
        this.context = context;
        fieldSetup(query);
    }


    public void fieldSetup(String query) {
        this.query = query;
        this.material = "";
        this.newsDesk = "";
        this.section = "";
        this.day = 0;
        this.month = 0;
        this.year = 0;
        this.order = 0;
        try{
            populateLists();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*** Begin Getters and Setters ***/

    public void setQuery(String query) {
        this.query = query;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setNewsDesk(String newsDesk) {
        this.newsDesk = newsDesk;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setMatSelect(boolean[] matSelect) {
        this.matSelect = matSelect;
    }

    public void setNewsSelect(boolean[] newsSelect) {
        this.newsSelect = newsSelect;
    }

    public void setSectionSelect(boolean[] sectionSelect) {
        this.sectionSelect = sectionSelect;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public ArrayList<String> getMatSelected() {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < matSelect.length; i++) {
            if (matSelect[i]) {
                result.add(matArray[i]);
            }
        }
        return result;
    }

    public ArrayList<String> getNewsSelected() {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < newsSelect.length; i++) {
            if (newsSelect[i]) {
                result.add(newsArray[i]);
            }
        }
        return result;
    }

    public ArrayList<String> getSectionSelected() {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < sectionSelect.length; i++) {
            if (sectionSelect[i]) {
                result.add(sectionArray[i]);
            }
        }
        return result;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getQuery() {
        return query;
    }

    public String getMaterial() {
        return material;
    }

    public String getNewsDesk() {
        return newsDesk;
    }

    public String getSection() {
        return section;
    }

    public boolean[] getMatSelect() {
        return matSelect;
    }

    public boolean[] getNewsSelect() {
        return newsSelect;
    }

    public boolean[] getSectionSelect() {
        return sectionSelect;
    }

    public String[] getMatArray() {
        return matArray;
    }

    public String[] getNewsArray() {
        return newsArray;
    }

    public String[] getSectionArray() {
        return sectionArray;
    }

    public String getFormatDay() {
        return String.format("%02d", day);
    }

    public String getFormatMonth() {
        return String.format("%02d", month);
    }

    public String getFormatYear() {
        return "" + year;
    }


    /*** End Getters and Setters ***/

    public Query(Parcel in) {
        query = in.readString();
        material = in.readString();
        newsDesk = in.readString();
        section = in.readString();
        matArray = in.createStringArray();
        newsArray = in.createStringArray();
        sectionArray = in.createStringArray();
        matSelect = in.createBooleanArray();
        newsSelect = in.createBooleanArray();
        sectionSelect = in.createBooleanArray();
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        order = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(query);
        out.writeString(material);
        out.writeString(newsDesk);
        out.writeString(section);
        out.writeStringArray(matArray);
        out.writeStringArray(newsArray);
        out.writeStringArray(sectionArray);
        out.writeBooleanArray(matSelect);
        out.writeBooleanArray(newsSelect);
        out.writeBooleanArray(sectionSelect);
        out.writeInt(year);
        out.writeInt(month);
        out.writeInt(day);
        out.writeInt(order);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Query> CREATOR = new Parcelable.Creator<Query>() {
        @Override
        public Query createFromParcel(Parcel source) {
            return new Query(source);
        }

        @Override
        public Query[] newArray(int size) {
            return new Query[size];
        }
    };


    public void populateLists() throws IOException {
        AssetManager assetManager = context.getAssets();
        List<String> lines = new ArrayList<>();
        List<String> lines2 = new ArrayList<>();
        List<String> lines3 = new ArrayList<>();


        BufferedReader reader = null;
        BufferedReader reader2 = null;
        BufferedReader reader3 = null;
        try {
            reader = new BufferedReader(new InputStreamReader(assetManager.open("lists/MaterialList")));
            reader2 = new BufferedReader(new InputStreamReader(assetManager.open("lists/NewsDeskList")));
            reader3 = new BufferedReader(new InputStreamReader(assetManager.open("lists/SectionNameList")));
            String line = null;
            String line2 = null;
            String line3 = null;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            while ((line2 = reader2.readLine()) != null) {
                lines2.add(line2);
            }
            while ((line3 = reader3.readLine()) != null) {
                lines3.add(line3);
            }
        } finally {
            assert reader != null;
            reader.close();
            assert reader2 != null;
            reader2.close();
            assert reader3 != null;
            reader3.close();
        }
        newsArray = lines2.toArray(new String[0]);
        sectionArray = lines3.toArray(new String[0]);
        matArray = lines.toArray(new String[0]);
        newsSelect = new boolean[newsArray.length];
        sectionSelect = new boolean[sectionArray.length];
        matSelect = new boolean[matArray.length];
    }
}
