package com.zskx.pemsystem.adpater;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zskx.net.response.TestTypeEntity;
import com.zskx.pemsystem.R;
import com.zskx.pemsystem.util.ImageLoader;
import com.zskx.pemsystem.util.ImageLoader.ImageCallback;
import com.zskx.pemsystem.util.UtilService;

public class TestTypeAdapter extends BaseAdapter {
	private static String TAG = "TestTypeAdapter";
	public static final int TestTypeAdapter_check = 581655;
	public static final int TestTypeAdapter_report = 23985;
	public static final int TestTypeAdapter_test = 48565;
	private ArrayList<TestTypeEntity> arrayList;
	private ImageLoader il;
	int i = 0;

	private Context context1;
	private Handler handler;
	private LayoutInflater inflater;

	public TestTypeAdapter(ArrayList<TestTypeEntity> paramArrayList,
			Context paramContext, Handler paramHandler
			) {
		this.arrayList = paramArrayList;
		this.context1 = paramContext;
		this.inflater = LayoutInflater.from(paramContext);
		this.handler = paramHandler;
		il = new ImageLoader(paramContext.getClass().getSimpleName());
	}

	private void set_buttons_gone(Holder paramHolder) {
		paramHolder.btn_test.setVisibility(View.GONE);
		paramHolder.btn_check.setVisibility(View.GONE);
		paramHolder.btn_reported.setVisibility(View.GONE);
	}

	private void set_pos(Holder paramHolder, int pos) {
		paramHolder.btn_test.setTag(Integer.valueOf(pos));
		paramHolder.btn_check.setTag(Integer.valueOf(pos));
		paramHolder.btn_reported.setTag(Integer.valueOf(pos));
	}

	public int getCount() {
		return this.arrayList.size();
	}

	public Object getItem(int position) {
		return Integer.valueOf(position);
	}

	public long getItemId(int paramInt) {
		return paramInt;
	}

	public View getView(int position, View convertView, ViewGroup paramViewGroup) {
		final Holder holder;
		if (convertView != null) {
			holder = (Holder) convertView.getTag();
		} else {

			System.out.println("holder::" + ++i);
			LinearLayout linearLayout = (LinearLayout) this.inflater.inflate(
					R.layout.test_type_item, null);
			holder = new Holder();
			holder.imageView = ((ImageView) linearLayout
					.findViewById(R.id.test_type_imageview));
			holder.txtTitle = ((TextView) linearLayout
					.findViewById(R.id.test_type_title));
			holder.txtIntro = ((TextView) linearLayout
					.findViewById(R.id.test_type_intro));
			holder.btn_test = ((Button) linearLayout
					.findViewById(R.id.test_type_test));
			holder.btn_check = ((Button) linearLayout
					.findViewById(R.id.test_type_check));
			holder.btn_reported = ((Button) linearLayout
					.findViewById(R.id.test_type_reported));
			holder.imageView.setImageDrawable(this.context1.getResources()
					.getDrawable(R.drawable.test_item_img));

			holder.btn_test.setOnClickListener(this.clickListener);
			holder.btn_check.setOnClickListener(this.clickListener);
			holder.btn_reported.setOnClickListener(this.clickListener);

			set_buttons_gone(holder);
			convertView = linearLayout;
		}
		set_pos(holder, position);
		holder.imageView.setImageDrawable(null);
		String img_url = ((TestTypeEntity) this.arrayList.get(position))
				.getTestCoverImage();
		holder.imageView.setTag(img_url);
		if ((img_url != null) && (!img_url.equals(""))) {
			il.loadDrawable(context1.getClass().getSimpleName(),img_url, new ImageCallback() {

				@Override
				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
					if (((String) holder.imageView.getTag()).equals(imageUrl)){
						holder.imageView.setImageDrawable(imageDrawable);
					}
					
				}
			});
		}
		holder.txtTitle.setText(((TestTypeEntity) this.arrayList.get(position))
				.getTestQuestionTitle());
		holder.txtIntro.setText(((TestTypeEntity) this.arrayList.get(position))
				.getTestQuestionDescription());
		check_scale_status(this.arrayList, holder);
		convertView.setTag(holder);

		return convertView;
	}

	private void check_scale_status(ArrayList<TestTypeEntity> paramArrayList,
			Holder paramHolder) {
		TestTypeEntity localTestTypeEntity = (TestTypeEntity) paramArrayList
				.get(((Integer) paramHolder.btn_test.getTag()).intValue());
		String status = localTestTypeEntity.getTestStatus();
		String visible = localTestTypeEntity.getVisible();
		if (status.equals("INIT")) {
			set_buttons_gone(paramHolder);
			if(isAnswerFile(localTestTypeEntity.getTestQuestionTitle())) {
				paramHolder.btn_test.setText(R.string.test_status_continue);
			}
			paramHolder.btn_test.setVisibility(View.VISIBLE);
			
		} else if ((status.equals("TESTED"))
				|| ((status.equals("REPORTED")) && (visible.equals("N")))) {
			set_buttons_gone(paramHolder);
			paramHolder.btn_check.setVisibility(View.VISIBLE);
		} else if ((status.equals("REPORTED")) && (visible.equals("Y"))) {
			set_buttons_gone(paramHolder);
			paramHolder.btn_reported.setVisibility(View.VISIBLE);
		}
	}

	private boolean isAnswerFile(String file_name) {
		File file = context1.getFileStreamPath(file_name);
		return file.exists();
	}
	
	
	private View.OnClickListener clickListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.test_type_check:
				String str1 = context1.getResources().getString(R.string.test_checking);
				UtilService.show(context1, str1, 2000);
				break;

			case R.id.test_type_test:
				Message localMessage2 = new Message();
				localMessage2.what = TestTypeAdapter_test;
				localMessage2.arg1 = ((Integer) v.getTag()).intValue();
				TestTypeAdapter.this.handler.sendMessage(localMessage2);
				break;

			case R.id.test_type_reported:
				Message localMessage1 = new Message();
				localMessage1.what = TestTypeAdapter_report;
				localMessage1.arg1 = ((Integer) v.getTag()).intValue();
				TestTypeAdapter.this.handler.sendMessage(localMessage1);
				break;

			}
		}
	};

	public static final class Holder {
		private Button btn_check;
		private Button btn_reported;
		private Button btn_test;
		public ImageView imageView;
		public TextView txtIntro;
		public TextView txtTitle;
	}
}

/*
 * Location:
 * /home/guokai/share/apktool-install-linux-r04-brut1/com.zskx.pemsystem
 * -1/classes_dex2jar.jar Qualified Name:
 * com.zskx.pemsystem.adpater.TestTypeAdapter JD-Core Version: 0.5.4
 */