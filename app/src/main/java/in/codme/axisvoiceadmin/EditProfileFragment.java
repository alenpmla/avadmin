package in.codme.axisvoiceadmin;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Alen on 26-Jun-16.
 */
public class EditProfileFragment extends Fragment {
    String namesr,addresssr,phonenumbersr,imageurlsr;
    final int SELECT_PHOTO=202;
       CircleImageView profile_image;
       EditText displayname,phonenumber,address;
    Boolean imgchanged=false;
    Bitmap imgtouploadbm;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_edit_profile,container,false);
        setHasOptionsMenu(true);


         displayname=(EditText)rootView.findViewById(R.id.displayname);
           phonenumber=(EditText)rootView.findViewById(R.id.phonenumber);
            address=(EditText)rootView.findViewById(R.id.address);
        Bundle bundle = getArguments();
        namesr=   bundle.getString("name","");
        addresssr=bundle.getString("addresssr","");
        phonenumbersr=bundle.getString("phonenumbersr","");
        imageurlsr=bundle.getString("imageurlsr","");

        displayname.setText(namesr);
        phonenumber.setText(phonenumbersr);
        address.setText(addresssr);

          profile_image=(CircleImageView)rootView.findViewById(R.id.profile_image);

        try{
            Picasso.with(getActivity().getApplicationContext()).load(imageurlsr).into(profile_image);
        }
    catch (NullPointerException ne){
        ne.printStackTrace();
    }
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getActivity(), profile_image);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.edit_profile_pic, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(getActivity(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);//
                        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_PHOTO);

                        return true;
                    }
                });

                popup.show();//showing popup menu



}
        });

        Button savebutton=(Button)rootView.findViewById(R.id.saveeditedcontent);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                ProfileFragment profileFragment = new ProfileFragment();
                fragmentTransaction.replace(R.id.container, profileFragment);

                fragmentTransaction.commit();
                if(imgchanged){
                    uploadpic();
                }
                else {
                    FirebaseUser usernew = FirebaseAuth.getInstance().getCurrentUser();
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("users");
                    DatabaseReference newref = myRef.child(usernew.getUid());
                    Map<String,Object>map=new HashMap<String, Object>();

                    map.put("display_name",displayname.getText().toString());
                    map.put("phone_number",phonenumber.getText().toString());
                    map.put("address",address.getText().toString());
                    newref.updateChildren(map);
                }
            }
        });


        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_fragment, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            ProfileFragment profileFragment = new ProfileFragment();
            fragmentTransaction.replace(R.id.container, profileFragment);

            fragmentTransaction.commit();



            if(imgchanged){
                uploadpic();
            }
            else {
                FirebaseUser usernew = FirebaseAuth.getInstance().getCurrentUser();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users");
                DatabaseReference newref = myRef.child(usernew.getUid());
                Map<String,Object>map=new HashMap<String, Object>();

                map.put("display_name",displayname.getText().toString());
                map.put("phone_number",phonenumber.getText().toString());
                map.put("address",address.getText().toString());
                newref.updateChildren(map);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PHOTO)
                onSelectFromGalleryResult(data);

        }
    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {



                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                imgchanged=true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            imgchanged=false;
        }
        profile_image.setImageBitmap(bm);
        imgtouploadbm=bm;
    }

public void uploadpic(){
    profile_image.setImageBitmap(imgtouploadbm);
    FirebaseUser usernew = FirebaseAuth.getInstance().getCurrentUser();

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://project-3363810000149996090.appspot.com");
    StorageReference users = storageRef.child("users").child(usernew.getUid());

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    imgtouploadbm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    byte[] dataimg = baos.toByteArray();

    UploadTask uploadTask = users.putBytes(dataimg);
    uploadTask.addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
            // Handle unsuccessful uploads
        }
    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
            Uri downloadUrl = taskSnapshot.getDownloadUrl();
            Toast.makeText(getActivity(),"upload success",Toast.LENGTH_SHORT).show();
            try{
                Picasso.with(getActivity().getApplicationContext()).load(downloadUrl.toString()).into(profile_image);
            }catch (NullPointerException ne){
                ne.printStackTrace();
            }

            FirebaseUser usernew = FirebaseAuth.getInstance().getCurrentUser();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users");
            DatabaseReference newref = myRef.child(usernew.getUid());
            Map<String,Object>map=new HashMap<String, Object>();

            map.put("display_name",displayname.getText().toString());
            map.put("phone_number",phonenumber.getText().toString());
            map.put("address",address.getText().toString());
            map.put("img_url",downloadUrl.toString());
            newref.updateChildren(map);
        }
    });


}
}
