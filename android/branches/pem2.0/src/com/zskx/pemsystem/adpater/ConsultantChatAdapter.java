package com.zskx.pemsystem.adpater;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zskx.pemsystem.R;
import com.zskx.pemsystem.util.ImageLoader;
import com.zskx.pemsystem.util.ImageLoader.ImageCallback;
import com.zskx.testreport.ChatMsgEntity;

public class ConsultantChatAdapter extends BaseAdapter {
	public static final int ConsultantChatAdapter_IMG = 498961;
	
	Context context;
	ArrayList<ChatMsgEntity> list_msg;
	private ImageLoader il;
	private Handler handler;
	
	public ConsultantChatAdapter(Context context,
			ArrayList<ChatMsgEntity> list_msg, ImageLoader il, Handler handler) {
		super();
		this.context = context;
		this.list_msg = list_msg;
		this.il = new ImageLoader(context.getClass().getSimpleName());
		this.handler = handler;
	}
	
	

	@Override
	public int getCount() {
		return list_msg.size();
	}

	@Override
	public Object getItem(int position) {
		return list_msg.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChatMsgEntity entity = list_msg.get(position);  
        boolean isComMsg = entity.getMsgType();  
        boolean isSendSuc = entity.isSendSuc();  
        final Holder holder ;  
        
        if (convertView == null) {  
            if (isComMsg) {  
                convertView = View.inflate(context, R.layout.consult_chat_item_left, null);

            } else {  
            	convertView = View.inflate(context, R.layout.consult_chat_item_right, null);
            	
            }  
  
            holder = new Holder();  
            holder.message = (TextView) convertView.findViewById(R.id.chat_msg_content);  
            holder.isComMsg = isComMsg;  
            holder.isSendSuc = isSendSuc;
            holder.imageView.setOnClickListener(btn_click);
        } else {  
        	holder = (Holder) convertView.getTag();  
        }  
        holder.date.setText(entity.getDate());  
        holder.message.setText(entity.getMessage());  
        if(isComMsg){
        	holder.imageView.setImageDrawable(null);
    		String img_url = ( (ChatMsgEntity)this.list_msg.get(position))
    				.getConsultantImage();
    		holder.imageView.setTag(img_url);
    		if ((img_url != null) && (!img_url.equals(""))) {
    			il.loadDrawable(context.getClass().getSimpleName(),img_url, new ImageCallback() {

    				@Override
    				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
    					if (((String) holder.imageView.getTag()).equals(imageUrl)){
    						holder.imageView.setImageDrawable(imageDrawable);
    					}
    					
    				}
    			});
    		}
        }
        convertView.setTag(holder);
        return convertView;  
	}
	
	View.OnClickListener btn_click = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.chat_img:
				handler.sendEmptyMessage(ConsultantChatAdapter_IMG);
				break;
			}
		}
	};

	
	public static final class Holder {
		public ImageView imageView;
		public TextView date;
		public TextView message;
		public boolean isComMsg ;
		public boolean isSendSuc = true;
	}
}
