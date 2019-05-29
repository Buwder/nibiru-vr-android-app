package com.zskx.pemsystem.ebook;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

public class PullMagazineXMLParser {

	private static int currentParserPara = MagazineBean.NONE;

	public static MagazineBean parse(InputStream is) throws Exception {
		MagazineBean mb = null;
		Bold bold = null;
		Directory dir = null;
		// XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		// XmlPullParser parser = factory.newPullParser();

		XmlPullParser parser = Xml.newPullParser(); // 由android.util.Xml创建一个XmlPullParser实例
		parser.setInput(is, "UTF-8"); // 设置输入流 并指明编码方式

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {

			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				// System.out.println("START_DOCUMENT");
				break;
			case XmlPullParser.START_TAG:
				// System.out.println("START_TAG：");

				if (parser.getName().equals("magazine")) {
					mb = new MagazineBean();
				} else if (parser.getName().equals("title")) {
					currentParserPara = MagazineBean.TITLE;
				} else if (parser.getName().equals("directory")) {
					currentParserPara = MagazineBean.DIRECTORY;
					dir = new Directory();
					dir.setDir_tag("@@" + parser.getAttributeValue(0) + "@");
				} else if (parser.getName().equals("content")) {
					currentParserPara = MagazineBean.CONTENT;
				}

				break;
			case XmlPullParser.END_TAG:
				// System.out.println("END_TAG：");
				if (parser.getName().equals("title")) {
					currentParserPara = MagazineBean.NONE;
				} else if (parser.getName().equals("directory")) {
					currentParserPara = MagazineBean.NONE;
				} else if (parser.getName().equals("content")) {
					currentParserPara = MagazineBean.NONE;
				}
				break;
			case XmlPullParser.TEXT:
				// System.out.println("TEXT:" + parser.getText());
				if (currentParserPara == MagazineBean.TITLE) {
					mb.setTitle(parser.getText());
				} else if (currentParserPara == MagazineBean.DIRECTORY) {
					dir.setName(parser.getText());
					dir.setContent(parser.getText());
					mb.addDir(dir);
				} else if (currentParserPara == MagazineBean.CONTENT) {
					mb.addContent(parser.getText());
				}
				break;
			}
			// System.out.println("parser.name：" + parser.getName());
			eventType = parser.next();

		}
		// Log.i("MagazineBean", mb.toString());
		return mb;
	}

}
