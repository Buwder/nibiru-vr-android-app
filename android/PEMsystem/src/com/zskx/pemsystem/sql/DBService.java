package com.zskx.pemsystem.sql;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zskx.net.response.MusicDetailEntity;
import com.zskx.pemsystem.util.StringsUtil;

public class DBService {
	private static String TAG = "DBService";
	private DBHelper dbHelper;
	private Context context;
	
	
	public DBService(Context context ,String userId) {
		super();
		this.dbHelper = new DBHelper(context, userId);
		this.context = context;
	}
	
	public void save_music_list(MusicDetailEntity music_track ){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "insert into " + StringsUtil.Music_Playing_list + 
						"(musicId, musicTitle, musicUrl, musicImg, musicTypeDesc) values(?,?,?,?,?)";
		db.execSQL(sql, new Object[]{music_track.getMusicId(), music_track.getMusicTitle(), 
					music_track.getMusicUrl(), music_track.getMusicImage(), music_track.getMusicTypeDescription()});
		db.close();
		Log.i(TAG, "sql>>>" + sql + "---" + music_track.getMusicUrl());
	}
	
	public void save_music_pos(int listpos , int pos, int model , int is_start , int is_pos , int is_fifo){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "insert into " + StringsUtil.Music_Playing_position + "(listPosition, musicPosition, model, isStartPlay , isPos, isFIFO) values(?,?,?,?,?,?)";
		db.execSQL(sql, new Object[]{listpos, pos, model, is_start, is_pos, is_fifo});
		db.close();
		Log.i(TAG, "sql>>>" + sql);
	}
	
	public void delete_music_list(){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "delete from " + StringsUtil.Music_Playing_list ;//+ " where id =?";
		db.execSQL(sql);
		db.close();
		Log.i(TAG, "sql>>>" + sql);
	}
	
	public void delete_music_pos( ){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "delete from " + StringsUtil.Music_Playing_position;// + " where id =?";
		db.execSQL(sql);
		db.close();
		Log.i(TAG, "sql>>>" + sql);
	}
	
	/*public void update_music_pos(MusicDetailEntity music_track, int pos, int model) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "update " + StringsUtil.Music_Playing_position + " set musicId =? , musicTitle =?, position =?, model =? where id =?";
		db.execSQL(sql, new Object[]{music_track.getMusicId(), music_track.getMusicTitle(), pos, model , 0});
		db.close();
		Log.i(TAG, sql + model );
	}*/
	
	/**
	 * 取得分页列表
	 */
	public ArrayList<MusicDetailEntity> getAll_playing_list() {
		ArrayList<MusicDetailEntity> list = new ArrayList<MusicDetailEntity>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String sql = "select * from " + StringsUtil.Music_Playing_list + " order by id asc";
		Cursor rs = db.rawQuery(sql, null);
		MusicDetailEntity entity ;
		while(rs.moveToNext()){
			String id = rs.getString(rs.getColumnIndex("musicId"));
			String title = rs.getString(rs.getColumnIndex("musicTitle"));
			String url = rs.getString(rs.getColumnIndex("musicUrl"));
			String imgURL = rs.getString(rs.getColumnIndex("musicImg"));
			String musicTypeDesc = rs.getString(rs.getColumnIndex("musicTypeDesc"));
			entity = new MusicDetailEntity();
			entity.setMusicId(id);
			entity.setMusicTitle(title);
			entity.setMusicUrl("");
			entity.setMusicUrl(url);
			entity.setMusicImage(imgURL);
			entity.setMusicTypeDescription(musicTypeDesc);
			list.add(entity);
			Log.i(TAG, "title--" + title + "  url--" + entity.getMusicUrl());
		}
		rs.close();
		db.close();
		return list;
	}
	
	public int qurey_track_position() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String sql = "select * from " + StringsUtil.Music_Playing_position ; //+ " where id =?";
		Cursor rs = db.rawQuery(sql, null);
		if(rs.moveToLast()) {
			int id = rs.getInt(rs.getColumnIndex("listPosition"));
			
			rs.close();
			db.close();
			return id;
		}
		rs.close();
		db.close();
		return 1000;
	}
	
	public int qurey_pos() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String sql = "select * from " + StringsUtil.Music_Playing_position ;//+ " where id =?";
		Cursor rs = db.rawQuery(sql, null);
		if(rs.moveToLast()) {
		//	int id = rs.getInt(rs.getColumnIndex("musicId"));
		//	String musicTitle = rs.getString(rs.getColumnIndex("musicTitle"));
			int position = rs.getInt(rs.getColumnIndex("musicPosition"));
		//	int model = rs.getInt(rs.getColumnIndex("model"));
			rs.close();
			db.close();
			return position;
		}
		rs.close();
		db.close();
		return 0;
	}
	
	public int qurey_model() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String sql = "select * from " + StringsUtil.Music_Playing_position ; //+ " where id =?";
		Cursor rs = db.rawQuery(sql, null);
		if(rs.moveToLast()) {
		//	int id = rs.getInt(rs.getColumnIndex("musicId"));
		//	String musicTitle = rs.getString(rs.getColumnIndex("musicTitle"));
		//	int position = rs.getInt(rs.getColumnIndex("position"));
			int model = rs.getInt(rs.getColumnIndex("model"));
			rs.close();
			db.close();
			return model;
		}
		rs.close();
		db.close();
		return 1;
	}
	
	
	public int qurey_isStart() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String sql = "select * from " + StringsUtil.Music_Playing_position ; //+ " where id =?";
		Cursor rs = db.rawQuery(sql, null);
		if(rs.moveToLast()) {
			int isStartPlay = rs.getInt(rs.getColumnIndex("isStartPlay"));
			rs.close();
			db.close();
			return isStartPlay;
		}
		rs.close();
		db.close();
		return 0;
	}
	
	public int qurey_isPos() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String sql = "select * from " + StringsUtil.Music_Playing_position ; //+ " where id =?";
		Cursor rs = db.rawQuery(sql, null);
		if(rs.moveToLast()) {
			int isPos = rs.getInt(rs.getColumnIndex("isPos"));
			rs.close();
			db.close();
			return isPos;
		}
		rs.close();
		db.close();
		return 0;
	}
	
	public int qurey_isFIFO() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String sql = "select * from " + StringsUtil.Music_Playing_position ; //+ " where id =?";
		Cursor rs = db.rawQuery(sql, null);
		if(rs.moveToLast()) {
			int isFIFO = rs.getInt(rs.getColumnIndex("isFIFO"));
			rs.close();
			db.close();
			return isFIFO;
		}
		rs.close();
		db.close();
		return 0;
	}
	
	 
	public long getCount(String table) {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			String sql = "select count(*) from " + table ;
			Cursor rs = db.rawQuery(sql, null);
			rs.moveToFirst();
			long count = rs.getLong(0);
			rs.close();
			db.close();
			return count;
		}
	

}
