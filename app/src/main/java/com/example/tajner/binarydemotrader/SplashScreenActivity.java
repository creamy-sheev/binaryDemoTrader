package com.example.tajner.binarydemotrader;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView iv;
    private TextView tv;
    private final String urlBitcoin = "https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_DAILY&symbol=BTC&market=USD&apikey=O6HF3VVDES1LVSV1";
    private final String urlEthereum = "https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_DAILY&symbol=ETH&market=USD&apikey=O6HF3VVDES1LVSV1";
    private final String urlMsft = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=MSFT&apikey=O6HF3VVDES1LVSV1";
    private final String urlCat = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=CAT&apikey=O6HF3VVDES1LVSV1";
    private final String bitcoinTable = "bitcoin_data";
    private final String ethereumTable = "ethereum_data";
    private final String msftTable = "msft_stock_data";
    private final String catTable = "cat_stock_data";
    private ProgressBar progressBar;
    private dbHelper helper;
    private TextView loadingTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        iv = (ImageView) findViewById(R.id.iv);
        tv = (TextView) findViewById(R.id.tv);
        Animation splashScreenTransition = AnimationUtils.loadAnimation(this,R.anim.splash_screen_transition);
        iv.startAnimation(splashScreenTransition);
        tv.startAnimation(splashScreenTransition);
        helper = new dbHelper(SplashScreenActivity.this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        loadingTV = (TextView) findViewById(R.id.loadingText);

        final fetchData dataBtc = new fetchData(SplashScreenActivity.this,bitcoinTable,urlBitcoin,0);
        final fetchData dataEth = new fetchData(SplashScreenActivity.this,ethereumTable,urlEthereum,0);
        final fetchData dataMsft = new fetchData(SplashScreenActivity.this,msftTable,urlMsft,1);
        final fetchData dataCat = new fetchData(SplashScreenActivity.this,catTable,urlCat,1);

        final Intent i = new Intent(this,main_menu_activity.class);
        Thread timer = new Thread() {
            public void run() {
                try {
                    if(!helper.checkIfTableExists()){

                        dataBtc.execute();
                        dataEth.execute();
                        dataMsft.execute();
                        dataCat.execute();
                        sleep(120000);
                    }else{
                        progressBar.setVisibility(View.INVISIBLE);
                        loadingTV.setVisibility(View.INVISIBLE);
                        sleep(6000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();

    }
}
