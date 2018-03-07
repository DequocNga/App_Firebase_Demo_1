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
import russia.com.app_firebase_demo_1.model.Track;

/**
 * Created by VLADIMIR PUTIN on 3/6/2018.
 */

public class TrackList extends ArrayAdapter<Track> {

    Context context;
    int resource;
    ArrayList<Track> trackArrayList = new ArrayList<>();

    public TrackList(@NonNull Context context, int resource, @NonNull ArrayList<Track> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.trackArrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_artist_list, parent, false);
            viewHolder.textViewName = convertView.findViewById(R.id.textViewName);
            viewHolder.textViewRating = convertView.findViewById(R.id.textViewGenre);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Track track = trackArrayList.get(position);
        viewHolder.textViewName.setText(track.getTrackName());
        viewHolder.textViewRating.setText(track.getRating() + "");
        return convertView;
    }

    public class ViewHolder {
        TextView textViewName;
        TextView textViewRating;
    }
}
