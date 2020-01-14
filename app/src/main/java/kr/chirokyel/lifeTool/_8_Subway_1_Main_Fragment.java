package kr.chirokyel.lifeTool;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class _8_Subway_1_Main_Fragment extends Fragment {

    private View view;

    String news;

    Button subway_button_refresh;
    Button subway_button1_before, subway_button2_before, subway_button3_before, subway_button4_before;
    Button subway_button1_after, subway_button2_after, subway_button3_after, subway_button4_after;
    TextView subway_textview1, subway_textview2, subway_textview3, subway_textview4;


    List<String> timeList1 = new ArrayList<>(); int index1 = 0;
    List<String> timeList2 = new ArrayList<>(); int index2 = 0;
    List<String> timeList3 = new ArrayList<>(); int index3 = 0;
    List<String> timeList4 = new ArrayList<>(); int index4 = 0;
    String strURL_subway1, strURL_subway2, strURL_subway3, strURL_subway4;
    String strXML_subway1, strXML_subway2, strXML_subway3, strXML_subway4;
    String day;

    ArrayList<_9_Book_1_Form> bookList = new ArrayList<>();
    _9_Book_2_Adapter book_adapter;
    RecyclerView book_main_recyclerview;

    EditText book_main_editText;
    Button book_main_button;
    String result = "";

    public static _8_Subway_1_Main_Fragment newInstance() {
        _8_Subway_1_Main_Fragment fragWednesday = new _8_Subway_1_Main_Fragment();
        return fragWednesday;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout._8_subway_1_main_fragment____________________8, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        subway_button_refresh = getActivity().findViewById(R.id.subway_button_refresh);
        subway_button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    index1 = 0;
                    index2 = 0;
                    index3 = 0;
                    index4 = 0;

                    // 메서드 : 요일 설정하기 ~ setAPI 앞에 와야함.
                    setDay();

                    // 메서드 : 지하철 시간표 받기
                    setAPI();

                    subwayAPI(strXML_subway1, timeList1);
                    subwayAPI(strXML_subway2, timeList2);
                    subwayAPI(strXML_subway3, timeList3);
                    subwayAPI(strXML_subway4, timeList4);

                    setTextview();
                    setButtonAfter();
                    setButtonBefore();

                } catch (NullPointerException e) {
                    Toast.makeText(getContext(), "네트워크 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        book_main_editText = getActivity().findViewById(R.id.book_main_edittext);
        book_main_button = getActivity().findViewById(R.id.book_main_button);

        book_main_recyclerview = getActivity().findViewById(R.id.book_main_recyclerview);
        book_adapter = new _9_Book_2_Adapter(bookList);
        book_main_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        book_main_recyclerview.setAdapter(book_adapter);

        book_main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBook(book_main_editText.getText().toString());
                bookAPI();
                book_adapter.notifyDataSetChanged();
                _0_Frag2_And_Weather_Step.fragmentCode2 = "북북";
            }
        });
    }



    // 메서드 : 요일 설정하기
    public void setDay() {
        Calendar calendar = Calendar.getInstance();
        int dayNum = calendar.get(calendar.DAY_OF_WEEK);

        if (dayNum == 7) {
            day = "2"; // 토요일
        } else if (dayNum == 1) {
            day = "3"; // 일요일
        } else {
            day = "1"; // 평일
        }
    }

    // 메서드 : API 데이터 명세서 작성
    public void setAPI() {

        String serviceKey = "43506462616b6b3137324d5a6f7a48";

        strURL_subway1 = "http://openAPI.seoul.go.kr:8088" +
                "/" + serviceKey + // 서비스키
                "/xml" + // 데이터 형식
                "/SearchArrivalTimeOfLine2SubwayByIDService" + // 데이터 종류2
                "/1" + // 아이템 시작
                "/5" + // 아이템 종료
                "/0201" + // 전철역 : 고덕 0552 상행(1)
                "/" + day + // 요일 : 평일1, 토요일2, 일요일3
                "/1" + // 상하행 : 상행1, 하행2
                "/";

        strXML_subway1 = getData(strURL_subway1);


        strURL_subway2 = "http://openAPI.seoul.go.kr:8088" +
                "/" + serviceKey + // 서비스키
                "/xml" + // 데이터 형식
//                "/SearchSTNTimeTableByIDService" + // 데이터 종류
                "/SearchArrivalTimeOfLine2SubwayByIDService" + // 데이터 종류2
                "/1" + // 아이템 시작
                "/5" + // 아이템 종료
                "/0202" + // 전철역 : 군자역 725 하행(2)
                "/" + day + // 요일 : 평일1, 토요일2, 일요일3
                "/2" + // 상하행 : 상행1, 하행2
                "/";

        strXML_subway2 = getData(strURL_subway2);

        strURL_subway3 = "http://openAPI.seoul.go.kr:8088" +
                "/" + serviceKey + // 서비스키
                "/xml" + // 데이터 형식
//                "/SearchSTNTimeTableByIDService" + // 데이터 종류
                "/SearchArrivalTimeOfLine2SubwayByIDService" + // 데이터 종류2
                "/1" + // 아이템 시작
                "/5" + // 아이템 종료
                "/0203" + // 전철역 : 이수역 736 상행(1)
                "/" + day + // 요일 : 평일1, 토요일2, 일요일3
                "/1" + // 상하행 : 상행1, 하행2
                "/";

        strXML_subway3 = getData(strURL_subway3);

        strURL_subway4 = "http://openAPI.seoul.go.kr:8088" +
                "/" + serviceKey + // 서비스키
                "/xml" + // 데이터 형식
//                "/SearchSTNTimeTableByIDService" + // 데이터 종류
                "/SearchArrivalTimeOfLine2SubwayByIDService" + // 데이터 종류2
                "/1" + // 아이템 시작
                "/5" + // 아이템 종료
                "/0204" + // 전철역 : 군자역 544 하행(2)
                "/" + day + // 요일 : 평일1, 토요일2, 일요일3
                "/2" + // 상하행 : 상행1, 하행2
                "/";

        strXML_subway4 = getData(strURL_subway4);
    }

    // 메서드 : 명세서 기반 데이터 받아오기
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

    // 메서드 : 데이터 가공하기
    public void subwayAPI(String data, List<String> timeLIst) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(data.getBytes());
            Document doc = documentBuilder.parse(is);
            NodeList nodeList = doc.getElementsByTagName("row");
            Element element = doc.getDocumentElement();

            for (int i = 0; i < nodeList.getLength(); i ++) {
                Node nodeTime = element.getElementsByTagName("ARRIVETIME").item(i);
                String subString = nodeTime.getTextContent().substring(0,5);
                timeLIst.add(subString);
            }

        } catch (ParserConfigurationException e) {
        } catch (SAXException e) {
        } catch (IOException e) {
        }
    }

    public void setButtonBefore() {

        subway_button1_before = getActivity().findViewById(R.id.subway_button1_before);
        subway_button2_before = getActivity().findViewById(R.id.subway_button2_before);
        subway_button3_before = getActivity().findViewById(R.id.subway_button3_before);
        subway_button4_before = getActivity().findViewById(R.id.subway_button4_before);

        subway_button1_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(index1==0)) {
                    index1 -= 1;
                    subway_textview1.setText(timeList1.get(index1));
                }
            }
        });
        subway_button2_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(index2==0)) {
                    index2 -= 1;
                    subway_textview2.setText(timeList2.get(index2));
                }
            }
        });
        subway_button3_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(index3==0)) {
                    index3 -= 1;
                    subway_textview3.setText(timeList3.get(index3));
                }
            }
        });
        subway_button4_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(index4==0)) {
                    index4 -= 1;
                    subway_textview4.setText(timeList4.get(index4));
                }
            }
        });
    }
    public void setButtonAfter() {

        subway_button1_after = getActivity().findViewById(R.id.subway_button1_after);
        subway_button2_after = getActivity().findViewById(R.id.subway_button2_after);
        subway_button3_after = getActivity().findViewById(R.id.subway_button3_after);
        subway_button4_after = getActivity().findViewById(R.id.subway_button4_after);

        subway_button1_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(index1==4)) {
                    index1 += 1;
                    subway_textview1.setText(timeList1.get(index1));
                }
            }
        });
        subway_button2_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(index2==4)) {
                    index2 += 1;
                    subway_textview2.setText(timeList2.get(index2));
                }
            }
        });
        subway_button3_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(index3==4)) {
                    index3 += 1;
                    subway_textview3.setText(timeList3.get(index3));
                }
            }
        });
        subway_button4_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(index4==4)) {
                    index4 += 1;
                    subway_textview4.setText(timeList4.get(index4));
                }
            }
        });
    }
    public void setTextview() {

        subway_textview1 = getActivity().findViewById(R.id.subway_textview1);
        subway_textview2 = getActivity().findViewById(R.id.subway_textview2);
        subway_textview3 = getActivity().findViewById(R.id.subway_textview3);
        subway_textview4 = getActivity().findViewById(R.id.subway_textview4);

        subway_textview1.setText(timeList1.get(0));
        subway_textview2.setText(timeList2.get(0));
        subway_textview3.setText(timeList3.get(0));
        subway_textview4.setText(timeList4.get(0));
    }

    // 메서드 : API 데이터 명세서 작성, 명세서 기반 데이터 받아오기
    public void getBook(final String keyword1) {

        Thread thread = new Thread() {
            @Override
            public void run() {

                String clientId = "_2OvFS9TxB8S4XXn0CZN";//애플리케이션 클라이언트 아이디값";
                String clientSecret = "m2uqlLlmHO";//애플리케이션 클라이언트 시크릿값";

                try {
                    String keyword2 = URLEncoder.encode(keyword1, "UTF-8"); // 검색어
                    String apiURL = "https://openapi.naver.com/v1/search/book.xml?query="+
                            keyword2 +
                            "&display=20" +
                            "&sort=count"; // xml 결과
                    URL url = new URL(apiURL);

                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("X-Naver-Client-Id", clientId);
                    con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                    int responseCode = con.getResponseCode();
                    BufferedReader br;
                    if(responseCode==200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    } else {  // 에러 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();
                    System.out.println(response.toString());
                    result = response.toString();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        };
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("쓰레드 종료");
    }

    // 메서드 : 데이터 가공하기
    public void bookAPI() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(result.getBytes());
            Document doc = documentBuilder.parse(is);
            NodeList nodeList = doc.getElementsByTagName("item");
            Element element = doc.getDocumentElement();

            bookList.clear();
            for (int i = 0; i < nodeList.getLength(); i ++) {
                String strTitle = element.getElementsByTagName("title").item(i+1).getTextContent();
                String strAuthor = element.getElementsByTagName("author").item(i).getTextContent();
                String strPublisher = element.getElementsByTagName("publisher").item(i).getTextContent();
                String strImage = element.getElementsByTagName("image").item(i).getTextContent();
                String strDescription = element.getElementsByTagName("description").item(i+1).getTextContent();
                String strLink = element.getElementsByTagName("link").item(i+1).getTextContent();
                _9_Book_1_Form bookObject = new _9_Book_1_Form(strTitle, strAuthor, strPublisher, strImage, strDescription, strLink);
                bookList.add(bookObject);
            }

        } catch (ParserConfigurationException e) {
        } catch (SAXException e) {
        } catch (IOException e) {
        }

    }



}
