package russia.com.app_firebase_demo_1.main;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import russia.com.app_firebase_demo_1.R;
import russia.com.app_firebase_demo_1.adapter.ArtistAdapter;
import russia.com.app_firebase_demo_1.model.Artist;

public class MainActivity extends AppCompatActivity {

    public static final String ARTIST_NAME = "artistname";
    public static final String ARTIST_ID = "artistid";

    EditText editTextName;
    Spinner spinnerGenre;
    Button buttonAddArtist;
    ListView listViewArtist;
    ArrayList<Artist> artistArrayList = new ArrayList<>();
    DatabaseReference databaseReference;
    ArtistAdapter artistAdapter;

    /*creating activity layouts
    * we need two activities. One is to add Artists and other one is to add Tracks
    * to database. we will use MainActivity.java and activity_main.xml for Artist.
    * But you need to create one more activity for Tracks. So I have created ArtistActivity
    * and activity_artist.xml*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*getting the reference of artist node*/
        databaseReference = FirebaseDatabase.getInstance().getReference("artists");
        connectView();
        buttonAddArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addArtist();
            }
        });
        listViewArtist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Artist artist = artistArrayList.get(i);
                Intent intent = new Intent(getApplicationContext(), ArtistActivity.class);
                intent.putExtra(ARTIST_ID, artist.getArtistID());
                intent.putExtra(ARTIST_NAME, artist.getArtistName());
                startActivity(intent);
            }
        });
        listViewArtist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Artist artist = artistArrayList.get(i);
                showUpdateDeleteDialog(artist.getArtistID(), artist.getArtistName());
                return true;
            }
        });
    }

    private void addArtist() {
        String name = editTextName.getText().toString().trim() + "";
        String genre = spinnerGenre.getSelectedItem().toString() + "";

        if (!TextUtils.isEmpty(name)) {
            // getting a unique id using push().getKey() method
            // it will create a unique id and we will use it as the Primary Key for our Artist
            String id = databaseReference.push().getKey();
            //creating an Artist object
            Artist artist = new Artist(id, name, genre);
            //saving the Artist
            databaseReference.child(id).setValue(artist);
            editTextName.setText("");
            Toast.makeText(this, "Artist added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
        }
    }

    private void connectView() {
        editTextName = findViewById(R.id.editTextName);
        spinnerGenre = findViewById(R.id.spinnerGenres);
        buttonAddArtist = findViewById(R.id.buttonAddArtist);
        listViewArtist = findViewById(R.id.listViewArtists);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //get Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous artist list
                artistArrayList.clear();

                //thuc hien vong lap cho tat ca cac node
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Artist artist = postSnapshot.getValue(Artist.class);
                    //adding artist to the list
                    artistArrayList.add(artist);
                }
                //creating adapter
                artistAdapter = new ArtistAdapter(MainActivity.this, R.layout.layout_artist_list, artistArrayList);
                listViewArtist.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDeleteDialog(final String artistID, String artistName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);
        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        final Spinner spinnerGenre = dialogView.findViewById(R.id.spinnerGenres);
        final Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdateArtist);
        final Button buttonDelete = dialogView.findViewById(R.id.buttonDeleteArtist);
        dialogBuilder.setTitle(artistName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String genre = spinnerGenre.getSelectedItem().toString();
                if (!TextUtils.isEmpty(name)) {
                    updateArtist(artistID, name, genre);
                    b.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteArtist(artistID);
                b.dismiss();
            }
        });
    }

    private boolean deleteArtist(String artistID) {
        databaseReference = FirebaseDatabase.getInstance().getReference("artists").child(artistID);
        databaseReference.removeValue();
        DatabaseReference drTracks = FirebaseDatabase.getInstance().getReference("tracks").child(artistID);
        drTracks.removeValue();
        Toast.makeText(this, "Artist Deleted", Toast.LENGTH_SHORT).show();
        return true;
    }

    private boolean updateArtist(String id, String name, String genre) {
        //getting the specified artist reference
        databaseReference = FirebaseDatabase.getInstance().getReference("artists").child(id);
        //updating artist
        Artist artist = new Artist(id, name, genre);
        databaseReference.setValue(artist);
        Toast.makeText(this, "Artist Updated", Toast.LENGTH_SHORT).show();
        return true;
    }
}
