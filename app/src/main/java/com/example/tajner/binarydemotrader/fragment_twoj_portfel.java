package com.example.tajner.binarydemotrader;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_twoj_portfel extends android.support.v4.app.Fragment {


    private dbHelper helper;
    String[] date, dt, tm, val, asset, res;
    private Button back, confirm;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        helper = new dbHelper(context);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        date = helper.getTrDate();
        dt = new String[date.length];
        tm = new String[date.length];
        String[] splitter;
        for(int i = 0;i<date.length;i++){
            splitter = date[i].split(" ");
            dt[i] = splitter[0];
            tm[i] = splitter[1];
        }
        val = helper.getTrValue();
        asset = helper.getTrAsset();
        res = helper.getTrRes();


        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_twoj_portfel, container, false);

        if(date.length != 0){
            TextView walletDescriptionTV = (TextView) view.findViewById(R.id.walletDescriptionTV);
            walletDescriptionTV.setText(helper.getWalletBalance());
        }else{
            String defaultText = "Nie zajerestrowano transakcji do wyświetlenia.\nRozpocznij grę w zakładce handel";
            TextView walletDescriptionTV = (TextView) view.findViewById(R.id.walletDescriptionTV);
            TextView generalDescriptionTV = (TextView) view.findViewById(R.id.generalDescriptionTV);

            walletDescriptionTV.setText(" ");
            generalDescriptionTV.setText(defaultText);
        }

        Button resetBtn = (Button) view.findViewById(R.id.resetDataBtn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = layoutInflater.inflate(R.layout.reset_data_dialog_box, null);

                back = (Button) dialogView.findViewById(R.id.powrotBtn);
                confirm = (Button) dialogView.findViewById(R.id.takBtn);

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), main_menu_activity.class);
                        startActivity(i);
                    }
                });

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        helper.resetUserData();
                        Intent i = new Intent(getActivity(), main_menu_activity.class);
                        startActivity(i);
                    }
                });
                mBuilder.setView(dialogView);
                AlertDialog dialog = mBuilder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

        ListView listView = (ListView) view.findViewById(R.id.walletDescriptionLV);
        ListViewAdapter listViewAdapter = new ListViewAdapter();
        listView.setAdapter(listViewAdapter);
        return view;


    }

    private class ListViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return date.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){

                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.transaction_list_view_item, parent, false);

                LinearLayout parentLayout = (LinearLayout) convertView.findViewById(R.id.parentLayotu);
                TextView dateTV = (TextView) convertView.findViewById(R.id.date);
                TextView time = (TextView) convertView.findViewById(R.id.time);
                TextView assetTV = (TextView) convertView.findViewById(R.id.assetTV);
                TextView result = (TextView) convertView.findViewById(R.id.resultTV);
                TextView value = (TextView) convertView.findViewById(R.id.valueTV);

                dateTV.setText(dt[position]);
                time.setText(tm[position]);
                assetTV.setText(asset[position]);
                value.setText(val[position]);

                int i = Integer.parseInt(res[position]);
                switch (i){
                    case 0:
                        result.setText("-");
                        parentLayout.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
                        break;
                    case 1:
                        result.setText("+");
                        parentLayout.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
                }

            }

            return convertView;
        }
    }



}
