package com.zshgif.laugh.wechat.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.zshgif.laugh.R;
import com.zshgif.laugh.model.PictureBean;
import com.zshgif.laugh.wechat.DemoHelper;
import com.zshgif.laugh.wechat.bean.PhoneConteacts;
import com.zshgif.laugh.wechat.ui.PhoneContactsActivity;

import java.util.List;

/**
 * Created by zhush on 2016/6/15.
 */
public class PhoneContactsAdapter extends ArrayAdapter<PhoneConteacts> {
    List<PhoneConteacts> lsit;
    PhoneContactsActivity context;
    int resource;
    public PhoneContactsAdapter(Context context, int resource, List<PhoneConteacts> objects) {
        super(context, resource, objects);
        lsit = objects;
        this.context = (PhoneContactsActivity) context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhoneConteacts phoneConteacts= lsit.get(position);
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            holder.phone = (TextView) convertView.findViewById(R.id.phone);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.button = (Button) convertView.findViewById(R.id.button);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.phone.setText(phoneConteacts.getPhone());
        holder.name.setText(phoneConteacts.getName());
        holder.button.setText(phoneConteacts.isState()?"添加好友":"邀请好友");
        holder.button.setBackgroundColor(phoneConteacts.isState()? Color.parseColor("#11cd6e"):Color.parseColor("#a9b7b7"));
        final String phone = phoneConteacts.getPhone();
        final boolean state = phoneConteacts.isState();
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state){
                    context.addContact(phone);
                }else {
                    context.shareShow();
                }



            }
        });
        return convertView;
    }

    class Holder {

        TextView phone;
        TextView name;
        Button  button;
    }


}
