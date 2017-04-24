package css.cis3334.fishlocatorfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

     FirebaseAuth mAuth;
     FirebaseAuth.AuthStateListener mAuthListener;
    Button buttonAdd, buttonDetails, buttonDelete;          // two button widgets
    ListView listViewFish;                                  // listview to display all the fish in the database
    ArrayAdapter<Fish> fishAdapter;
    List<Fish> fishList;
    FishFirebaseData fishDataSource;
    DatabaseReference myFishDbRef;
    int positionSelected;
    Fish fishSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed out
                    Log.d("CSS3334", "onAuthStateChanged - User NOT is signed in");
                    Intent signInIntent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(signInIntent);
                }
            }
        };

        setupFirebaseDataChange();
        setupListView();
        setupAddButton();
        setupDetailButton();
        setupDeleteButton();
    }

    private void setupFirebaseDataChange() {
        fishDataSource = new FishFirebaseData();
        myFishDbRef = fishDataSource.open();
        myFishDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("CIS3334", "Starting onDataChange()");        // debugging log
                fishList = fishDataSource.getAllFish(dataSnapshot);
                // Instantiate a custom adapter for displaying each fish
                fishAdapter = new FishAdapter(MainActivity.this, android.R.layout.simple_list_item_single_choice, fishList);
                // Apply the adapter to the list
                listViewFish.setAdapter(fishAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("CIS3334", "onCancelled: ");
            }
        });
    }
    /**
     * onStart is attaching the listener for the authorization on this method.
     */
    @Override
    public void onStart() { //method header of no type and returns void
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener); //FirebaseAuth object to access addAuthStateListener method
        // to add a authorization state listener using FirebaseAuth.AuthStateListener
        // as the parameter for the method call
    }

    /**
     * onStop removes the attached listener for the authorization
     */
    @Override
    public void onStop() { //method header of no type and returns void
        super.onStop();
        if (mAuthListener != null) { //As long as  FirebaseAuth.AuthStateListener object is not null...
            mAuth.removeAuthStateListener(mAuthListener); //FirebaseAuth object to access removeAuthStateListener method
            // to remove a authorization state listener using FirebaseAuth.AuthStateListener
            // as the parameter for the method call
        }
    }
    private void setupListView() {
        listViewFish = (ListView) findViewById(R.id.ListViewFish);
        listViewFish.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View parent,
                                    int position, long id) {
                positionSelected = position;
                Log.d("MAIN", "Fish selected at position " + positionSelected);
            }
        });
    }

    private void setupAddButton() {
        // Set up the button to add a new fish using a seperate activity
        buttonAdd = (Button) findViewById(R.id.buttonAddFish);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Start up the add fish activity with an intent
                Intent detailActIntent = new Intent(view.getContext(), AddFishActivity.class);
                finish();
                startActivity(detailActIntent);
            }
        });
    }

    private void setupDetailButton() {
        // Set up the button to display details on one fish using a seperate activity
        buttonDetails = (Button) findViewById(R.id.buttonDetails);
        buttonDetails.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("MAIN", "onClick for Details");
                Intent detailActIntent = new Intent(view.getContext(), DetailActivity.class);
                detailActIntent.putExtra("Fish", fishList.get(positionSelected));
                finish();
                startActivity(detailActIntent);
            }
        });
    }

    private void setupDeleteButton() {
        // Set up the button to display details on one fish using a seperate activity
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("MAIN", "onClick for Delete");
                Log.d("MAIN", "Delete at position " + positionSelected);
                fishDataSource.deleteFish(fishList.get(positionSelected));
                fishAdapter.remove( fishList.get(positionSelected) );
                fishAdapter.notifyDataSetChanged();
            }
        });
    }
}
