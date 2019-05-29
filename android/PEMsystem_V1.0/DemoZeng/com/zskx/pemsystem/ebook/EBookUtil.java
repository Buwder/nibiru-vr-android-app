package com.zskx.pemsystem.ebook;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import android.content.Context;
import android.util.Log;

public class EBookUtil {

	public class BoldTagNoMatch extends Exception {
		public BoldTagNoMatch(String err) {
			Log.e("BoldException", err);
		}
	}

	/**
	 * 由于删除加粗字体的标签，用于 重新定位 目录书签的位置。
	 * 
	 * @param position
	 * @param mb
	 */
	private void reGetPositionForDir(int position, MagazineBean mb) {
		Iterator<Directory> iterator = mb.getDirectory().iterator();
		while (iterator.hasNext()) {
			Directory dir = iterator.next();
			if (dir.getPosition() > position) {
				dir.setPosition(dir.getPosition() - 2);
			}
		}
	}

	/**
	 * 解析杂志内容，把目录和加粗字体的标签的位置 遍历出来。
	 * 
	 * @param mb
	 * @throws BoldTagNoMatch
	 */
	private MagazineBean parserMagazineContent(MagazineBean mb)
			throws BoldTagNoMatch {
		StringBuilder content = mb.getContent();
		Iterator<Directory> iterator = mb.getDirectory().iterator();
		while (iterator.hasNext()) {
			Directory dir = iterator.next();
			int position = content.indexOf(dir.getDir_tag());

			dir.setPosition(position);
			if (position != -1) {
				content.replace(position, position + dir.getDir_tag().length(),
						"");
			}

		}

		String bold_tag = "##";
		while (true) {
			if (content.indexOf(bold_tag) != -1) {
				Bold bold = new Bold();
				int start_position = content.indexOf(bold_tag);

				bold.setStart_position(start_position);

				content.replace(start_position, start_position + 2, "");
				reGetPositionForDir(start_position, mb);
				int end_position = content.indexOf(bold_tag);
				if (end_position == -1) {
					throw new BoldTagNoMatch("加粗字体标签不匹配！");
				} else {
					bold.setEnd_position(end_position);
				}
				content.replace(end_position, end_position + 2, "");
				reGetPositionForDir(end_position, mb);
				mb.addBold(bold);
			} else {
				break;
			}
		}
		return mb;
	}

	/**
	 * 解析XML字符串，分离出 杂志格式
	 * 
	 * @param resource
	 */
	public MagazineBean parserString(String resource) {
		String s = paraseTabToSpace(resource);
		ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes());
		try {
			return parserMagazineContent(PullMagazineXMLParser.parse(bais));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public MagazineBean testParser(Context context) throws IOException {
		return parserString(readFileToString(context.getAssets().open(
				"test_phone.xml")));
	}

	private String paraseTabToSpace(String resource) {

		if (resource.contains("\t")) {
			resource = resource.replaceAll("\\t", "    ");
		}
		if (resource.contains("\r")) {
			resource = resource.replaceAll("\\r", "");
		}
		return resource;
	}

	private String readFileToString(InputStream in) throws IOException {
		StringBuilder result = new StringBuilder();

		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String s = "";
		while (true) {
			s = br.readLine();

			if (s != null) {
				while (s.contains("\t") || s.contains("\r")) {
					s = s.replaceAll("\t", "  ");
					s = s.replaceAll("\r", "");
				}
				result.append(s).append("\n");

			} else {
				break;
			}
		}
		br.close();
		return result.toString();
	}
}
