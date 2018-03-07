package russia.com.app_firebase_demo_1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import russia.com.app_firebase_demo_1.R;
import russia.com.app_firebase_demo_1.model.Artist;

/**
 * Created by VLADIMIR PUTIN on 3/5/2018.
 */

public class ArtistAdapter extends ArrayAdapter {

    Context context;
    int resource;
    ArrayList<Artist> artistArrayList = new ArrayList<>();

    public ArtistAdapter(@NonNull Context context, int resource, @NonNull ArrayList objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.artistArrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_artist_list, parent, false);
            viewHolder.textNameArtist = convertView.findViewById(R.id.textViewName);
            viewHolder.textViewGenre = convertView.findViewById(R.id.textViewGenre);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Artist artist = artistArrayList.get(position);
        viewHolder.textNameArtist.setText(artist.getArtistName());
        viewHolder.textViewGenre.setText(artist.getArtistGenre());
        return convertView;
    }

    public class ViewHolder {
        TextView textNameArtist;
        TextView textViewGenre;
    }

}
