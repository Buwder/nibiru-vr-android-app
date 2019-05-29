package com.zskx.pemsystem.adpater;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.zskx.net.response.MusicDetailEntity;
import com.zskx.pemsystem.R;

public class PlayingListAdapter extends BaseAdapter {
	private String TAG = "PlayingListAdapter";
	LayoutInflater layoutInflater;
	Context mcontext;
	private ArrayList<MusicDetailEntity> mPlaylist;
	
	private int current = 1000; //当前播放歌曲
	private int status = 0; //当前播放状态
	
	public PlayingListAdapter(Context context , ArrayList<MusicDetailEntity> list){
		layoutInflater = LayoutInflater.from(context);
		mcontext = context;
		mPlaylist = list;
	}
	
	@Override
	public void notifyDataSetChanged() {
		Log.i(TAG, "notifyDataSetChanged::" + mPlaylist.size());
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mPlaylist.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	PlayingListHolder holder;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView != null) holder = (PlayingListHolder) convertView.getTag();
		else{
			convertView = layoutInflater.inflate(R.layout.music_playing_item, null);
			holder = new PlayingListHolder();
			holder.layout = (RelativeLayout) convertView.findViewById(R.id.music_playinglist_item);
			holder.textView_num = (TextView) convertView.findViewById(R.id.music_playing_num);
			holder.imageView = (ImageView) convertView.findViewById(R.id.music_playing_title_image);
			holder.textView_title = (TextView) convertView.findViewById(R.id.music_playing_item_1);
			holder.deleteButton = (Button) convertView.findViewById(R.id.music_playing_button_delete_1);
		}
		holder.textView_num.setText(position + 1 + "");
		holder.textView_title.setText(mPlaylist.get(position).getMusicTitle());
		holder.deleteButton.setOnClickListener(new BtnClick());
		if(position == current) {
			if(status == 0 ) status = 1;
			setPlayingsong(status , holder );
		}
		else setPlayingsong(0 , holder );
		convertView.setTag(holder);
		holder.deleteButton.setTag(mPlaylist.get(position));
		
		
		return convertView;
	}
	
	class BtnClick implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			MusicDetailEntity pb = (MusicDetailEntity) v.getTag();
			Toast.makeText(mcontext, mcontext.getResources().getString(R.string.music_delete_success), Toast.LENGTH_SHORT).show();
			Log.i(TAG, "Delete  " +  pb.getMusicTitle() + "   " + mPlaylist.indexOf(pb));
			if(mPlaylist.size() > 0 && pb != null ) sendMyBroadcast("NEXT", pb.getMusicId());
			mPlaylist.remove(v.getTag());
			PlayingListAdapter.this.notifyDataSetChanged();
		}}
	

	/**
	 * �����Զ���㲥
	 * @param conponent ����intentFilter�Ĺؼ���
	 * @param msg ���÷��͵Ĳ�����String����
	 */
	public void sendMyBroadcast( String msg ,String getMusicId){
		Intent _intent = new Intent();
		_intent.setAction(msg);
		_intent.putExtra("getMusicId", getMusicId);
		mcontext.sendBroadcast(_intent);
	}
	
	public final class PlayingListHolder{
		public RelativeLayout layout;
		public TextView textView_num;
		public ImageView imageView;
		public TextView textView_title;
		public Button deleteButton;
	}
	
	/*************************************************************/
	
	public void changeSong(int pos , int song_status) {
		current = pos;
		status = song_status;
		PlayingListAdapter.this.notifyDataSetChanged();
	}
	/**
	 * 显示播放列表正在播放的歌曲
	 * @param status
	 * @param holder
	 */
	private void setPlayingsong(int status, PlayingListHolder holder ){
		if(status == 0){
			Log.i(TAG, "setPlayingsong>>stop!");
			holder.imageView.setVisibility(View.GONE);
			holder.textView_num.setVisibility(View.VISIBLE);
			holder.textView_title.setTextColor(mcontext.getResources().getColor(R.color.black));
			RelativeLayout.LayoutParams params_1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params_1.addRule(RelativeLayout.RIGHT_OF, R.id.music_playing_num);
			params_1.addRule(RelativeLayout.CENTER_VERTICAL);
			params_1.setMargins(15, 0, 0, 0);
			holder.layout.removeView(holder.textView_title);
			holder.layout.addView(holder.textView_title, params_1);
		}else if(status == 1){ 
			Log.i(TAG, "setPlayingsong>>playing!");
			holder.imageView.setImageDrawable(mcontext.getResources().getDrawable(
					R.drawable.music_playinglist_play));
			holder.imageView.setVisibility(View.VISIBLE);
			holder.textView_num.setVisibility(View.GONE);
			holder.textView_title.setTextColor(mcontext.getResources().getColor(R.color.blue));
			RelativeLayout.LayoutParams params_2 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params_2.addRule(RelativeLayout.RIGHT_OF, R.id.music_playing_title_image);
			params_2.addRule(RelativeLayout.CENTER_VERTICAL);
			params_2.setMargins(18, 0, 15, 0);
			holder.layout.removeView(holder.textView_title);
			holder.layout.addView(holder.textView_title, params_2);
		}else if(status == 2){
			Log.i(TAG, "setPlayingsong>>pause!");
			holder.imageView.setImageDrawable(mcontext.getResources().getDrawable(
					R.drawable.music_playinglist_pause));
			holder.imageView.setVisibility(View.VISIBLE);
			holder.textView_num.setVisibility(View.GONE);
			holder.textView_title.setTextColor(mcontext.getResources().getColor(R.color.blue));
			RelativeLayout.LayoutParams params_2 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params_2.addRule(RelativeLayout.RIGHT_OF, R.id.music_playing_title_image);
			params_2.addRule(RelativeLayout.CENTER_VERTICAL);
			params_2.setMargins(18, 0, 15, 0);
			holder.layout.removeView(holder.textView_title);
			holder.layout.addView(holder.textView_title, params_2);
		}
	}
}
