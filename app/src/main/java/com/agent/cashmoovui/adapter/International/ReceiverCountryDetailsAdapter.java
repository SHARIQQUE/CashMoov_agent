package com.agent.cashmoovui.adapter.International;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.agent.cashmoovui.R;
import com.agent.cashmoovui.remmetience.international.ReceiverCountryModal;
import com.agent.cashmoovui.remmetience.international.SenderCountryModal;

import java.util.ArrayList;


public class ReceiverCountryDetailsAdapter extends BaseAdapter {

    Context context;
    ArrayList<ReceiverCountryModal> list;
    LayoutInflater inflter;

    public ReceiverCountryDetailsAdapter(Context applicationContext, ArrayList<ReceiverCountryModal> listData) {

        this.context = applicationContext;
        this.list = listData;

//        this.preferences = preferences;
//        Collections.sort(arrayList);
//        if(arrayList.size()>0) {
//            HashSet<ReChargeAmountModel> onlineMart = new HashSet<ReChargeAmountModel>(arrayList);
//            Iterator<ReChargeAmountModel> strIterator = onlineMart.iterator();
//            while (strIterator.hasNext()) {
//                System.out.println(strIterator.next());
//            }
//            TreeSet<ReChargeAmountModel> ts = new TreeSet<ReChargeAmountModel>(onlineMart);
//            Iterator<ReChargeAmountModel> ascSorting = ts.iterator();
//            while (ascSorting.hasNext()) {
//                System.out.println(ascSorting.next());
//            }
//        }
//                new ArrayList<ReChargeAmountModel>();
//        Collections.sort(arrayList, new StudentDateComparator());

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflter.inflate(R.layout.row_item_simple, null);
        TextView names = (TextView) view.findViewById(R.id.textViewName);

        names.setText(list.get(i).getCountryName_receiver());

        return view;
    }
}

