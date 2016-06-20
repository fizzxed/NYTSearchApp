package earroyof.nytimessearch;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by earroyof on 6/20/16.
 */
public class ArticleArrayAdapter extends ArrayAdapter<Article> {


    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the data item for the current position
        Article article = this.getItem(position);

        // check to see if the existing view is being reused or recycled
        // if not using recycled view then inflate the layout
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
        }

        // find the image view inside it
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);

        // clear out the recycled image just in case
        imageView.setImageResource(0);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(article.getHeadline());

        // populate the thumbnail image
        // remote download image in Background

        String thumbnail = article.getThumbnail();

        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).fit().into(imageView);
        }
        return convertView;
    }
}
