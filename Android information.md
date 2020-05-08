# Android information

## [Fragment](https://github.com/hyunho058/AndroidTIL/blob/master/Fragment.md)

### Data Share

* Activity -> Fragment Data send
  * Bundle을 이용해 Data 전달 한다
    * String, int 등 기본 Type과 Object, ArrayList 를 넘길 수 있다.
    * putSerializable("Key", Value)
      * Key를 이용하여 Fragment에서 Data를 받는다.
* MainActivity

```java
fragmentTransaction=fragmentManager.beginTransaction();
bundle = new Bundle();
fragmentHome = new FragmentHome();
bundle.putSerializable("list", list);
fragmentTransaction.replace(
    R.id.frame, fragmentHome).commitAllowingStateLoss();
fragmentHome.setArguments(bundle);
```

* Fragment

```java
list=(ArrayList<SystemInfoVO>)getArguments().get("list");
```

### Err

*  java.lang.RuntimeException: Parcel: unable to marshal value model.SystemInfoVO

  * Bundle을 통해 데이터를 전달하는 과정에서 에러 발생
  * SystemInfoVO Class에서 직렬화를 해주지 않아 발생
    * implements Serializable 를 추가하여 문제 처리

  ```java
  public class SystemInfoVO implements Serializable 
  ```

  





### Reference

