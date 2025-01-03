package com.tarea3.pokemonappmvs;



import android.content.Intent;
import android.os.Bundle;
import com.firebase.ui.auth.AuthUI;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;




public class LogoutConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_confirmation);

        showLogoutDialog();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.logout_confirmation_message))
                .setCancelable(false)
                .setPositiveButton(R.string.yes, (dialog, id) -> logout())
                .setNegativeButton(R.string.no, (dialog, id) -> finish())
                .show();
    }

    private void logout() {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(task ->{
                        goToLogin();
        });
    }

    private void goToLogin(){
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
        finish();

    }
}