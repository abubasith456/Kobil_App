package com.example.kobilapp.viewModel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.kobilapp.AstListener;
import com.example.kobilapp.MainActivity;
import com.example.kobilapp.R;
import com.example.kobilapp.SdkListener;
import com.example.kobilapp.databinding.SideMenuFragmentBinding;
import com.example.kobilapp.fragment.ActivationFragment;
import com.example.kobilapp.fragment.ChangePinFragment;
import com.example.kobilapp.fragment.InitFragment;
import com.example.kobilapp.fragment.LoginFragment;
import com.example.kobilapp.fragment.SideMenuFragment;
import com.example.kobilapp.fragment.UsersFragment;
import com.example.kobilapp.model.Status;
import com.example.kobilapp.utils.SharedPreference;
import com.kobil.midapp.ast.api.AstLogListener;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

import java.util.Objects;

public class SideMenuViewModel extends AndroidViewModel {
    private static final int RESULT_OK = 0;
    @SuppressLint("StaticFieldLeak")
    private FragmentActivity sideMenuFragment;
    private String from;
    public ObservableField<Boolean> selectLanguageVisibility = new ObservableField<>();
    public ObservableField<Boolean> changePinVisibility = new ObservableField<>();
    public ObservableField<Boolean> addUserVisibility = new ObservableField<>();
    public ObservableField<Boolean> deleteUserVisibility = new ObservableField<>();
    public ObservableField<Boolean> reportVisibility = new ObservableField<>();
    public ObservableField<Boolean> contactDeskVisibility = new ObservableField<>();
    public ObservableField<Boolean> logoutVisibility = new ObservableField<>();
    private ProgressDialog progressdialog;

    public SideMenuViewModel(@NonNull Application application) {
        super(application);
        AstListener.getInstance().setAstSdk(getApplication());
        deleteUserVisibility.set(false);
        reportVisibility.set(true);
        contactDeskVisibility.set(true);
        selectLanguageVisibility.set(true);
        logoutVisibility.set(false);
    }

    public void getFragment(FragmentActivity sideMenuFragment) {
        this.sideMenuFragment = sideMenuFragment;
        from = SharedPreference.getInstance().getValue(sideMenuFragment, "from");
        if (from.equals("LoginFragment")) {
            changePinVisibility.set(false);
            addUserVisibility.set(true);
            deleteUserVisibility.set(true);
        } else if (from.equals("ActivationFragment")) {
            changePinVisibility.set(false);
            addUserVisibility.set(false);
            deleteUserVisibility.set(false);
        } else if (from.equals("DashboardFragment")) {
            changePinVisibility.set(true);
            deleteUserVisibility.set(false);
            logoutVisibility.set(true);
        } else if (from.equals("UsersFragment")) {
            changePinVisibility.set(false);
            addUserVisibility.set(true);
        } else if (from.equals("AdapterToLoginFragment")) {
            changePinVisibility.set(false);
            addUserVisibility.set(true);
            deleteUserVisibility.set(true);
        }
    }

    public void closeMenu(View view) {
        try {
            sideMenuFragment.getSupportFragmentManager().popBackStackImmediate();
        } catch (Exception e) {
            Log.e("Error=>", e.getMessage());
        }
    }

    public void selectLanguageClick(View view) {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(sideMenuFragment);
            alertDialog.setTitle("Select Language");
            String[] items = {"English", "German"};
            int checkedItem = 0;
            alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            Toast.makeText(sideMenuFragment, "Clicked on English", Toast.LENGTH_LONG).show();
                            break;
                        case 1:
                            Toast.makeText(sideMenuFragment, "Clicked on German", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alert = alertDialog.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        } catch (Exception e) {
            Log.e("Error=>", e.getMessage());
        }
    }

    public void onAddUserClick(View view) {
        try {
            Log.e("Clicked", "onClicked");
            sideMenuFragment.getSupportFragmentManager().popBackStack();
            Fragment fragment = new ActivationFragment();
            FragmentTransaction transaction = sideMenuFragment.getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } catch (Exception e) {
            Log.e("Error=>", e.getMessage());
        }
    }

