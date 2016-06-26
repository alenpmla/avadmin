package in.codme.axisvoiceadmin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alen on 26-Jun-16.
 */
public class ProfileFragment extends Fragment {

    String namesr,addresssr,phonenumbersr,imageurlsr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_profile,container,false);
        setHasOptionsMenu(true);
        FirebaseUser usernew = FirebaseAuth.getInstance().getCurrentUser();
      final  TextView name=(TextView)rootView.findViewById(R.id.name);
        final   TextView emailid=(TextView)rootView.findViewById(R.id.emailid);
        final  TextView phonenumber=(TextView)rootView.findViewById(R.id.phonenumber);
        final  TextView address=(TextView)rootView.findViewById(R.id.address);

        if (usernew != null) {
            // User is signed in


            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users");


            Map<String,Object> map=new HashMap<>();
            map.put("name",usernew.getDisplayName());
            map.put("email",usernew.getEmail());
            myRef.child(usernew.getUid()).updateChildren(map);




            DatabaseReference newref = myRef.child(usernew.getUid());

            newref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserClass userClass=dataSnapshot.getValue(UserClass.class);

                    name.setText(userClass.getDisplay_name());
                    emailid.setText(userClass.getEmail());
                    phonenumber.setText(userClass.getPhone_number());
                    address.setText(userClass.getAddress());

                    namesr=userClass.getDisplay_name();
                    addresssr=userClass.getAddress();
                    phonenumbersr=userClass.getPhone_number();
                    imageurlsr=userClass.getImg_url();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        } else {
            // No user is signed in
        }


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_fragment, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_profile) {
            Bundle bundle=new Bundle();
            bundle.putString("name",namesr);
            bundle.putString("addresssr",addresssr);
            bundle.putString("phonenumbersr",phonenumbersr);
            bundle.putString("imageurlsr",imageurlsr);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            EditProfileFragment editProfileFragment = new EditProfileFragment();
            fragmentTransaction.replace(R.id.container, editProfileFragment);
            fragmentTransaction.addToBackStack(null);
            editProfileFragment.setArguments(bundle);
            fragmentTransaction.commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
