package org.netzd.autenticaciondip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FirebaseAuth aut = FirebaseAuth.getInstance(); //Instanciamos el objeto firebaseauth

        if(aut.getCurrentUser()!=null){
            AuthUI.getInstance().signOut(this).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Cerro Sesion", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(MainActivity.this, "Hubo un problema", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
            );
        }else{
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.PhoneBuilder().build(),
                    new AuthUI.IdpConfig.FacebookBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build())).build(),RC_SIGN_IN);
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode==RESULT_OK){
                System.out.println("responseEEASDASDASD->"+response.getIdpToken());
                System.out.println("responseEEASDASDASD->"+response.getEmail());
                System.out.println("responseEEASDASDASD->"+response.getErrorCode());
                System.out.println("responseEEASDASDASD->"+response.getProviderType());
                System.out.println("responseEEASDASDASD->"+response.getIdpSecret());
                Toast.makeText(MainActivity.this, "Bienvenido "+response.getEmail(), Toast.LENGTH_SHORT).show();
                //finish();
                return;
            }else {
                if(response==null){
                    Toast.makeText(MainActivity.this, "Hubo un problema "+response.getEmail(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(response.getErrorCode()== ErrorCodes.NO_NETWORK){
                    Toast.makeText(MainActivity.this, "Hubo un problema, no hay red"+response.getEmail(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(response.getErrorCode()== ErrorCodes.UNKNOWN_ERROR){
                    Toast.makeText(MainActivity.this, "Hubo un problema, desconocido "+response.getEmail(), Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