    public void onDeleteUserClick(View view) {
        try {
            Log.e("Clicked", "onDeleteUserClick");
            String username = SharedPreference.getInstance().getValue(sideMenuFragment, "userId");
            AlertDialog.Builder builder = new AlertDialog.Builder(sideMenuFragment);
            builder.setMessage("Are you sure do you want to delete " + username + " user?");
            builder.setCancelable(false);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    showProcessBar("Deleting user....");
                    deleteUser(username);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        progressdialog.dismiss();
                        sideMenuFragment.getSupportFragmentManager().popBackStack();
                        Fragment fragment = new InitFragment();
                        FragmentTransaction transaction = sideMenuFragment.getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                        transaction.replace(R.id.frameLayoutForSideMenu, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        SharedPreference.getInstance().saveValue(sideMenuFragment, "from", "DashboardFragment");
                    }, 3000);
                }
            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } catch (Exception e) {
            Log.e("Error=>", e.getMessage());
        }
    }

    public void onChangePinClick(View view) {
        try {
            Fragment fragment = new ChangePinFragment();
            FragmentTransaction transaction = sideMenuFragment.getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            transaction.replace(R.id.frameLayoutForSideMenu, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } catch (Exception e) {
            Log.e("Error=>", e.getMessage());
        }
    }

    public void onContactDeskClick(View view) {
        try {


//            int index = (text.toString()).indexOf(url);
//            spans.setSpan(clickSpan, index, url.length() + index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            TextView messageText = alertDialog.findViewById(android.R.id.message);
//            messageText.setGravity(Gravity.CENTER);

//            final SpannableString s = new SpannableString("Please send any questions to email@fake.com");
//
////added a TextView
//            final TextView tx1 = new TextView(sideMenuFragment.getContext());
//            tx1.setText(s);
//            tx1.setAutoLinkMask(RESULT_OK);
//            tx1.setMovementMethod(LinkMovementMethod.getInstance());
//            Linkify.addLinks(s, Linkify.EMAIL_ADDRESSES);
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(sideMenuFragment.getContext());
//            builder.setTitle("Contact Support Desk");
//            builder.setMessage("Mobile Number:");
//            builder.setCancelable(false);
//            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
//                }
//            });
//            builder.setView(tx1);
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//            TextView messageText = alertDialog.findViewById(android.R.id.message);
//            messageText.setGravity(Gravity.CENTER);
//            messageText.setMovementMethod(LinkMovementMethod.getInstance());

            // Linkify the message
//            final SpannableString fromMalta = new SpannableString("9585909514");// msg should have url to enable clicking
//            final SpannableString fromAbroad = new SpannableString("7411606412");
//            Linkify.addLinks(fromMalta, Linkify.PHONE_NUMBERS);
//            Linkify.addLinks(fromAbroad, Linkify.PHONE_NUMBERS);

//            final TextView message = new TextView(sideMenuFragment.getContext());
//            // i.e.: R.string.dialog_message =>
//            // "Test this dialog following the link to dtmilano.blogspot.com"
//            final SpannableString s =
//                    new SpannableString("9585909514");
//            Linkify.addLinks(s, Linkify.PHONE_NUMBERS);
//            message.setText(s);
//            message.setMovementMethod(LinkMovementMethod.getInstance());

//            AlertDialog.Builder builder = new AlertDialog.Builder(sideMenuFragment.getContext());
//            builder.setView(R.layout.alert_dialog);
//
//            final AlertDialog alertDialog = new AlertDialog.Builder(sideMenuFragment.getContext())
//                    .setPositiveButton(android.R.string.ok, null)
//                    .setMessage("The support team is available on weekends from 08.00-17.00 Kindly call: \n" +
//                            "from Malta: \n" + fromMalta +
//                            "\n from abroad: \n" + fromAbroad)
//                    .setView(message)
//                    .create();
//
//
//            alertDialog.show();
//
//            // Make the textview clickable. Must be called after show()
//            ((TextView) alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());


            final SpannableString fromMalta = new SpannableString("9585909514");// msg should have url to enable clicking
            final SpannableString fromAbroad = new SpannableString("7411606412");
            Linkify.addLinks(fromMalta, Linkify.PHONE_NUMBERS);
            Linkify.addLinks(fromAbroad, Linkify.PHONE_NUMBERS);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(sideMenuFragment);
            LayoutInflater inflater = sideMenuFragment.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_dialog, null);
            dialogBuilder.setView(dialogView);

            TextView title = dialogView.findViewById(R.id.alertTitle);
            title.setText("Contact Support Desk");
            TextView message = dialogView.findViewById(R.id.message);
            message.setText("The support team is available on weekends from 08.00-17.00 Kindly call:");
            TextView message1 = dialogView.findViewById(R.id.message1);
            message1.setText("from Malta:");
            TextView message2 = dialogView.findViewById(R.id.message2);
            message2.setTextColor(Color.BLACK);
            message2.setText(fromMalta);
            message2.setMovementMethod(LinkMovementMethod.getInstance());
            TextView message3 = dialogView.findViewById(R.id.message3);
            message3.setText("from abroad");
            TextView message4 = dialogView.findViewById(R.id.message4);
            message4.setText(fromAbroad);
            message4.setMovementMethod(LinkMovementMethod.getInstance());
            dialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            Log.e("Error=>", e.getMessage());
        }
    }

    public void onReportClick(View view) {
        try {
            String deviceName = SharedPreference.getInstance().getValue(sideMenuFragment, "deviceName");
            String deviceOSVersion = SharedPreference.getInstance().getValue(sideMenuFragment, "deviceOSVersion");
            AlertDialog.Builder builder = new AlertDialog.Builder(sideMenuFragment);
            builder.setTitle("Report");
            builder.setMessage(Html.fromHtml("<b>" + " Device Name: " + "</b>" + deviceName +
                    "<b>" + "<br>" + "Device OS: " + "</b>" + deviceOSVersion));
            builder.setCancelable(false);
            builder.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("message/rfc822");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"appcenter@kobil.com"});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Report | Device issues");
                        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("Hi Team," + "<br>" + "<br>" + "<b>" + "Device Name: " + "</b>" + deviceName
                                + "<br>" + "<b>" + "Device OS version: " + "</b>" + deviceOSVersion + "<br>" + "<br>"
                                + ".......Type your issues here......." + "<br>" + "<br>" + " Thank you."));
                        try {
                            sideMenuFragment.startActivity(Intent.createChooser(intent, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(sideMenuFragment, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("Error=>", e.getMessage());
                    }

                }
            }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                }
            });
            alertDialog.show();
            TextView messageText = alertDialog.findViewById(android.R.id.message);
            messageText.setGravity(Gravity.CENTER);
        } catch (Exception e) {
            Log.e("Error=>", e.getMessage());
        }
    }

    public void onLogoutClick(View view) {
        try {
            showProcessBar("Logging out....");
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                AstListener.getInstance().getAstSdk().exit(0);
                progressdialog.dismiss();
                sideMenuFragment.getSupportFragmentManager().popBackStackImmediate();

                AstListener.getInstance().initSdk();
                if (Status.getInstance().getList().size()>=2){
                    Fragment usersFragment = new UsersFragment();
                    FragmentTransaction fragmentTransaction = sideMenuFragment.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                    fragmentTransaction.replace(R.id.frameLayoutLoginFragmentContainer, usersFragment);
                    fragmentTransaction.commit();
                    SharedPreference.getInstance().saveValue(getApplication(), "from", "UsersFragment");
                }else {
                    Fragment fragment = new LoginFragment();
                    FragmentTransaction transaction = sideMenuFragment.getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                    transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
                    transaction.commit();
                    SharedPreference.getInstance().saveValue(getApplication(), "from", "LoginFragment");
                }
            }, 3000);
        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }
    }

    private void deleteUser(String user_id) {
        try {
            SdkListener listener = new SdkListener();
            AstSdk sdk = AstSdkFactory.getSdk(sideMenuFragment, listener);
            sdk.doDeactivate(user_id);
            Log.e("deleteUser:", "User deleted " + user_id);
        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }
    }

    private void showProcessBar(String message) {
        progressdialog = new ProgressDialog(sideMenuFragment, R.style.MyAlertDialogStyle);
        progressdialog.setMessage(message);
        progressdialog.setCancelable(false);
        progressdialog.show();
    }

}