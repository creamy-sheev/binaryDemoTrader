package com.example.tajner.binarydemotrader;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_handel extends android.support.v4.app.Fragment {

    int[] images = {R.drawable.bitcoin_logo, R.drawable.ethereum_logo, R.drawable.msft_logo, R.drawable.cat_logo};
    String[] names = {"Bitcoin", "Ethereum", "Microsoft", "Caterpillar"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_handel, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        ListViewAdapter listViewAdapter = new ListViewAdapter();
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tableName = "";
                Intent i = new Intent(getActivity(),graph_activity.class);
                switch (position) {
                    case 0:
                        tableName = "bitcoin_data";
                        break;
                    case 1:
                        tableName = "ethereum_data";
                        break;
                    case 2:
                        tableName = "msft_stock_data";
                        break;
                    case 3:
                        tableName = "cat_stock_data";
                }
                i.putExtra("TableName", tableName);
                startActivity(i);
            }
        });
        return view;

    }

    private class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return images.length;
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
            if (convertView == null) {

                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_view_item, parent, false);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView2);
                TextView textView = (TextView) convertView.findViewById(R.id.textView_description);

                imageView.setImageResource(images[position]);
                textView.setText(names[position]);


            }

            return convertView;
        }

    }
}