[Fragment Data Send](https://iw90.tistory.com/131)





## View Pager&Tab Layout

### TabLayout

```java
tabLayout=(TabLayout)findViewById(R.id.tabLayout);

//TabLayout 항목 추가
tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView(
    "HOME",R.drawable.house_black_18dp)));
tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView(
    "Win",R.drawable.toys_black_18dp)));
tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView(
    "냉장고",R.drawable.kitchen_black_18dp)));
tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView(
    "현관문",R.drawable.border_vertical_black_18dp)));
tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView(
    "조명",R.drawable.incandescent_black_18dp)));
/**
* TabLayout 터치 이벤트
*/
tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.v(TAG,"onTabSelected()_getPosition=="+tab.getPosition());
        fragmentTransaction=fragmentManager.beginTransaction();
        bundle = new Bundle();
        switch (tab.getPosition()){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:

        }
    }
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }
    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
});
//customTabViewCreate//
private View createTabView(String tabName, int iconImage){
    View tabView = getLayoutInflater().inflate(R.layout.custom_tab, null);
    TextView tvTab = (TextView) tabView.findViewById(R.id.tvTab);
    tvTab.setText(tabName);
    ImageView ivTab = (ImageView) tabView.findViewById(R.id.ivTab);
    ivTab.setImageResource(iconImage);
    return tabView;
}
```

* Method
  * onTabSelected()
    * 텝이 선택 되었을때 호출
  * onTabUnselected()
    * 텝이 선택되지 않았을 때 호출
  * onTabReselected()
    * 텝이 다시 선택되었을 때 호출

### View Page

* 원하는 page 개수만큼 Fragment 생성
* View Pager를 관리하는 Page Adapter Create
* Page Adapter내에 있는 fragments 배열에 만든 Fragment를 넣어준다
* View에 Page Adapter를 장착
* Tab Layout에 View Pager를 연동
* Tab Layout에 Custom View를 통해 원하는 모양으로 View를 적용

```java
FragmentReceiver fReceiver = FragmentReceiver.shareMyString(mystring);
adapter.addFragment(fReceiver);

public static FragmentReceiver shareMyString(String value) {
    FragmentReceiver f = new FragmentReceiver();
    Bundle args = new Bundle();
    args.putString("mystring", value);
    f.setArguments(args);
    return f;
}
 
@Override
public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    myString = getArguments().getString("mystring");
}

ViewPagerActivity에서는 어댑터에 add하기 전에, 미리 프래그먼트를 선언하고, 메소드로 데이터를 전달합니다.
전달 받을 프래그먼트에서는 위와 같이 ShareMyString이라는 메소드를 하나 만들고, onViewCreated에서 부여한 key값을 통해 데이터를 받을 수 있습니다.
```

### Reference

[Viewpater&TabLayout](https://kangmin1012.tistory.com/12)

[ViewPager](https://hyogeun-android.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EA%B0%95%EC%9D%98-9-TabLayout-ViewPager-%EC%99%80-BaseActivity%EC%82%AC%EC%9A%A9)

## QR code

* Zxing Library
* build.gradle

```
compile 'com.journeyapps:zxing-android-embedded:3.2.0@aar'
compile 'com.google.zxing:core:3.2.1'
implementation 'com.journeyapps:zxing-android-embedded:3.6.0'
```

### Zxing in Fragment 

```java
@Nullable
@Override
public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_fridge,container,false);
    context=container.getContext();
    //QR code Scanner Start
    IntentIntegrator.forSupportFragment(FragmentFridge.this).initiateScan();

    return  view;
}
/**
* QR code - Zxing Library
*/
@Override
public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    Log.v(TAG,"onActivityResult"+resultCode);
    if(resultCode == Activity.RESULT_OK) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Log.v(TAG, "result.getContents() == "+scanResult.getContents());
        Toast.makeText(context,scanResult.getContents(),Toast.LENGTH_SHORT).show();
    } else {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
```





### Reference

[CustomZxing](https://dpdpwl.tistory.com/20)

[in Fragment](https://stackoverflow.com/questions/37251583/how-to-start-zxing-on-a-fragment/43966669)

## Socket Communication

* Socket
  * PrintWriter
  * BufferedReader

### PrintWriter

* MainActivity

```java
Thread thread = new Thread(new Runnable() {
    @Override
    public void run() {
        try {
            socket=new Socket("IP",1234);
            bufferedReader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream());
            Log.v(TAG,"Socket Situation=="+socket.isConnected());
            asyncTask.execute();
            while (true){
                String msg = sharedObject.pop();
                printWriter.println(msg);
                printWriter.flush();
            }
        }catch (IOException e){
            Log.v(TAG,"Socket Communication IOException=="+ e);
        }
    }
});
thread.start();
```

* ShraredObject

```java
/**
 * Queue 구조로 LinkedList에 put Method로 데이터를 입력하고 pop Method로 데이터를 꺼낸다.
 */
public class SharedObject {
    String TAG = "SharedObject";
    Object monitor = new Object();
    private LinkedList<String> dataList = new LinkedList<>();

    public void put(String msg) {
        synchronized (monitor){
            Log.v(TAG, "put() == " + msg);
            dataList.addLast(msg);
            monitor.notify();
        }
    }

    public String pop() {
        String result = "";
        synchronized (monitor) {
            if (dataList.isEmpty()) {
                try{
                    monitor.wait();
                    result = dataList.removeFirst();

                }catch (InterruptedException e){
                    Log.v(TAG,"pop()_InterruptedException=="+e);
                }
            }else {
                Log.v(TAG,"pop() =="+dataList.getFirst());
                result = dataList.removeFirst();
            }
        }
        return result;
    }
}
```

### BufferedReader

* Fragment

```java
public class FragmentTest extends Fragment {
    String TAG="FragmentTest";
    View view;
    Context context;
    SeekBar sbLED;
    TextView tvReceiveData;
    Button btnTest;
    Communication.SharedObject sharedObject;
    BufferedReader bufferedReader;

    public FragmentTest(Communication.SharedObject sharedObject, BufferedReader bufferedReader){
        this.sharedObject=sharedObject;
        this.bufferedReader=bufferedReader;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test,container,false);
        context=container.getContext();
        tvReceiveData=view.findViewById(R.id.tvReceiveData);

        btnTest=view.findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.v(TAG,"onClick");
                tvReceiveData.setText("바뀌니???");
            }
        });
        /**
         * asyncTaskTest Object 인자에 bufferedReader 와 TextVIew를 넘겨준다
         */
        Communication.DataReceiveAsyncTaskTest asyncTaskTest =
                new Communication.DataReceiveAsyncTaskTest(bufferedReader, tvReceiveData);
        asyncTaskTest.execute();
        /**
         * SeekBar를 이용해 0-255 까지의 Int값을 받아 sharedObject에 Data를 넘겨준다
         */
        sbLED=view.findViewById(R.id.sbLED);
        sbLED.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.v(TAG,"onProgressChanged =="+progress);
                sharedObject.put(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return  view;
    }
}
```

* AsyncTask

```java
public class DataReceiveAsyncTaskTest extends AsyncTask<Void, String, String> {
    String TAG="DataReceiveAsyncTaskTest";
    BufferedReader bufferedReader;
    TextView tvReceiveData;

    String msg = "";

    public DataReceiveAsyncTaskTest(BufferedReader bufferedReader, TextView tvReceiveData){
        this.bufferedReader=bufferedReader;
        this.tvReceiveData=tvReceiveData;
    }
    /**\
     *Thread 처리 Code
     */
    @Override
    protected String doInBackground(Void... voids) {
        while (true){
            try {
                msg = bufferedReader.readLine();
                Log.v(TAG,"doInBackground()_readLine()=="+msg);
                publishProgress(msg);
            }catch (IOException e){
                Log.v(TAG,"doInBackground()_IOException=="+e);
            }
        }
    }
    /**
     * Thread가 진행 중 실행 UI를 변경할 수 있다.
     * Thread 진행중 bufferedReader를 통해 받아온 데이터를 TextView에 Set해준다
     */
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.v(TAG,"nProgressUpdate_values="+values[0]);
        tvReceiveData.setText(values[0]);
        Log.v(TAG,"getText()=="+tvReceiveData.getText());
    }
    /**
     * doInBackground 의 수행이 끝난뒤 실행
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.v(TAG,"onPostExecute------------");
    }
}
```



### Fragment Component



## Other Reference

[Layout 테두리](https://5stralia.tistory.com/10)

[상단바or타이틀바 지우기](https://commin.tistory.com/63)

[상태바색변경&투명도](https://codeomni.tistory.com/217)







