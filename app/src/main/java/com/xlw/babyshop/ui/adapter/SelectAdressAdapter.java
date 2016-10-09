package com.xlw.babyshop.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xlw.babyshop.dao.AreaDao;
import com.xlw.babyshop.model.AddressDetailModel;
import com.xlw.babyshop.model.AreaModel;
import com.xlw.marsbabyshop.R;

import java.util.Collection;

/**
 * 选择地址适配器
 */
public class SelectAdressAdapter extends ArrayWapperAdapter<AddressDetailModel>{

    private LayoutInflater inflater;
    private AreaDao areaDao;
    private String[] areaDetail;

    public static class AddressManageViewHoler {
        TextView receiver;
        TextView phone;
        TextView ads;
    }

    public SelectAdressAdapter(Context context) {
        super(context);
        areaDao = new AreaDao(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void addAll(Collection<? extends AddressDetailModel> collection) {
        clear();
        super.addAll(collection);
        areaDetail = new String[collection.size()];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        AddressManageViewHoler holer;
        if (convertView == null) {
            view = inflater.inflate(R.layout.address_manage_select_listitem, null);
            holer = new AddressManageViewHoler();
            holer.receiver = (TextView) view.findViewById(R.id.address_listitem_receiver_text);
            holer.phone = (TextView) view.findViewById(R.id.address_listitem_phone_text);
            holer.ads = (TextView) view.findViewById(R.id.address_listitem_ads_text);
            view.setTag(holer);
        } else {
            view = convertView;
            holer = (AddressManageViewHoler) view.getTag();
        }
        AddressDetailModel item = getItem(position);
        holer.receiver.setText(item.getName());
        holer.phone.setText(item.getPhonenumber());
        String string = areaDetail[position];
        if (string == null) {
            StringBuilder builder = new StringBuilder();
            AreaModel area = areaDao.findByCantCode(item.getProvinceid());
            builder.append(area.getValue());
            area = areaDao.findByCantCode(item.getCityid());
            builder.append(area.getValue());
            area = areaDao.findByCantCode(item.getAreaid());
            builder.append(area.getValue());
            builder.append(" " + item.getAreadetail());
            areaDetail[position] = builder.toString();
        }
        holer.ads.setText(areaDetail[position]);
        return view;
    }
}
