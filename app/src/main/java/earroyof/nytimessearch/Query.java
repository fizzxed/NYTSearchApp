package earroyof.nytimessearch;

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

    String[] matArray;
    String[] newsArray;
    String[] sectionArray;

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
        try{
            populateLists();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public String[] getMatArray() {
        return matArray;
    }

    public String[] getNewsArray() {
        return newsArray;
    }

    public String[] getSectionArray() {
        return sectionArray;
    }

    public Query(Parcel in) {
        query = in.readString();
        material = in.readString();
        newsDesk = in.readString();
        section = in.readString();
        matArray = in.createStringArray();
        newsArray = in.createStringArray();
        sectionArray = in.createStringArray();
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
    }
}
