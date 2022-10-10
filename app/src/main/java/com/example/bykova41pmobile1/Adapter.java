package com.example.bykova41pmobile1;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Adapter extends BaseAdapter {
    protected Context mContext;
    String Img = "";
    private ArrayList<MI> ListMI;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
    }

    public Adapter(Context mContext, List<MI> ListMI) {
        this.mContext = mContext;
        this.maskList = ListMI;
    }

    List<MI> maskList;


    @Override
    public int getCount() {
        return maskList.size();
    }

    @Override
    public Object getItem(int i) {
        return maskList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return maskList.get(i).getID();
    }


    public static Bitmap loadContactPhoto(ContentResolver cr, long id, Context context) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        if (input == null) {
            Resources res = context.getResources();
            return BitmapFactory.decodeResource(res, R.drawable.ph);
        }
        return BitmapFactory.decodeStream(input);
    }

    private Bitmap getUserImage(String encodedImg) {

        if (encodedImg != null && !encodedImg.equals("null")) {
            byte[] bytes = Base64.decode(encodedImg, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return BitmapFactory.decodeResource(Adapter.this.mContext.getResources(), R.drawable.ph);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.item_mi, null);

        TextView Name_of_MI = v.findViewById(R.id.Name);
        TextView Manufacturers = v.findViewById(R.id.Manufacturers);
        TextView Manufacturer_country = v.findViewById(R.id.Manufacturer_country);
        TextView Price_MI = v.findViewById(R.id.Price_MI);
        ImageView Image = v.findViewById(R.id.imageView);

        MI mask = maskList.get(position);
        Name_of_MI.setText(mask.getName_of_MI());
        Manufacturers.setText(mask.getManufacturers());
        Manufacturer_country.setText(mask.getManufacturer_country());
        Price_MI.setText(Integer.toString(mask.getPrice_MI()));
        Image.setImageBitmap(getUserImage(mask.getImage()));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, up.class);
                intent.putExtra("MI", mask);
                mContext.startActivity(intent);
            }
        });
        return v;

    }
}
