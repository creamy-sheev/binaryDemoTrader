package com.example.tajner.binarydemotrader;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class graph_activity extends AppCompatActivity {

    private Spinner valuePicker;
    private String tableName;
    private LinearLayout graphLayout;
    private dbHelper helper;
    private TextView saldo, result, openValue, closeValue, wallet, walletBalance;
    private Button menu, tryAgain;
    private ImageButton checkBtn;
    public float[] values;
    FloatingActionMenu floatingActionMenu;
    FloatingActionButton linearGraphBtn, candlestickGraphBtn;
    private int graphSelected = 0;
    private RadioGroup radioGroup;
    private int spinnerSelectedId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_activity);

        Bundle bundle = getIntent().getExtras();
        tableName = bundle.getString("TableName");

        graphLayout = (LinearLayout) findViewById(R.id.graphLayout);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);


        helper = new dbHelper(this);
        values = helper.getData(tableName);

        addLinearChart(graphLayout);

        initializeFloatingMenu();
        initializeSpinner();
        initializeWalletTextView();

        valuePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        spinnerSelectedId = 0;
                        break;
                    case 1:
                        spinnerSelectedId = 100;
                        break;
                    case 2:
                        spinnerSelectedId = 200;
                        break;
                    case 3:
                        spinnerSelectedId = 300;
                        break;
                    case 4:
                        spinnerSelectedId = 500;
                        break;
                    case 5:
                        spinnerSelectedId = 1000;
                        break;
                    case 6:
                        spinnerSelectedId = 10000;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        linearGraphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(graphSelected == 0){
                    Toast.makeText(getApplicationContext(),"Wykres liniowy jest już wybrany", Toast.LENGTH_LONG).show();
                }
                else {
                    graphLayout.removeAllViews();
                    addLinearChart(graphLayout);
                    graphSelected = 0;
                }
            }
        });

        candlestickGraphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(graphSelected == 1){
                    Toast.makeText(getApplicationContext(),"Wykres świecowy jest już wybrany", Toast.LENGTH_LONG).show();
                }
                else{
                    graphLayout.removeAllViews();
                    addCandleStickChart(graphLayout);
                    graphSelected = 1;
                }
            }
        });


        checkBtn = (ImageButton) findViewById(R.id.checkBtn);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkButton(graphLayout);
            }
        });

    }

    private void checkButton(View view) {

        if(spinnerSelectedId == 0){
            Toast.makeText(getApplicationContext(),"Wybierz kwotę transakcji", Toast.LENGTH_LONG).show();
        }
        else {

            switch (graphSelected) {
                case 0:
                    LineChart lineChart = new LineChart(graph_activity.this);
                    ArrayList<Entry> yValues = new ArrayList<>();

                    int lineDataIndex = 0;

                    for (int i = 0; i < values.length; i = i + 4) {
                        yValues.add(new Entry(lineDataIndex, values[i]));
                        lineDataIndex++;
                    }

                    LineDataSet set1 = new LineDataSet(yValues, "");
                    set1.setFillAlpha(110);
                    set1.setColor(Color.BLACK);
                    set1.setLineWidth(1f);
                    set1.setValueTextSize(14f);
                    set1.setValueTextColor(Color.RED);

                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(set1);

                    LimitLine limitLine = new LimitLine(values[36]);
                    limitLine.setLineColor(Color.BLUE);
                    limitLine.setLineWidth(3f);
                    YAxis leftAxis = lineChart.getAxisLeft();
                    leftAxis.addLimitLine(limitLine);

                    LineData data = new LineData(dataSets);
                    lineChart.setData(data);
                    lineChart.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    lineChart.animateXY(1000, 1000);
                    graphLayout.removeAllViews();
                    graphLayout.addView(lineChart);
                    break;

                case 1:
                    CandleStickChart candleStickChart = new CandleStickChart(graph_activity.this);
                    ArrayList<CandleEntry> yValuesc = new ArrayList<>();


                    int candleDataIndex = 0;

                    for (int i = 0; i < values.length; i = i + 4) {

                        yValuesc.add(new CandleEntry(candleDataIndex, values[i], values[i + 1], values[i + 2], values[i + 3]));
                        candleDataIndex++;
                    }

                    CandleDataSet set = new CandleDataSet(yValuesc, "");
                    set.setValueTextSize(14f);
                    set.setColor(Color.rgb(80, 80, 80));
                    set.setShadowColor(Color.DKGRAY);
                    set.setShadowWidth(0.7f);
                    set.setDecreasingColor(Color.RED);
                    set.setDecreasingPaintStyle(Paint.Style.FILL);
                    set.setIncreasingColor(Color.rgb(122, 242, 84));
                    set.setIncreasingPaintStyle(Paint.Style.FILL);
                    set.setNeutralColor(Color.BLUE);
                    set.setValueTextColor(Color.RED);
                    ArrayList<ICandleDataSet> candleDataSets = new ArrayList<>();
                    candleDataSets.add(set);

                    LimitLine candleLimitLine = new LimitLine(values[36]);
                    candleLimitLine.setLineColor(Color.BLUE);
                    candleLimitLine.setLineWidth(3f);
                    YAxis lAxis = candleStickChart.getAxisLeft();
                    lAxis.addLimitLine(candleLimitLine);

                    CandleData candleData = new CandleData(candleDataSets);

                    candleStickChart.setData(candleData);
                    candleStickChart.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    candleStickChart.animateXY(1000, 1000);
                    graphLayout.removeAllViews();
                    graphLayout.addView(candleStickChart);
            }


            AlertDialog.Builder mBuilder = new AlertDialog.Builder(graph_activity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_box, null);

            result = (TextView) dialogView.findViewById(R.id.rezultat);
            openValue = (TextView) dialogView.findViewById(R.id.cena_poczatkowa);
            closeValue = (TextView) dialogView.findViewById(R.id.cena_realizacji);
            wallet = (TextView) dialogView.findViewById(R.id.portfel);
            walletBalance = (TextView) dialogView.findViewById(R.id.stan_portfela);
            menu = (Button) dialogView.findViewById(R.id.menu);
            tryAgain = (Button) dialogView.findViewById(R.id.ponow);

            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(graph_activity.this, main_menu_activity.class);
                    graph_activity.this.startActivity(i);
                }
            });

            tryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(getIntent());
                }
            });

            result.setText(stringCreator());
            String ov = "Cena początkowa:  " + values[36];
            openValue.setText(ov);
            String cv = "Cena realizacji:  " + values[40];
            closeValue.setText(cv);
            String w = "Portfel:  ";
            boolean b = winOrLose();
            int transactionResult = 0;
            if (b) {
                spinnerSelectedId = (spinnerSelectedId * 12)/10;
                w = w + "+ " + spinnerSelectedId;
                transactionResult = 1;
            } else {
                w = w + "- " + spinnerSelectedId;
            }
            wallet.setText(w);
            helper.updateWallet(spinnerSelectedId, b);

            helper.insertTransactionData(spinnerSelectedId, transactionResult,currentTransactionAsset(tableName));

            String wb;
            int walletBalanceToInt = Integer.parseInt(helper.getWalletBalance());
            if (walletBalanceToInt > 0){
                 wb = "Saldo po transakcji:  " + helper.getWalletBalance();
            }else {
                 wb = "Jesteś bankrutem! Saldo zostanie zresetowane";
                helper.resetUserData();
            }

            walletBalance.setText(wb);

            mBuilder.setView(dialogView);
            AlertDialog dialog = mBuilder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }
    }

    public void initializeFloatingMenu() {
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.floatingMenu);
        linearGraphBtn = (FloatingActionButton) findViewById(R.id.floatingBtn1);
        candlestickGraphBtn = (FloatingActionButton) findViewById(R.id.floatingBtn2);
    }

    public void initializeSpinner() {

        valuePicker = (Spinner) findViewById(R.id.valueSelectionSpinner);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(graph_activity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.SpinnerValues));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        valuePicker.setAdapter(spinnerAdapter);
    }

    public void initializeWalletTextView() {

        saldo = (TextView) findViewById(R.id.walletBalance);
        saldo.setText(helper.getWalletBalance());
    }

    private boolean winOrLose(){
        boolean result = true;
        int btnBuyId = 2131558525;
        int btnSellId = 2131558526;
        int selectedBtnId = radioGroup.getCheckedRadioButtonId();

        if(selectedBtnId == btnBuyId && values[36] < values[40]){
            result = true;
        }else if(selectedBtnId == btnBuyId && values[36] > values[40]){
            result = false;
        }else if(selectedBtnId == btnSellId && values[36] < values[40]){
            result = false;
        }else if(selectedBtnId == btnSellId && values[36] > values[40]){
            result = true;
        }
        return result;
    }

    private String stringCreator() {
        String wynik = "";
        int i = radioGroup.getCheckedRadioButtonId();
        if(i == 2131558525){  //id z radio button
            wynik = wynik + "Kupiłeś";
        }else{
            wynik = wynik + "Sprzedałeś";
        }
        wynik = wynik + " " + "opcje binarne typu góra/dół na kwotę:  " + spinnerSelectedId + " zł";
        return wynik;
    }


    private void addCandleStickChart(View view){
        CandleStickChart candleStickChart = new CandleStickChart(graph_activity.this);
        ArrayList<CandleEntry> yValues = new ArrayList<>();


        int dataIndex = 0;

        for(int i=0; i<values.length-4; i = i+4){

            yValues.add(new CandleEntry(dataIndex,values[i],values[i+1],values[i+2], values[i+3]));
            dataIndex++;
        }

        CandleDataSet set1 = new CandleDataSet(yValues, "");
        set1.setValueTextSize(14f);
        set1.setColor(Color.rgb(80, 80, 80));
        set1.setShadowColor(Color.DKGRAY);
        set1.setShadowWidth(0.7f);
        set1.setDecreasingColor(Color.RED);
        set1.setDecreasingPaintStyle(Paint.Style.FILL);
        set1.setIncreasingColor(Color.rgb(122, 242, 84));
        set1.setIncreasingPaintStyle(Paint.Style.FILL);
        set1.setNeutralColor(Color.BLUE);
        set1.setValueTextColor(Color.RED);
        ArrayList<ICandleDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        CandleData data = new CandleData(dataSets);

        candleStickChart.setData(data);
        candleStickChart.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        candleStickChart.animateXY(1000, 1000);
        graphLayout.addView(candleStickChart);
    }

    private void addLinearChart(View view) {
        LineChart lineChart = new LineChart(graph_activity.this);
        ArrayList<Entry> yValues = new ArrayList<>();

        int dataIndex = 0;

        for(int i = 0; i<values.length-4; i = i+4) {
            yValues.add(new Entry(dataIndex,values[i]));
            dataIndex++;
        }

        LineDataSet set1 = new LineDataSet(yValues,"");
        set1.setFillAlpha(110);
        set1.setColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setValueTextSize(14f);
        set1.setValueTextColor(Color.RED);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        lineChart.animateXY(1000, 1000);

        graphLayout.addView(lineChart);
    }

    private String currentTransactionAsset(String tbName){
        String result = "";
        switch(tbName){
            case "bitcoin_data":
                result = "Bitcoin";
                break;
            case "ethereum_data":
                result = "Ethereum";
                break;
            case "msft_stock_data":
                result = "Microsoft";
                break;
            case "cat_stock_data":
                result = "Caterpillar";
                break;
        }
        return result;
    }
}
