package com.smsverification;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

  private static final int REQ_USER_CONSENT = 200;
  SmsBroadcastReceiver smsBroadcastReceiver;
  String string;
  TextView tvOTP1, tvOTP2, tvOTP3, tvOTP4, requestOTP, proceed;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    tvOTP1 = findViewById(R.id.textViewOTP1);
    tvOTP2 = findViewById(R.id.textViewOTP2);
    tvOTP3 = findViewById(R.id.textViewOTP3);
    tvOTP4 = findViewById(R.id.textViewOTP4);
    requestOTP = findViewById(R.id.requestOTP);
    requestOTP.setOnClickListener(view -> {

    });
    proceed = findViewById(R.id.proceedOTP);
    proceed.setOnClickListener(view -> {

      Toast.makeText(this, "Hello World?", Toast.LENGTH_SHORT).show();
    });

    startSmsUserConsent();

  }


  private void startSmsUserConsent() {
    SmsRetrieverClient client = SmsRetriever.getClient(this);
    //We can add sender phone number or leave it blank
    // I'm adding null here
    client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
      @Override
      public void onSuccess(Void aVoid) {

      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Toast.makeText(getApplicationContext(), "Oops It Looks Like Something Went Wrong", Toast.LENGTH_LONG).show();
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQ_USER_CONSENT) {
      if ((resultCode == RESULT_OK) && (data != null)) {
        //That gives all message to us.
        // We need to get the code from inside with regex
        String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();


        getOtpFromMessage(message);
      }
    }
  }

  private void getOtpFromMessage(String message) {
    // This will match any 6 digit number in the message
    Pattern pattern = Pattern.compile("(|^)\\d{4}");
    Matcher matcher = pattern.matcher(message);
    if (matcher.find()) {

      string = matcher.group(0);
      showToast();
    }
  }

  private void registerBroadcastReceiver() {
    smsBroadcastReceiver = new SmsBroadcastReceiver();
    smsBroadcastReceiver.smsBroadcastReceiverListener =
            new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
              @Override
              public void onSuccess(Intent intent) {
                startActivityForResult(intent, REQ_USER_CONSENT);
              }

              @Override
              public void onFailure() {

              }
            };
    IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
    registerReceiver(smsBroadcastReceiver, intentFilter);
  }

  @Override
  protected void onStart() {
    super.onStart();
    registerBroadcastReceiver();
  }

  @Override
  protected void onStop() {
    super.onStop();
    unregisterReceiver(smsBroadcastReceiver);
  }

  private void showToast() {

    try {
      if (string != null){

        char string1 = string.charAt(0);
        char string2 = string.charAt(1);
        char string3 = string.charAt(2);
        char string4 = string.charAt(3);

        tvOTP1.setText(String.valueOf(string1));

        View vOTPSlow1 = tvOTP2;
        vOTPSlow1.postDelayed(new Runnable() {
          @Override
          public void run() {
            tvOTP2.setText(String.valueOf(string2));
          }
        }, 1000);

        View vOTPSlow2 = tvOTP2;
        vOTPSlow2.postDelayed(new Runnable() {
          @Override
          public void run() {
            tvOTP3.setText(String.valueOf(string3));
          }
        }, 2000);

        View vOTPSlow3 = tvOTP2;
        vOTPSlow3.postDelayed(new Runnable() {
          @Override
          public void run() {
            tvOTP4.setText(String.valueOf(string4));
          }
        }, 3000);

        View vProSlow = tvOTP2;
        vProSlow.postDelayed(new Runnable() {
          @Override
          public void run() {
            proceed.setVisibility(View.VISIBLE);
          }
        }, 3500);


      } else {

        View vOTPSlow = tvOTP2;
        vOTPSlow.postDelayed(new Runnable() {
          @Override
          public void run() {
            requestOTP.setVisibility(View.VISIBLE);
          }
        }, 500);
      }
    } catch (Exception e){
      e.printStackTrace();
    }

  }
}