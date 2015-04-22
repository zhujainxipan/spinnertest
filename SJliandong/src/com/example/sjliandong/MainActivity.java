package com.example.sjliandong;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends Activity {
	private List<Provence> provences;
	private Provence provence;
	ArrayAdapter<Provence> adapter01;
	ArrayAdapter<City> adapter02;
	ArrayAdapter<District> adapter03;
	private Spinner spinner01, spinner02, spinner03;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		spinner01 = (Spinner) findViewById(R.id.spinner01);
		spinner02 = (Spinner) findViewById(R.id.spinner02);
		spinner03 = (Spinner) findViewById(R.id.spinner03);

		try {
			provences = getProvinces();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		adapter01 = new ArrayAdapter<Provence>(this,
				android.R.layout.simple_list_item_1, provences);
		spinner01.setAdapter(adapter01);
		spinner01.setSelection(0, true);

		adapter02 = new ArrayAdapter<City>(this,
				android.R.layout.simple_list_item_1, provences.get(0)
						.getCitys());
		spinner02.setAdapter(adapter02);
		spinner02.setSelection(0, true);

		adapter03 = new ArrayAdapter<District>(this,
				android.R.layout.simple_list_item_1, provences.get(0)
						.getCitys().get(0).getDistricts());
		spinner03.setAdapter(adapter03);
		spinner03.setSelection(0, true);

		spinner01.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				provence = provences.get(position);
				adapter02 = new ArrayAdapter<City>(MainActivity.this,
						android.R.layout.simple_list_item_1, provences.get(
								position).getCitys());
				spinner02.setAdapter(adapter02);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		
		
		spinner02.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				adapter03 = new ArrayAdapter<District>(MainActivity.this,
						android.R.layout.simple_list_item_1, provence.getCitys().get(position)
						.getDistricts());
				spinner03.setAdapter(adapter03);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public List<Provence> getProvinces() throws XmlPullParserException,
			IOException {
		List<Provence> provinces = null;
		Provence province = null;
		List<City> citys = null;
		City city = null;
		List<District> districts = null;
		District district = null;
		Resources resources = getResources();

		InputStream in = resources.openRawResource(R.raw.citys_weather);

		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();

		parser.setInput(in, "utf-8");
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				provinces = new ArrayList<Provence>();
				break;
			case XmlPullParser.START_TAG:
				String tagName = parser.getName();
				if ("p".equals(tagName)) {
					province = new Provence();
					citys = new ArrayList<City>();
					int count = parser.getAttributeCount();
					for (int i = 0; i < count; i++) {
						String attrName = parser.getAttributeName(i);
						String attrValue = parser.getAttributeValue(i);
						if ("p_id".equals(attrName))
							province.setId(attrValue);
					}
				}
				if ("pn".equals(tagName)) {
					province.setName(parser.nextText());
				}
				if ("c".equals(tagName)) {
					city = new City();
					districts = new ArrayList<District>();
					int count = parser.getAttributeCount();
					for (int i = 0; i < count; i++) {
						String attrName = parser.getAttributeName(i);
						String attrValue = parser.getAttributeValue(i);
						if ("c_id".equals(attrName))
							city.setId(attrValue);
					}
				}
				if ("cn".equals(tagName)) {
					city.setName(parser.nextText());
				}
				if ("d".equals(tagName)) {
					district = new District();
					int count = parser.getAttributeCount();
					for (int i = 0; i < count; i++) {
						String attrName = parser.getAttributeName(i);
						String attrValue = parser.getAttributeValue(i);
						if ("d_id".equals(attrName))
							district.setId(attrValue);
					}
					district.setName(parser.nextText());
					districts.add(district);
				}
				break;
			case XmlPullParser.END_TAG:
				if ("c".equals(parser.getName())) {
					city.setDistricts(districts);
					citys.add(city);
				}
				if ("p".equals(parser.getName())) {
					province.setCitys(citys);
					provinces.add(province);
				}

				break;

			}
			event = parser.next();

		}
		return provinces;
	}
}
