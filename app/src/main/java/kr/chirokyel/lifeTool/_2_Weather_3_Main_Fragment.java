package kr.chirokyel.lifeTool;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class _2_Weather_3_Main_Fragment extends Fragment {

    private View view;

    Handler handler = new Handler();
    String strURL_town;
    String strXML_dust;
    String strURL_dust;
    String strXML_sun;
    String strURL_sun;
    String strXML_town;

    _2_Weather_4_Object weatherObject = new _2_Weather_4_Object();
    LocationManager locationManager;
    String latitude;
    String longitutude;

    List<_2_Weather_1_Form> weatherList = new ArrayList<>();
    _2_Weather_2_Adapter weather_adapter;
    RecyclerView weather_main_recyclerview;

    SwipeRefreshLayout weather_swipelayout;

    TextView weather_textview_time;
    TextView weather_textview_address;

    ImageView weather_main_imageview_today;
    TextView weather_main_textview_today; int today;
    TextView weather_main_textview_temperature_today; int temperature;
    TextView weather_main_textview_temperature_maxmin;

    ImageView weather_main_imageview_dust;
    TextView weather_main_textview_dust;

    TextView weather_textview_sun;

    public static _2_Weather_3_Main_Fragment newInstance() {
        _2_Weather_3_Main_Fragment fragMonday = new _2_Weather_3_Main_Fragment();
        return fragMonday;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        System.out.println("OnCreate Weather");
        view = inflater.inflate(R.layout._2_weather_2_main_fragment____________________2, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 메서드 : 기존에 저장된 날씨정보 불러오기
        if (weatherObject.getSunSet() == null) {
            Toast.makeText(getContext().getApplicationContext(), "날씨정보 로드!", Toast.LENGTH_SHORT).show();
            loadData();
        }


            // 저장된 날씨정보가 없는 경우
        if (weatherObject.getSunSet() == null) {
            Toast.makeText(getContext(), "날씨정보를 업데이트해주세요!", Toast.LENGTH_SHORT).show();

            // 저장된 날씨정보가 있는 경우
        } else {

        try {
            setTextView_date();
            setTextView_address();
            setToday();
            setTextView_temperature_today();
            setTextview_temperature_maxmin();
            setImageView_dust();
            setTextView_dust();
            setRecyclerView();
            setTextView_sun();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        }



        // 스와이프 레이아웃 : 당겨서 데이터 새로고침
        weather_swipelayout = getActivity().findViewById(R.id.weather_main_swipeRefreshLayout);
        weather_swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                try {

                    System.out.println("SwipeRefresh Weather");
                    weather_adapter = new _2_Weather_2_Adapter(weatherList);

                    // *****************************************************************************************

                    getLocation();
                    setAPI();

                    // *****************************************************************************************

                    baseAPI(strXML_town);
                    dustAPI(strXML_dust);
                    sunAPI(strXML_sun);
                    dateAPI();

                    // 날씨정보 제대로 불러오기 안되는 경우, 기존 정보 로드해서 재활용
                } catch (NullPointerException e) { e.printStackTrace();
                    loadData();
                    Toast.makeText(getActivity().getApplicationContext(), "네트워크 연결상태를 확인하세요", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "네트워크 연결상태를 확인하세요", Toast.LENGTH_SHORT).show();
                } catch (IndexOutOfBoundsException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "네트워크 연결상태를 확인하세요", Toast.LENGTH_SHORT).show();
                }


                // *****************************************************************************************



                // 날씨정보가 비어있지 않은 경우에만 새로고침 허용
                if (weatherObject.getSunSet() == null) {
                } else {

                    try {

                        setTextView_date();
                        setTextView_address();
                        setToday();
                        setTextView_temperature_today();
                        setTextview_temperature_maxmin();
                        setImageView_dust();
                        setTextView_dust();
                        setRecyclerView();
                        setTextView_sun();

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                    }
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(weather_swipelayout.isRefreshing()) {
                            weather_swipelayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }

        });
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 날씨정보 제대로 들어있는 경우
        if (weatherObject.getSunSet() != null) {
            // 메서드 : 데이터 저장하기
            saveData();
        }
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate onCreate
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    // 메서드 : 위도 받아오기(1/4)
    public void getLocation() {

        // get location
        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGPS();
        } else {
            useGPS();
        }
        convertGPS();
    }

    // 메서드 : 위도 받아오기(2/4) ~ GPS 켜기
    private void onGPS() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // 메서드 : 위도 받아오기(3/4)
    private void useGPS() {

        // 권한 얻기
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {

            // 메서드 : 위치 변화할 때마다 위치정보 받아오기 (1/2)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 100, locationListener);
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 100, locationListener);

            // 최근 위치정보 받아오기
            Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            System.out.println("@@@@@@@@@@@@@@@@@@@@ locationGPS : " + LocationGps);
            System.out.println("@@@@@@@@@@@@@@@@@@@@ locationNetwork : " + LocationNetwork);
            System.out.println("@@@@@@@@@@@@@@@@@@@@ locationPassive : " + LocationPassive);


            // GPS 최근 위치정보 받아오기
            if (LocationGps != null) {

                double lat = LocationGps.getLatitude();
                double lon = LocationGps.getLongitude();

                latitude = String.valueOf(lat);
                longitutude = String.valueOf(lon);
                System.out.println("GPS@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + latitude);

                // 네트워크 최근 위치정보 받아오기
            } else if (LocationNetwork != null) {

                double lat = LocationNetwork.getLatitude();
                double lon = LocationNetwork.getLongitude();

                latitude = String.valueOf(lat);
                longitutude = String.valueOf(lon);
                System.out.println("network@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + latitude);

                // 패시브 최근 위치정보 받아오기
            } else if (LocationPassive != null) {

                System.out.println("passive@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                double lat = LocationPassive.getLatitude();
                double lon = LocationPassive.getLongitude();

                latitude = String.valueOf(lat);
                longitutude = String.valueOf(lon);
                System.out.println("passive@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + latitude);

                // 위치정보 받아올 수 없는 경우
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "gps error", Toast.LENGTH_SHORT).show();
            }

            weatherObject.setLatitude(latitude);
            weatherObject.setLongitude(longitutude);
            System.out.println("@@@@@@@@@@@@@@@@@@@@ method : " + latitude);

        }
    }


    // 메서드 : 위치 변화할 때마다 위치정보 받아오기 (2/2)
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            try {
                weatherObject.setLatitude(String.valueOf(location.getLatitude()));
                weatherObject.setLongitude(String.valueOf(location.getLongitude()));
                System.out.println("@@@@@@@@@@@@@@@@@@@@ location change : " + location);
            } catch (NullPointerException e) {
            }
        }
        @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override public void onProviderEnabled(String provider) {}
        @Override public void onProviderDisabled(String provider) {}
    };



    // 메서드 : 위도 받아오기(4/4) ~ 받아온 위치정보 행정주소로 변환
    public void convertGPS() {

        try {
            Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
            List<Address> list = geocoder.getFromLocation(Double.valueOf(weatherObject.getLatitude()), Double.valueOf(weatherObject.getLongitude()), 1);
            String[] address = list.get(0).getAddressLine(0).split(" ");

            weatherObject.setLocation1(address[1]);
            weatherObject.setLocation2(address[2]);
            weatherObject.setLocation3(address[3]);
            System.out.println(address[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // 메서드 : 위치정보 API 세팅(1/2) ~ API 데이터 요청하기
    public void setAPI() {

        Date date_api = new Date();
        SimpleDateFormat simpleDateFormat_api = new SimpleDateFormat("yyyyMMdd");
        String string_api = simpleDateFormat_api.format(date_api);

        // 서비스키
        String serviceKey = "KXAL2xJD%2FlpCwzdK0nRzvug0rQ8BeOy93SVz23DYDXF8RsZ2nve%2F0KAmd6kPecon5UlOhFDSAUVU3ZC%2FWPs0mw%3D%3D";

        // 날씨온도 API
        Double double_x = convertGRID_GPS(0, Double.parseDouble(latitude), Double.parseDouble(longitutude)).x; int x = (int) Math.round(double_x);
        Double double_y = convertGRID_GPS(0, Double.parseDouble(latitude), Double.parseDouble(longitutude)).y; int y = (int) Math.round(double_y);

        strURL_town = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData?" +
                "serviceKey=" + serviceKey +
                "&base_date=" + string_api +
                "&base_time=" + "0200" +
                "&nx=" + x +
                "&ny=" + y +
                "&numOfRows=" + "200" +
                "&pageNo=" + "1" +
                "&_type=" + "xml";


        // 미세먼지 API
        strURL_dust = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?" +
                "sidoName=" + weatherObject.getLocation1().substring(0,2) +
                "&searchCondition=" + "DAILY" +
                "&pageNo=" + "1" +
                "&numOfRows=" + "100" +
                "&ServiceKey=" + serviceKey;

        // 일몰일출 API
        strURL_sun = "http://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/getLCRiseSetInfo?" +
                "serviceKey=" + serviceKey +
                "&locdate=" + string_api +
                "&longitude=" + weatherObject.getLongitude() +
                "&latitude=" + weatherObject.getLatitude() +
                "&dnYn=" + "Y";

        strXML_town = getData(strURL_town);
        strXML_dust = getData(strURL_dust);
        strXML_sun = getData(strURL_sun);
    }



    // 메서드 : 위치정보 API 세팅(2/2) ~ API 데이터 받아오기
    public String getData(final String strUrl) {

        final String[] lines = {""};
        final String line;

        Thread thread = new Thread() {
            @Override
            public void run() {

                try {

                    BufferedReader bufferedReader = null;
                    URL url = new URL(strUrl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        lines[0] = lines[0] + line + "\n";
                    }
                    System.out.println(lines[0]);

                } catch (MalformedURLException e) {
                } catch (IOException e) {
                }

            }
        };
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return lines[0];
    }




    // 메서드 : 날씨온도 API 데이터 가공
    public void baseAPI(String data) {

        // 날짜, 시간
        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<String> timeLIst = new ArrayList<>();

        String[] timeArray = {"06", "09", "12", "15", "18", "21", "24", "03", "06", "09", "12", "15", "18", "21", "24"};
        String[] dateArray = {"오늘", "오늘", "오늘", "오늘", "오늘", "오늘", "오늘", "내일", "내일", "내일", "내일", "내일", "내일", "내일", "내일"};
        dateList.addAll(Arrays.asList(dateArray));
        timeLIst.addAll(Arrays.asList(timeArray));


        // 현재온도, 날씨상태, 강수상태, 강수확률
        ArrayList<String> curTempList = new ArrayList<>();
        ArrayList<String> cloudList = new ArrayList<>();
        ArrayList<String> waterList = new ArrayList<>();
        ArrayList<String> waterPList = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(data.getBytes());
            Document doc = documentBuilder.parse(is);
            NodeList baseLIst = doc.getElementsByTagName("item");
            Element element = doc.getDocumentElement();

            int a = 0;
            for (int i = 0; i < baseLIst.getLength(); i ++) {

                // 현재온도
                if (element.getElementsByTagName("category").item(i).getTextContent().equals("T3H")) {
                    Node nodeCurTemp = element.getElementsByTagName("fcstValue").item(i);
                    curTempList.add(nodeCurTemp.getTextContent());
                }
                // 날씨상태
                if (element.getElementsByTagName("category").item(i).getTextContent().equals("SKY")) {
                    Node nodeCloud = element.getElementsByTagName("fcstValue").item(i);
                    cloudList.add(nodeCloud.getTextContent());
                }
                // 강수상태
                if (element.getElementsByTagName("category").item(i).getTextContent().equals("PTY")) {
                    Node nodeWater = element.getElementsByTagName("fcstValue").item(i);
                    waterList.add(nodeWater.getTextContent());
                }
                // 강수확률
                if (element.getElementsByTagName("category").item(i).getTextContent().equals("POP")) {
                    Node nodeWaterP = element.getElementsByTagName("fcstValue").item(i);
                    waterPList.add(nodeWaterP.getTextContent());
                }
            }


            weatherObject.baseLIst.clear();
            for (int i = 0; i < 15; i ++) {

                _2_Weather_1_Form weather_form = new _2_Weather_1_Form();

                weather_form.setDate(dateList.get(i));
                weather_form.setTime(timeLIst.get(i));

                weather_form.setCurTemp(curTempList.get(i));
                weather_form.setCloud(cloudList.get(i));
                weather_form.setWater(waterList.get(i));
                weather_form.setWaterP(waterPList.get(i));

                weatherObject.baseLIst.add(weather_form);

                if (weatherObject.baseLIst.get(i).getWater().equals("1")) {
                    weatherObject.baseLIst.get(i).setImage(getURI(getActivity().getApplicationContext(), R.drawable._2_weather_weather4_rain).toString());
                } else if (weatherObject.baseLIst.get(i).getWater().equals("2")) {
                    weatherObject.baseLIst.get(i).setImage(getURI(getActivity().getApplicationContext(), R.drawable._2_weather_weather4_rain).toString());
                } else if (weatherObject.baseLIst.get(i).getWater().equals("3")) {
                    weatherObject.baseLIst.get(i).setImage(getURI(getActivity().getApplicationContext(), R.drawable._2_weather_weather5_snow).toString());
                } else if (weatherObject.baseLIst.get(i).getWater().equals("4")) {
                    weatherObject.baseLIst.get(i).setImage(getURI(getActivity().getApplicationContext(), R.drawable._2_weather_weather4_rain).toString());
                } else if (weatherObject.baseLIst.get(i).getWater().equals("0")) {
                    if (weatherObject.baseLIst.get(i).getCloud().equals("4")) {
                        weatherObject.baseLIst.get(i).setImage(getURI(getActivity().getApplicationContext(), R.drawable._2_weather_weather3_cloud2).toString());
                    } else if (weatherObject.baseLIst.get(i).getCloud().equals("3")) {
                        weatherObject.baseLIst.get(i).setImage(getURI(getActivity().getApplicationContext(), R.drawable._2_weather_weather2_cloud1).toString());
                    } else if (weatherObject.baseLIst.get(i).getCloud().equals("1")) {
                        weatherObject.baseLIst.get(i).setImage(getURI(getActivity().getApplicationContext(), R.drawable._2_weather_weather1_sun).toString());
                    }
                }
            }



            // 최고최저 온도
            int[] curTempArray = new int[8];
            for (int i = 0; i < 8; i ++) {
                curTempArray[i] = Integer.parseInt(weatherObject.baseLIst.get(i).getCurTemp());
            }

            int max = curTempArray[0]; int min = curTempArray[0];

            for(int i = 0 ;i < curTempArray.length;i++) {
                if(max < curTempArray[i]) {max = curTempArray[i];}
                if(min > curTempArray[i]) {min = curTempArray[i];}
            }

            weatherObject.setMaxTemp(Integer.toString(max));
            weatherObject.setMinTemp(Integer.toString(min));

        } catch (ParserConfigurationException e) {
            System.out.println("baseAPI@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@1");
        } catch (SAXException e) {
            System.out.println("baseAPI@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2");
        } catch (IOException e) {
            System.out.println("baseAPI@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@3");
        }
    }


    // 메서드 : 미세먼지 API 데이터 가공
    public void dustAPI(String data) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(data.getBytes());
            Document doc = documentBuilder.parse(is);
            NodeList dustList = doc.getElementsByTagName("item");
            Element element = doc.getDocumentElement();

            for (int i = dustList.getLength() - 1; i >= 0; i --) {
                if (element.getElementsByTagName("cityName").item(i).getTextContent().equals(weatherObject.getLocation2())) {
                    Node nodeDust10 = element.getElementsByTagName("pm10Value").item(i);
                    Node nodeDust25 = element.getElementsByTagName("pm25Value").item(i);

                    weatherObject.setDust10(nodeDust10.getTextContent());
                    weatherObject.setDust25(nodeDust25.getTextContent());
                }
            }

        } catch (ParserConfigurationException e) {
            System.out.println("dustAPI@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@1");
        } catch (SAXException e) {
            System.out.println("dustAPI@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2");
        } catch (IOException e) {
            System.out.println("dustAPI@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@3");
        }
    }

    // 메서드 : 일몰일출 API 데이터 가공
    public void sunAPI(String data) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(data.getBytes());
            Document doc = documentBuilder.parse(is);
            Element element = doc.getDocumentElement();

            Node nodeSunRise = element.getElementsByTagName("sunrise").item(0);
            Node nodeSunSet = element.getElementsByTagName("sunset").item(0);

            weatherObject.setSunRise(nodeSunRise.getTextContent());
            weatherObject.setSunSet(nodeSunSet.getTextContent());
            System.out.println(weatherObject.getSunRise());

        } catch (ParserConfigurationException e) {
            System.out.println("sunAPI@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@1");
        } catch (SAXException e) {
            System.out.println("sunAPI@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2");
        } catch (IOException e) {
            System.out.println("sunAPI@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@3");
        }
    }


    public void dateAPI() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy" + "년 " + "MM" + "월 " + "dd" + "일 " + "HH" + "시 " + "mm" + "분");
        String string_date = simpleDateFormat.format(date);

        weatherObject.setString_date(string_date);
    }



    // 텍스트뷰 : 날짜 설정
    public void setTextView_date() {

        weather_textview_time = getActivity().findViewById(R.id.weather_main_textview_time);
        weather_textview_time.setText(weatherObject.getString_date());
    }
    // 텍스트뷰 : 주소설정
    public void setTextView_address() {

        weather_textview_address = getActivity().findViewById(R.id.weather_main_textview_address);
        weather_textview_address.setText(
                weatherObject.getLocation1() + " " +
                        weatherObject.getLocation2() + " " +
                        weatherObject.getLocation3());
    }


    // 이미지뷰, 텍스트뷰 : 오늘 날씨 온도 설정
    public void setToday() {

        Date date_today = new Date();
        SimpleDateFormat simpleDateFormat_today = new SimpleDateFormat("HH" + "mm");
        String string_today = simpleDateFormat_today.format(date_today);

        if (Integer.parseInt(string_today) <= 430) { today = 7;
        } else if (Integer.parseInt(string_today) <= 730) { today = 0;
        } else  if (Integer.parseInt(string_today) <= 1030) { today = 1;
        } else  if (Integer.parseInt(string_today) <= 1330) { today = 2;
        } else  if (Integer.parseInt(string_today) <= 1630) { today = 3;
        } else  if (Integer.parseInt(string_today) <= 1930) { today = 4;
        } else  if (Integer.parseInt(string_today) <= 2230) { today = 5;
        } else  if (Integer.parseInt(string_today) <= 2400) { today = 6;
        }

        weather_main_imageview_today = getActivity().findViewById(R.id.weather_main_imageview_today);
        weather_main_imageview_today.setImageURI(Uri.parse(weatherObject.baseLIst.get(today).getImage()));

        weather_main_textview_today = getActivity().findViewById(R.id.weather_main_textview_today);
        if (weatherObject.baseLIst.get(today).getWater().equals("1")) {
            weather_main_textview_today.setText("비" + "\n" + "( 강수확률 : " + weatherObject.baseLIst.get(today).getWaterP() + "% )");
        } else if (weatherObject.baseLIst.get(today).getWater().equals("2")) {
            weather_main_textview_today.setText("비/눈" + "\n" + "( 강수확률 : " + weatherObject.baseLIst.get(today).getWaterP() + "% )");
        } else if (weatherObject.baseLIst.get(today).getWater().equals("3")) {
            weather_main_textview_today.setText("눈" + "\n" + "( 강수확률 : " + weatherObject.baseLIst.get(today).getWaterP() + "% )");
        } else if (weatherObject.baseLIst.get(today).getWater().equals("4")) {
            weather_main_textview_today.setText("소나기" + "\n" + "( 강수확률 : " + weatherObject.baseLIst.get(today).getWaterP() + "% )");
        } else if (weatherObject.baseLIst.get(today).getWater().equals("0")) {
            if (weatherObject.baseLIst.get(today).getCloud().equals("4")) {
                weather_main_textview_today.setText("매우흐림" + "\n" + "( 강수확률 : " + weatherObject.baseLIst.get(today).getWaterP() + "% )");
            } else if (weatherObject.baseLIst.get(today).getCloud().equals("3")) {
                weather_main_textview_today.setText("흐림" + "\n" + "( 강수확률 : " + weatherObject.baseLIst.get(today).getWaterP() + "% )");
            } else if (weatherObject.baseLIst.get(today).getCloud().equals("1")) {
                weather_main_textview_today.setText("맑음" + "\n" + "( 강수확률 : " + weatherObject.baseLIst.get(today).getWaterP() + "% )");
            }
        }

    }
    // 텍스트뷰 : 현재 온도 설정
    public void setTextView_temperature_today() {

        weather_main_textview_temperature_today = getActivity().findViewById(R.id.weather_main_textview_temperature_today);
        weather_main_textview_temperature_today.setText(weatherObject.baseLIst.get(today).getCurTemp() + " °C");
    }
    // 텍스트뷰 : 최고 최저 온도 설정
    public void setTextview_temperature_maxmin() {
        weather_main_textview_temperature_maxmin = getActivity().findViewById(R.id.weather_main_textview_temperature_maxmin);
        weather_main_textview_temperature_maxmin.setText("최저 " + weatherObject.getMinTemp() + "°C  /  최고 " + weatherObject.getMaxTemp() + "°C");
    }


    // 이미지뷰 : 미세먼지 단계 설정
    public void setImageView_dust() {
        weather_main_imageview_dust = getActivity().findViewById(R.id.weather_main_imageview_dust);
        System.out.println(weatherObject.getDust10());

        if (Integer.parseInt(weatherObject.getDust10()) >= 151 || Integer.parseInt(weatherObject.getDust25()) >= 76) {
            weather_main_imageview_dust.setImageResource(R.drawable._2_weather_dust1);
        } else if (Integer.parseInt(weatherObject.getDust10()) >= 101 || Integer.parseInt(weatherObject.getDust25()) >= 51) {
            weather_main_imageview_dust.setImageResource(R.drawable._2_weather_dust2);
        } else if (Integer.parseInt(weatherObject.getDust10()) >= 76 || Integer.parseInt(weatherObject.getDust25()) >= 38) {
            weather_main_imageview_dust.setImageResource(R.drawable._2_weather_dust3);
        } else if (Integer.parseInt(weatherObject.getDust10()) >= 51 || Integer.parseInt(weatherObject.getDust25()) >= 26) {
            weather_main_imageview_dust.setImageResource(R.drawable._2_weather_dust4);
        } else if (Integer.parseInt(weatherObject.getDust10()) >= 41 || Integer.parseInt(weatherObject.getDust25()) >= 21) {
            weather_main_imageview_dust.setImageResource(R.drawable._2_weather_dust5);
        } else if (Integer.parseInt(weatherObject.getDust10()) >= 31 || Integer.parseInt(weatherObject.getDust25()) >= 16) {
            weather_main_imageview_dust.setImageResource(R.drawable._2_weather_dust6);
        } else if (Integer.parseInt(weatherObject.getDust10()) >= 16 || Integer.parseInt(weatherObject.getDust25()) >= 9) {
            weather_main_imageview_dust.setImageResource(R.drawable._2_weather_dust7);
        } else if (Integer.parseInt(weatherObject.getDust10()) >= 0 || Integer.parseInt(weatherObject.getDust25()) >= 0) {
            weather_main_imageview_dust.setImageResource(R.drawable._2_weather_dust8);
        }
    }
    // 텍스트뷰 : 미세먼지 수치 설정
    public void setTextView_dust() {
        weather_main_textview_dust = getActivity().findViewById(R.id.weather_main_textview_dust);
        weather_main_textview_dust.setText("미세먼지\n (" + weatherObject.getDust10() + ")\n\n" +
                "초미세먼지\n (" + weatherObject.getDust25() + ")"
        );
    }

    // 리사이클러뷰 : 오늘 내일 온도 설정
    public void setRecyclerView() {
        weather_main_recyclerview = getActivity().findViewById(R.id.weather_main_recyclerview);
        weather_main_recyclerview.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.HORIZONTAL));
        weather_adapter = new _2_Weather_2_Adapter(weatherList);
        weather_main_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        weather_main_recyclerview.setAdapter(weather_adapter);

        if (weatherList != null) {
            weatherList.clear();
        }
        weatherList.addAll(weatherObject.baseLIst);

        for (int i = today - 1; i >= 0; i--) {
            weatherList.remove(i);
            weather_adapter.notifyItemRemoved(i);
        }
    }


    // 텍스트뷰 : 일몰일출 설정
    public void setTextView_sun() {
        weather_textview_sun = getActivity().findViewById(R.id.weather_textview_sun);
        weather_textview_sun.setText("일출 : " + weatherObject.getSunRise() + "    /    " +
                "일몰 : " + weatherObject.getSunSet());
    }


    // 클래스 : 위도정보 API XY정보로 전환
    class LatXLngY {
        public double lat; public double lng;
        public double x; public double y;
    }
    private LatXLngY convertGRID_GPS(int mode, double lat_X, double lng_Y )
    {
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기1준점 Y좌표(GRID)

        //
        // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )
        //

        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        LatXLngY rs = new LatXLngY();

        if (mode == 0) {
            rs.lat = lat_X;
            rs.lng = lng_Y;
            double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra, sn);
            double theta = lng_Y * DEGRAD - olon;
            if (theta > Math.PI) theta -= 2.0 * Math.PI;
            if (theta < -Math.PI) theta += 2.0 * Math.PI;
            theta *= sn;
            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        }
        else {
            rs.x = lat_X;
            rs.y = lng_Y;
            double xn = lat_X - XO;
            double yn = ro - lng_Y + YO;
            double ra = Math.sqrt(xn * xn + yn * yn);
            if (sn < 0.0) {
                ra = -ra;
            }
            double alat = Math.pow((re * sf / ra), (1.0 / sn));
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

            double theta = 0.0;
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0;
            }
            else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5;
                    if (xn < 0.0) {
                        theta = -theta;
                    }
                }
                else theta = Math.atan2(xn, yn);
            }
            double alon = theta / sn + olon;
            rs.lat = alat * RADDEG;
            rs.lng = alon * RADDEG;
        }
        return rs;
    }


    // 메서드 : 드러블 Uri 받기
    public static final Uri getURI(@NonNull Context context,
                                   @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }


    // 메서드 : 데이터 저장하기
    private void saveData() {
        // 쉐어드 생성
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("donotforget", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 배열 저장하기
        Gson gson = new Gson();
        String weatherObject = gson.toJson(this.weatherObject);
        editor.putString("weather", weatherObject);

        editor.apply();
    }

    // 메서드 : 데이터 불러오기
    private void loadData() {
        // 쉐어드 생성하기
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("donotforget", Context.MODE_PRIVATE);

        // 배열 불러오기
        Gson gson = new Gson();
        String weatherObject = sharedPreferences.getString("weather", null);
        Type type = new TypeToken<_2_Weather_4_Object>() {}.getType();
        this.weatherObject = gson.fromJson(weatherObject, type);

        if (this.weatherObject == null) {
            this.weatherObject = new _2_Weather_4_Object();
        }
    }

}
