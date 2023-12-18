package com.example.bibliotech.datos.firestore;

import static android.content.ContentValues.TAG;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bibliotech.R;
import com.example.bibliotech.datos.User;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
public class FireBaseActions {

    static public FirebaseAuth auth;
    static public FirebaseUser user;

    static public FirebaseFirestore db;

    static public StorageReference ref;

    
    public FireBaseActions() {
        // Inicialitza l'instància d'autenticació de Firebase
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        ref = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Contract(" -> new")
    public static FireBaseActions getInstance() {
        return new FireBaseActions();
    }

    public static FirebaseUser getCurrentUser() {
        if(user == null){
            return null;
        }else return user = auth.getCurrentUser();
    }

    public static void refreshUser(){
        user = auth.getCurrentUser();
    }

    public static void signOut() {
        auth.signOut();
    }

    public static <TResult> Task signInWithCredential(AuthCredential credential) {
        return auth.signInWithCredential(credential);
    }

    public  static void configureGoogleSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("92021108070-o38qkvb463362i6hpuimna25slbq795d.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
       // GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    public  static void createUser(@NonNull User User, Context Context, Activity Activity, Class Class) {

        auth.createUserWithEmailAndPassword(User.email, User.password)
                .addOnCompleteListener(Activity, (OnCompleteListener<AuthResult>) task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(User.completeName)
                                .build();
                        //Update Profile with displayName
                        assert user != null;
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        verificaSiUsuarioValidado(Context,Class,Activity);
                                        Log.d(TAG, "User profile updated.");
                                    }
                                });

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(Context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                    }

                });

    }


    //Veirfies if user is validated and goes on
    // Class is the activity you want to go
    //Activity is the activity you are in
    public static void verificaSiUsuarioValidado(Context Context, Class Class, Activity Activity) {
        if (auth.getCurrentUser() != null) {
            // User is signed in, navigate to the main activity
            Intent i = new Intent(Context, Class);
            refreshUser();
            User updateUser = FireBaseActions.getUserAuth(Context);
            UserFirestore dbu = new UserFirestore();
            dbu.add(updateUser);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(Context,i,null);
            Activity.finish();
        }
    }
    @Nullable
    // CREATE USER FORM AUTH
    //USER NOT COMPLETE
    public static User getUserAuth(@NonNull Context Context) {
        if (auth.getCurrentUser() != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            String id = user.getUid();

            //Put image on Storage
            uploadImageUri(photoUrl,id,Context.getResources().getString(R.string.pfp_image_path),Context);

            return new User(email, name ,photoUrl,id);

        }
        Toast.makeText(Context,"La toma de datos ha fallado",Toast.LENGTH_SHORT);
        return null;
    }

    private static void uploadImageUri (@NonNull Uri imageUri, @NonNull String imageName, @Nullable String imagePath, @NonNull Context context){

        new Thread(() -> {
            try {
                //Convert URI to Stream
                URL url = null;
                InputStream stream = null;
                try {
                    url = new URL(imageUri.toString());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                URLConnection connection = null;
                try {
                    connection = url.openConnection();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    stream = connection.getInputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                //Create Image Storage Reference


                //we defualt if string is not in input
                StorageReference imgRef;
                if (imagePath != null || !imagePath.isEmpty()) {
                    imgRef = ref.child(imagePath + "/" + imageName);
                } else {
                    // Handle the case where imagePath is null
                    // You might want to use a default path or take some other action
                    Resources res = context.getResources();
                    imgRef = ref.child(res.getString(R.string.images_dafult) + "/" + imageName); // Change "defaultPath" to your preferred default
                }
                // Upload the image using the InputStream
                UploadTask uploadTask = imgRef.putStream(stream);

                // Register observers to listen for when the upload is successful or fails
                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    // You can get the download URL if needed
                    imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // The download URL is available here
                        String downloadUrl = uri.toString();

                        Log.d("FireBaseActions UploadImage", "Image uploaded successfully. Download URL: " + downloadUrl);
                    });
                }).addOnFailureListener(e -> {
                    // Handle unsuccessful uploads
                    Log.d("FireBaseActions UploadImage", "Error uploading image: " + e.getMessage());
                });
            } catch (Exception e) {
                // Handle exceptions appropriately
                Log.e("FireBaseActions UploadImage", "Error: " + e.getMessage());
            }
        }).start();

    }



    public static void updateUsername (String username) {
            getInstance();
            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username)
                    .build();
        assert user != null;
        user.updateProfile(userProfileChangeRequest)
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Log.d(TAG, "User profile updated.");
                    }
                });
    }

    public static void updateEmail(String email,Context context) {

        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                        } else {
                            Toast.makeText(context,"Ha habido un error interno. Inténtelo más tarde",Toast.LENGTH_SHORT);
                        }
                    }
                });
    }
    public static String getUserId() {
        getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            return null;
        }
    }

}

