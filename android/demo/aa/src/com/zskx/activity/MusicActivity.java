package com.zskx.activity;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

public class MusicActivity extends Activity {

	private ListView play_list;

	private ExpandableListView whole_music;

	private TextView play_list_btn;

	private TextView whole_music_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.music_layout);

		whole_music = (ExpandableListView) findViewById(R.music.all_music_list);
		whole_music_btn = (TextView) findViewById(R.music.all_music_list_title);

		whole_music.setAdapter(new MusicExpandableListAdapter(this));

		whole_music.setGroupIndicator(null);

		play_list = (ListView) findViewById(R.music.play_music_list);
		play_list_btn = (TextView) findViewById(R.music.play_list_title);

		play_list.setAdapter(new MusicListAdapter(this));

		initListener();
	}

	public class MusicListAdapter extends BaseAdapter {

		private View[] Views = new View[10];
		Random r = new Random();

		public MusicListAdapter(Context context) {

			for (int j = 0; j < 10; j++) {
				View v = View.inflate(context, R.layout.music_list_item, null);
				((TextView) v.findViewById(R.music_list_item.music_name))
						.setText(music_type[r.nextInt(10)] + r.nextInt(10));
				Views[j] = v;
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 10;
		}

		@Override
		public Object getItem(int position) {
			return Views[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return Views[position];
		}

	}

	private void initListener() {
		whole_music_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (whole_music.getVisibility() == View.INVISIBLE) {
					play_list_btn
							.setBackgroundResource(R.drawable.list_label_not_check);
					play_list.setVisibility(View.INVISIBLE);

					whole_music_btn
							.setBackgroundResource(R.drawable.list_label_check);
					whole_music.setVisibility(View.VISIBLE);
				}
			}
		});

		play_list_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (play_list.getVisibility() == View.INVISIBLE) {
					whole_music_btn
							.setBackgroundResource(R.drawable.list_label_not_check);
					whole_music.setVisibility(View.INVISIBLE);

					play_list_btn
							.setBackgroundResource(R.drawable.list_label_check);
					play_list.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	String[] music_type = { "抗压抗挫", "远离抑郁", "不再焦虑", "危机干预", "社会适应", "积极心态",
			"睡眠障碍", "放松减压", "情绪", "冥想音乐", };
	String[] music_type_describe = {
			"生活的苦难压不垮我，我心中的欢乐不是我自己的，我把欢乐注进音乐，为的是让全世界感到欢乐——莫扎特",
			"音乐是生活赋予我们最美好的礼物，它挤走烦恼，指引了我们快乐生活的方向。",
			"音乐的神奇在于它能拨动我们的心弦，让我们在优美的旋律中畅游。",
			"音乐是比一切智慧、一切哲学更高的启示，谁能渗透音乐的意义，便能超脱寻常人无以自拔的苦难",
			"音乐是不假任何外力，直接沁人心脾的最纯的感情的火焰。它是从口吸入的空气，它是生命的血管中流通着的血液",
			"当我听到音乐时，我的心会疾速跳动，充满了生命力，就像是起风时的商船队——梭罗",
			"音乐不是一种单纯的消遣，它或是对于心灵的一种理智上的裨益，或是镇定灵魂的一种抚慰----罗曼·罗兰。",
			"当遇到情绪烦躁或者精神苦闷时，聆听旋律优美、节奏平衡的音乐，会让你感到舒缓很多。",
			"感到烦闷紧张时，你通常会怎么做？到空旷无人处大喊还是找好友倾诉？那么也不妨来听听这样的音乐",
			"音乐是热情洋溢的自由艺术，是室外的艺术，像自然那样无边无际，像风，像天空，像海洋。重要的不在于旋律的开始，而是把它继续下去", };

	/**
	 * 全部音乐的适配器
	 * 
	 * @author demo
	 * 
	 */
	class MusicExpandableListAdapter extends BaseExpandableListAdapter {

		private Context context;

		private View[] groupViews = new View[music_type.length];

		private View[][] childrenViews = new View[music_type.length][5];

		public MusicExpandableListAdapter(Context context) {
			this.context = context;
			initMusicTypeData();
		}

		private void initMusicTypeData() {
			for (int i = 0; i < music_type.length; i++) {
				View v = View.inflate(context, R.layout.music_type_list_item,
						null);
				((TextView) v.findViewById(R.id.music_type_name))
						.setText(music_type[i]);
				((TextView) v.findViewById(R.id.music_type_describe))
						.setText(music_type_describe[i]);
				groupViews[i] = v;
				for (int j = 0; j < 5; j++) {
					View childView = View.inflate(context,
							R.layout.music_list_item, null);
					((TextView) childView
							.findViewById(R.music_list_item.music_name))
							.setText(music_type[i] + j);
					childrenViews[i][j] = childView;
				}

			}

		}

		@Override
		public int getGroupCount() {

			return music_type.length;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO 暫時每一個定爲5個子項
			return 5;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groupViews[groupPosition];
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return childrenViews[groupPosition][childPosition];
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO 所在組的ID拼上在組中的ID
			return Integer.valueOf(groupPosition + "" + childPosition);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {

			return groupViews[groupPosition];
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			return childrenViews[groupPosition][childPosition];
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

}
