package com.example.oasisproject;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.zip.Inflater;

// 메인화면
public class MainActivity extends AppCompatActivity {
    private static LinearLayout container;
    private static LayoutInflater inflater;
    private ArrayList<RecipeData> Recipedata;
    private ArrayList<RecipeData> checkedRecipedata = new ArrayList<RecipeData>();
    private AlarmManager alarmManager;
    private NotificationManager notificationManager;
    private int endDayOfMonth[] = new int[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    NotificationCompat.Builder builder;
    private GregorianCalendar mCalender;
    Button myRecipe, myRefrigerator, myHouseKeeper, full;
    Button addItem;
    ImageButton refrigerator;
    SelectDialog selectDialog;
    recipeAdapter recipeadapter;
    DbOpenHelper dbHelper;
    SQLiteDatabase db;
    TextView checkeditemlist;
    MaterialCalendarView calendar;
    HashMap<String, Integer> moneysumlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.main_container);

        myRecipe = (Button) findViewById(R.id.buttonRecipe);
        myRefrigerator = (Button) findViewById(R.id.buttonRefrigerator);
        myHouseKeeper = (Button) findViewById(R.id.buttonHousekeeping);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.refrigerator, container, true);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mCalender = new GregorianCalendar();

        setAlarm();
        setAddItem();
        initRecipe();

        dbHelper = new DbOpenHelper(this);

        myRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeAllViews();
                inflater.inflate(R.layout.recipe, container, true);

                Button selectitem = (Button) findViewById(R.id.buttonSelectItem);
                selectitem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, checkItems.class);
                        startActivityForResult(intent, Code.requestCode);

                    }
                });

                ListView listview = (ListView) findViewById(R.id.listveiwRecipe);
                recipeadapter = new recipeAdapter(getBaseContext(), checkedRecipedata);
                listview.setAdapter(recipeadapter);
                recipeadapter.notifyDataSetChanged();
            }
        });

        myRefrigerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeAllViews();
                inflater.inflate(R.layout.refrigerator, container, true);

                setAddItem();
                ImageButton refrigerator = (ImageButton) findViewById(R.id.imageButtonRefrigerator);
                refrigerator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        container.removeAllViews();
                        inflater.inflate(R.layout.clickrefrigerator, container, true);

                        findViewById(R.id.LinearLayoutRefrigerator).getBackground().setAlpha(127);


                        full = (Button) findViewById(R.id.buttonSeeFull);
                        full.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), Full.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });

        myHouseKeeper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeAllViews();
                inflater.inflate(R.layout.housekeeper, container, true);

                final LinearLayout housekeeperMonth, housekeeperWeek;
                housekeeperMonth = (LinearLayout) findViewById(R.id.housekeepingMonthlayout);
                housekeeperWeek = (LinearLayout) findViewById(R.id.housekeepingWeeklayout);

                housekeeperMonth.setVisibility(View.VISIBLE);
                housekeeperWeek.setVisibility(View.INVISIBLE);

                final ToggleButton change = (ToggleButton) findViewById(R.id.buttonChange);
                change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (change.isChecked()) {
                            housekeeperWeek.setVisibility(View.VISIBLE);
                            housekeeperMonth.setVisibility(View.INVISIBLE);
                        } else {
                            housekeeperMonth.setVisibility(View.VISIBLE);
                            housekeeperWeek.setVisibility(View.INVISIBLE);
                        }
                    }
                });

                Spinner weekSpinner = (Spinner) findViewById(R.id.spinner_week);
                ArrayAdapter weekAdapter = ArrayAdapter.createFromResource(getBaseContext(), R.array.date_week, android.R.layout.simple_spinner_item);
                weekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                weekSpinner.setAdapter(weekAdapter);

                calendar = (MaterialCalendarView) findViewById(R.id.calendarView);

                calendar.state().edit()
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setCalendarDisplayMode(CalendarMode.MONTHS)
                        .commit();

                calendar.addDecorators(
                        new SundayDecorator(),
                        new SaturdayDecorator());

                db = dbHelper.getReadableDatabase();

                ArrayList<String> dates = new ArrayList<String>();
                moneysumlist = new HashMap<String, Integer>();

                Cursor cursor = db.rawQuery(ItemDataBases.SELECT, null);

                while (cursor.moveToNext()) {
                    int money = cursor.getInt(1);
                    int year = cursor.getInt(6);
                    int month = cursor.getInt(7);
                    int day = cursor.getInt(8);
                    String date = year + "," + month + "," + day;

                    if (!dates.contains(date))
                        dates.add(date);

                    if (moneysumlist.containsKey(date)) {
                        int temp = moneysumlist.get(date);
                        money += temp;
                        moneysumlist.put(date, money);
                    } else {
                        moneysumlist.put(date, money);
                    }
                }

                long now = System.currentTimeMillis();
                Date mDate = new Date(now);
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM");
                String getTime = simpleDate.format(mDate);
                String split[] = getTime.split("-");
                String temp = split[0] + "," + Integer.parseInt(split[1]) + ",";
                int result = 0;


                for (int i = 1; i < endDayOfMonth[Integer.parseInt(split[1])]; i++) {
                    if (moneysumlist.containsKey(temp + Integer.toString(i))) {
                        result += moneysumlist.get(temp + Integer.toString(i));
                    }
                }

                final TextView text_month = (TextView) findViewById(R.id.text_month);
                text_month.setText(Integer.parseInt(split[1]) + "월");

                final TextView monthMoney = (TextView) findViewById(R.id.textviewmonthmoney);
                monthMoney.setText(Integer.toString(result) + "원");

                calendar.setOnMonthChangedListener(new OnMonthChangedListener() {
                    @Override
                    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                        int Year = date.getYear();
                        int Month = date.getMonth();

                        int result = 0;

                        for (int i = 1; i < endDayOfMonth[Month]; i++) {
                            if (moneysumlist.containsKey(Year + "," + (Month + 1) + "," + i))
                                result += moneysumlist.get(Year + "," + (Month + 1) + "," + i);
                        }

                        monthMoney.setText(Integer.toString(result) + "원");

                        text_month.setText((Month + 1) + "월");

                    }
                });


                calendar.setOnDateChangedListener(new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                        int Year = date.getYear();
                        int Month = date.getMonth() + 1;
                        int Day = date.getDay();

                        String shot_Day = Year + "," + Month + "," + Day;

                        Log.v("알림", shot_Day);
                        Integer result = moneysumlist.get(shot_Day);

                        calendar.clearSelection();

                        if (result != null) {
                            shot_Day = Year + "년 " + Month + "월 " + Day + "일";

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle(shot_Day);

                            shot_Day = "\n";

                            Cursor cursor = db.rawQuery("select * from buyItems where year=" + Year + " AND month=" + Month + " AND day=" + Day, null);

                            int moneysum = 0;

                            while (cursor.moveToNext()) {
                                String name = cursor.getString(0);
                                int money = cursor.getInt(1);
                                int number = cursor.getInt(2);
                                shot_Day += name + " " + money + "원 (" + number + "개)\n";
                                moneysum += money;
                            }
                            String sum = "\n합계 " + moneysum + "원";
                            sum += shot_Day;

                            builder.setMessage(sum);
                            builder.setPositiveButton("닫기",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });

                            builder.show();
                        }
                    }


                });

                new ApiSimulator(dates).executeOnExecutor(Executors.newSingleThreadExecutor());

            }
        });

        refrigerator = (ImageButton) findViewById(R.id.imageButtonRefrigerator);
        refrigerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeAllViews();
                inflater.inflate(R.layout.clickrefrigerator, container, true);

                findViewById(R.id.LinearLayoutRefrigerator).getBackground().setAlpha(127);

                full = (Button) findViewById(R.id.buttonSeeFull);
                full.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), Full.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }


    void setAddItem() {
        addItem = (Button) findViewById(R.id.buttonAdditem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog = new SelectDialog(MainActivity.this, OnTakePicture, OnLoadPicture, OnWrite);

                selectDialog.setCanceledOnTouchOutside(true);
                selectDialog.setCancelable(true);
                selectDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                selectDialog.show();

            }

            private View.OnClickListener OnTakePicture = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "개발 진행중입니다!.", Toast.LENGTH_SHORT).show();
                    selectDialog.dismiss();
                }

            };

            private View.OnClickListener OnLoadPicture = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "개발 진행중입니다!.", Toast.LENGTH_SHORT).show();
                    selectDialog.dismiss();
                }

            };

            private View.OnClickListener OnWrite = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), WriteItem.class);
                    startActivity(intent);
                    selectDialog.dismiss();
                }

            };

        });
    }

    public static LinearLayout LinearLayoutContainer() {
        return container;
    }

    public static LayoutInflater LinearLayoutInflater() {
        return inflater;
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        ArrayList<String> Time_Result;

        ApiSimulator(ArrayList<String> Time_Result) {
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            for (int i = 0; i < Time_Result.size(); i++) {
                CalendarDay day = CalendarDay.from(calendar);
                String[] time = Time_Result.get(i).split(",");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                dates.add(day);
                calendar.set(year, month - 1, dayy);
            }


            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            calendar.addDecorator(new EventDecorator(Color.MAGENTA, calendarDays, MainActivity.this));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MainActivity.super.onActivityResult(requestCode, resultCode, data);

        checkeditemlist = (TextView) findViewById(R.id.textviewCheckedItems);
        if (requestCode == Code.requestCode) {
            if (resultCode == Code.resultCode) {
                String itemlist = data.getStringExtra("data");
                checkeditemlist.setText(itemlist);

                checkedRecipedata.clear();

                String split[] = itemlist.split(" ");

                for (int i = 0; i < Recipedata.size(); i++) {
                    for (int j = 0; j < split.length; j++) {
                        String item = Recipedata.get(i).getItem();
                        if (split[j].equals(item)) {
                            String name = Recipedata.get(i).getName();
                            int image = Recipedata.get(i).getImage();
                            String url = Recipedata.get(i).getUrl();
                            checkedRecipedata.add(new RecipeData(name, image, item, url));
                        }
                    }
                }

                recipeadapter.notifyDataSetChanged();


            }
        }
    }

    private void setAlarm() {
        Intent receiverIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, receiverIntent, 0);

        String from = "2021-01-05 23:07:00"; //임의로 날짜와 시간을 지정

        //날짜 포맷을 바꿔주는 소스코드
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetime = null;
        try {
            datetime = dateFormat.parse(from);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);

        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }


    private void initRecipe() {
        Recipedata = new ArrayList<RecipeData>();
        Recipedata.add(new RecipeData("동파육", R.drawable.dongporou, "삼겹살", "https://www.10000recipe.com/recipe/693694"));
        Recipedata.add(new RecipeData("쭈꾸미 삼겹살", R.drawable.jjukkumisamgyeobsal, "삼겹살", "hhttps://www.10000recipe.com/recipe/6880401"));
        Recipedata.add(new RecipeData("돼지갈비", R.drawable.dwaejigalbi, "돼지갈비", "https://www.10000recipe.com/recipe/6866754"));
        Recipedata.add(new RecipeData("제육볶음", R.drawable.jeyugbokkeum, "돼지고기", "https://m.blog.naver.com/zldnld99/221790379198"));
        Recipedata.add(new RecipeData("수육", R.drawable.suyug, "돼지고기", "https://www.10000recipe.com/recipe/6861334"));
        Recipedata.add(new RecipeData("삼겹살 장조림", R.drawable.samgyeobsaljangjolim, "삼겹살", "https://m.10000recipe.com/recipe/2725316"));
        Recipedata.add(new RecipeData("돼지고기 샌드위치", R.drawable.porksandwich, "돼지고기", "https://www.10000recipe.com/recipe/5785693"));
        Recipedata.add(new RecipeData("통삼겹 오븐 구이", R.drawable.ovenbakedwholepork, "삼겹살", "https://ukivill.tistory.com/1634"));
        Recipedata.add(new RecipeData("삼겹살 김치찜", R.drawable.image018, "삼겹살", "https://m.blog.naver.com/PostView.nhn?blogId=freshianblog&logNo=221326045382&proxyReferer=https:%2F%2Fwww.google.com%2F"));
        Recipedata.add(new RecipeData("벌집 삼겹살 고추장 양념 구이", R.drawable.image019, "삼겹살", "https://matzzang.net/621"));
        Recipedata.add(new RecipeData("쭈꾸미 삼겹살", R.drawable.jjukkumisamgyeobsal, "쭈꾸미", "https://www.10000recipe.com/recipe/6880401"));
        Recipedata.add(new RecipeData("칠리 새우", R.drawable.chillyshrimp, "새우", "https://www.10000recipe.com/recipe/6879972"));
        Recipedata.add(new RecipeData("갈릭 버터 새우 구이", R.drawable.garlicbuttershrimp, "새우", "https://www.10000recipe.com/recipe/6878424"));
        Recipedata.add(new RecipeData("고등어 데리야끼 조림", R.drawable.godeunguhderiyakki, "고등어", "https://blog.naver.com/wj2796/220923034423"));
        Recipedata.add(new RecipeData("삼치 조림", R.drawable.samchijorim, "삼치", "https://www.10000recipe.com/recipe/6544373"));
        Recipedata.add(new RecipeData("참치 회 덮밥", R.drawable.tunadeopbap, "참치", "https://m.blog.naver.com/freshianblog/221330510078"));
        Recipedata.add(new RecipeData("오징어 볶음", R.drawable.ojinguhbokkeum, "오징어", "https://www.10000recipe.com/recipe/6838943"));
        Recipedata.add(new RecipeData("조개 전골", R.drawable.jogaejeongol, "조개", "https://hls3790.tistory.com/336"));
        Recipedata.add(new RecipeData("조개 버터 오븐 구이", R.drawable.jogaebutterovengui, "조개", "http://blog.daum.net/chefjhkim/11344886"));
        Recipedata.add(new RecipeData("전복 버터 구이", R.drawable.jeonbokbuttergui, "전복", "https://www.10000recipe.com/recipe/6905876"));
        Recipedata.add(new RecipeData("도토리묵 무침", R.drawable.dotorimukmuchim, "도토리묵", "https://m.blog.naver.com/freshianblog/221329833858"));
        Recipedata.add(new RecipeData("가지 튀김", R.drawable.gajituigim, "가지", "https://www.10000recipe.com/recipe/6857320"));
        Recipedata.add(new RecipeData("사과 조림 롤샌드위치", R.drawable.applerolesandwich, "사과", "https://m.blog.naver.com/autolian/220844769353"));
        Recipedata.add(new RecipeData("바나나 팬케이크", R.drawable.bananapancake, "바나나", "https://m.blog.naver.com/yalove66/220166137860"));
        Recipedata.add(new RecipeData("키위 양갱", R.drawable.kiwiangang, "키위", "https://www.10000recipe.com/recipe/964018"));
        Recipedata.add(new RecipeData("참외 장아찌", R.drawable.chamwoijangacci, "참외", "https://www.10000recipe.com/recipe/6870677"));
        Recipedata.add(new RecipeData("딸기 샌드위치", R.drawable.strawberrysandwich, "딸기", "https://m.10000recipe.com/recipe/6867255"));
        Recipedata.add(new RecipeData("딸기 탕후루", R.drawable.strawberrytanghuru, "딸기", "https://www.10000recipe.com/recipe/6886534"));
        Recipedata.add(new RecipeData("오렌지 셔벗", R.drawable.orangesherbet, "오렌지", "https://www.10000recipe.com/recipe/6889010"));
        Recipedata.add(new RecipeData("포도 잼", R.drawable.grapejam, "포도", "https://www.menupan.com/cook/recipeview.asp?cookid=1622"));
        Recipedata.add(new RecipeData("타락죽", R.drawable.tarakjuk, "우유", "https://www.10000recipe.com/recipe/5986263"));
        Recipedata.add(new RecipeData("콘치즈", R.drawable.conecheese, "치즈", "https://www.10000recipe.com/recipe/6879745"));
        Recipedata.add(new RecipeData("콘치즈", R.drawable.conecheese, "옥수수", "https://www.10000recipe.com/recipe/6879745"));
        Recipedata.add(new RecipeData("수플레 오믈렛", R.drawable.supleomlet, "계란", "https://www.10000recipe.com/recipe/6930877"));
        Recipedata.add(new RecipeData("떡만둣국", R.drawable.ricecakedumpligsoup, "만두", "https://amyzzung.tistory.com/464"));
        Recipedata.add(new RecipeData("소시지 야채 볶음", R.drawable.sosagevegetable, "소시지", "https://www.10000recipe.com/recipe/6909785"));
        Recipedata.add(new RecipeData("햄 돈부리 덮밥", R.drawable.spamdonburi, "햄", "https://www.10000recipe.com/recipe/6888069"));
        Recipedata.add(new RecipeData("찜닭", R.drawable.jjimdak, "닭", "https://blog.naver.com/moonra84/222137362165"));
        Recipedata.add(new RecipeData("닭볶음탕", R.drawable.chickenbokeumtang, "닭", "https://blog.naver.com/gugu9416/222192381438"));
        Recipedata.add(new RecipeData("버터카레치킨", R.drawable.butterchickencurry, "닭", "https://blog.naver.com/atemu/222120234598"));
        Recipedata.add(new RecipeData("치킨 스테이크", R.drawable.chickensteak, "닭", "https://blog.naver.com/jh1201o/222029388061"));
        Recipedata.add(new RecipeData("닭죽", R.drawable.dakjuk, "닭", "https://blog.naver.com/misoo0612/222118510852"));
        Recipedata.add(new RecipeData("닭갈비", R.drawable.dakgalbi, "닭", "https://blog.naver.com/lovetogapyjs/222058215631"));
        Recipedata.add(new RecipeData("닭칼국수", R.drawable.dakkalguksu, "닭", "https://blog.naver.com/osseosse/221595420581"));
        Recipedata.add(new RecipeData("유린기", R.drawable.uringi, "닭", "https://blog.naver.com/loveme90000/222086984112"));
        Recipedata.add(new RecipeData("떡볶이", R.drawable.ddeokbbokki, "떡", "https://blog.naver.com/lljjyy1983/222176718240"));
        Recipedata.add(new RecipeData("치킨 토마토 스프", R.drawable.chickentomatosoup, "닭", "https://blog.naver.com/aaado3/222173076529"));
        Recipedata.add(new RecipeData("닭날개 튀김", R.drawable.chickenwing, "닭", "https://blog.naver.com/juya1218/221592346421"));
        Recipedata.add(new RecipeData("치킨마요덮밥", R.drawable.chickenmayo, "닭", "https://blog.naver.com/momtalk2013/221565597400"));
        Recipedata.add(new RecipeData("장조림", R.drawable.jangjorim, "계란", "https://blog.naver.com/lljjyy1983/222060945972"));
        Recipedata.add(new RecipeData("닭가슴살 샐러드", R.drawable.chickenchestsalad, "닭", "https://blog.naver.com/aroomon/221759051369"));
        Recipedata.add(new RecipeData("육개장", R.drawable.yukgaejang, "소고기", "https://blog.naver.com/seodonghoon2/221633614041"));
        Recipedata.add(new RecipeData("닭가슴살 냉채", R.drawable.chichenchestnangchae, "닭", "https://blog.naver.com/minimam0926/221582042298"));
        Recipedata.add(new RecipeData("소불고기", R.drawable.sobulgogi, "소불고기", "https://blog.naver.com/mildek/222102759066"));
        Recipedata.add(new RecipeData("찹스테이크", R.drawable.chopsteak, "소고기", "https://post.naver.com/viewer/postView.nhn?volumeNo=8040769&memberNo=35667439&vType=VERTICAL"));
        Recipedata.add(new RecipeData("소고기 볶음밥", R.drawable.sogogibokkeumbap, "소고기", "https://blog.naver.com/cleostar/221741296136"));
        Recipedata.add(new RecipeData("소고기 무국", R.drawable.sogogimuguk, "소고기", "https://blog.naver.com/okiwasunmi/222191560957"));
        Recipedata.add(new RecipeData("규동", R.drawable.gyudong, "소고기", "https://blog.naver.com/moonra84/221687070534"));
        Recipedata.add(new RecipeData("소고기육전", R.drawable.sogogiyukjeon, "소고기", "https://post.naver.com/viewer/postView.nhn?volumeNo=9811106&memberNo=3265970&vType=VERTICAL"));
        Recipedata.add(new RecipeData("소고기버섯전골", R.drawable.beefmushroom, "소고기", "https://blog.naver.com/mamon08/222116629520"));
        Recipedata.add(new RecipeData("소고기죽", R.drawable.beefjuk, "소고기", "https://post.naver.com/viewer/postView.nhn?volumeNo=13364520&memberNo=3265970&vType=VERTICAL"));
        Recipedata.add(new RecipeData("소고기미역국", R.drawable.beefmiyeok, "소고기", "https://blog.naver.com/6design/222013165736"));
        Recipedata.add(new RecipeData("명란 아보카도 덮밥", R.drawable.myeongranabokado, "명란", "https://blog.naver.com/bottle_green/221789445890"));
        Recipedata.add(new RecipeData("소불고기 전골", R.drawable.sobulgogijeongol, "소불고기", "https://blog.naver.com/hwkmh/222146235520"));
        Recipedata.add(new RecipeData("밀푀유나베", R.drawable.milfeyunabe, "소고기", "https://blog.naver.com/heevely0522/221922366222"));
        Recipedata.add(new RecipeData("한우육회", R.drawable.rawbeef, "소고기", "https://blog.naver.com/hanwoo_love/221778572836"));
        Recipedata.add(new RecipeData("두부채소전", R.drawable.dubuchaesojeon, "두부", "https://post.naver.com/viewer/postView.nhn?volumeNo=19185117&memberNo=12633&vType=VERTICAL"));
        Recipedata.add(new RecipeData("아보카도 연어김밥", R.drawable.abokadoyeonuh, "아보카도", "https://blog.naver.com/rawith0411/221440163403"));
        Recipedata.add(new RecipeData("꼬막비빔밥", R.drawable.kkomak, "꼬막", "https://blog.naver.com/dkdkpad/221711319282"));
        Recipedata.add(new RecipeData("마파두부", R.drawable.mapadubu, "두부", "https://blog.naver.com/yunimoneta1/222161524585"));
        Recipedata.add(new RecipeData("두부까스", R.drawable.dubuggas, "두부", "https://blog.naver.com/ey404/222097266028"));
        Recipedata.add(new RecipeData("부대찌개", R.drawable.budaejjigae, "햄", "https://blog.naver.com/dew36/221887458519"));
        Recipedata.add(new RecipeData("계란찜", R.drawable.eggchim, "계란", "https://blog.naver.com/golfish99/222090522855"));
        Recipedata.add(new RecipeData("두부샐러드", R.drawable.dubusalad, "두부", "https://blog.naver.com/hyenjjung/222184092445"));
        Recipedata.add(new RecipeData("닭가슴살 두부유부초밥", R.drawable.chickenchestdubuyubu, "닭", "https://blog.naver.com/coco7341/222120927367"));

    }
}