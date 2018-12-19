package de.addcreations.mbsoundboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

//kopier lieber nichts von dem import hier mit sondern mach das bei jeder sache einzelnt mit der glühbrine. wenn du
//die funktionen bzw den code unten kopierst und einfügst, wirst du gefragt ob du die sachen importieren willst, da
//kannst du einfach ja drücken



public class Main extends AppCompatActivity {

    public static AlertDialog error, thanks;
    private static InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileAds.initialize(this, "ca-app-pub-3625885863390256~8830798286");

        // hier wo du die lange nummer siehst, muss die hin mit dem gekringelten bindestrich. '~'
        // musst aber genau die id nehmen die in diesem dialog feld angezeigt
        // wird wenn du einen anzeigeblock bei admob erstellt hast


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3625885863390256/9529206203");
        // hier muss die id mit dem schrägstrich rein. wird auch im selben dialog feld angezeigt wenn du
        // auf der admob seite einen anzeigeblock erstellst



        //das hier ist zur veranschaulichung. wenn man auf den button drückt, kommt die werbung. kannst natürlich
        //auch bei jedem anderen element das machen was einen onclick listener hat. in deinem fall dieses textview
        //wo man bei der info seite drauf drücken kann
        Button button = (Button) findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAd(getApplicationContext());
                //kann sein dass getApplicationContext() bei dir nicht angezeigt wird, kannst dann einfach
                //getContext() rein schreiben oder NameDerKlasse.this also in diesem fall hier wäre das 'Main.this'

                //bei jedem objekt wo eine werbung beim drauf klicken geladen werden soll musst du einfach nur
                //die funktion showAd( ... ); aufrufen, der rest wird von selbst erledigt. kannst auch von einer
                //anderen activity aus die werbung laden. Dafür musst du in der anderen activity bzw klasse einfach das aufrufen:

                //Main.showAd( ... );
            }
        });
    }

    public static void showAd(final Context context){
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Werbespot wird geladen ...");
        dialog.setTitle("Werbespot");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                dialog.dismiss();
                mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                dialog.dismiss();
                if (errorCode == 3){
                    AdsError(context, "Momentan sind keine Werbespots verfügbar. Vielleicht klappt es später. Danke trotzdem!");
                }else if (errorCode == 2){
                    AdsError(context, "Es besteht leider keine Internetverbindung oder sie reicht nicht aus");
                }else if (errorCode == 1){
                    AdsError(context, "Es ist ein interner Fehler unterlaufen. Bitte melde ihn dem Entwickler umgehend mit dem Fehlercode AD_01!");
                }else if (errorCode == 0){
                    AdsError(context, "Der Server für die Werbespots hat nicht auf die Anfrage reagiert. Probiere es später erneut. Danke trotzdem!");
                }
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                thankYouForSupport(context);
            }
        });

    }

    public static void thankYouForSupport(Context context){
        thanks =  new AlertDialog.Builder(context)
                .setTitle("Vielen Dank")
                .setMessage(R.string.for_your_support)
                .setCancelable(true)
                .setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    public static void AdsError(Context context, String message){
        error =  new AlertDialog.Builder(context)
                .setTitle("Fehler")
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}
