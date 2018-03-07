package russia.com.app_firebase_demo_1.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import russia.com.app_firebase_demo_1.R;
import russia.com.app_firebase_demo_1.adapter.TrackList;
import russia.com.app_firebase_demo_1.model.Track;

/**
 * Created by VLADIMIR PUTIN on 3/5/2018.
 */

public class ArtistActivity extends AppCompatActivity {

    EditText editTextTrackName;
    Button buttonAddTrack;
    SeekBar seekBarRating;
    TextView textViewRating;
    TextView textViewArtist;
    ListView listViewTracks;
    ArrayList<Track> trackArrayList = new ArrayList<>();
    DatabaseReference databaseReference;
    TrackList trackListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        Intent intent = getIntent();
        /*thia line is important
        * this time we are not getting the reference of a direct node
        * but inside the node track we are creating a new child with the artist id
        * and inside that node we will store all the tracks with unique ids*/
        databaseReference = FirebaseDatabase.getInstance().getReference("track").child(intent.getStringExtra(MainActivity.ARTIST_ID));
        connectView();
        textViewArtist.setText(intent.getStringExtra(MainActivity.ARTIST_NAME));
        seekBarRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textViewRating.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttonAddTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTrack();
            }
        });
    }

    private void saveTrack() {
        String trackName = editTextTrackName.getText().toString().trim();
        int rating = seekBarRating.getProgress();
        if (!TextUtils.isEmpty(trackName)) {
            String id = databaseReference.push().getKey();
            Track track = new Track(id, trackName, rating);
            /*truyen ca doi tuong len database can cu theo id*/
            databaseReference.child(id).setValue(track);
            Toast.makeText(this, "Track saved", Toast.LENGTH_SHORT).show();
            editTextTrackName.setText("");
        } else {
            Toast.makeText(this, "Please enter track me", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trackArrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Track track = postSnapshot.getValue(Track.class);
                    trackArrayList.add(track);
                }
                trackListAdapter = new TrackList(ArtistActivity.this, R.layout.layout_artist_list, trackArrayList);
                listViewTracks.setAdapter(trackListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void connectView() {
        editTextTrackName = findViewById(R.id.editTextName);
        buttonAddTrack = findViewById(R.id.buttonAddTrack);
        seekBarRating = findViewById(R.id.seekBarRating);
        textViewRating = findViewById(R.id.textViewRating);
        textViewArtist = findViewById(R.id.textViewArtist);
        listViewTracks = findViewById(R.id.listViewTracks);
    }


}
