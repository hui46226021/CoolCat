package com.zshgif.laugh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.zshgif.laugh.R;
import com.zshgif.laugh.bean.GifitemBean;
import com.zshgif.laugh.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GIF图片适配器
 * Created by zhush on 2016/5/15
 */
public class GifPaictureAdapter extends ArrayAdapter<GifitemBean> {
    private int resourceId;
    private Context context;
    /**
     * 内部图片集合
     */
    private List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
    public GifPaictureAdapter(Context context, int resource) {
        super(context, resource);
    }

    public GifPaictureAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }



    public GifPaictureAdapter(Context context, int resource, List<GifitemBean> objects) {
        super(context, resource, objects);
        this.context =context;
        resourceId = resource;
    }





    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GifPaictureHodler holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    resourceId, null);
            if (convertView == null) {
                LogUtils.e("234", "convertView为空");
            }

            holder = new GifPaictureHodler(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GifPaictureHodler) convertView.getTag();
        }
        String [] from ={"image"};
        int [] to = {R.id.image};
        getData();
        holder.gridView.setAdapter(new SimpleAdapter(context, data_list, R.layout.gridview, from, to));

        return convertView;
    }
    /**
     * 适配缓存
     */
    public class GifPaictureHodler {
        public GridView gridView;
        public GifPaictureHodler(View view) {
            gridView = (GridView) view.findViewById(R.id.gridView);
        }
    }

    public List<Map<String, Object>> getData(){
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<9;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", R.drawable.loadimage);
            data_list.add(map);
        }

        return data_list;
    }
}
