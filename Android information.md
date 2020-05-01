# Android information

## Fragment

### Data Share

* Activity -> Fragment Data send
  * Bundle을 이용해 Data 전달 한다
    * String, int 등 기본 Type과 Object, ArrayList 를 넘길 수 있다.
    * putSerializable("Key", Value)
      * Key를 이용하여 Fragment에서 Data를 받는다.

```java
//Activity//
fragmentTransaction=fragmentManager.beginTransaction();
bundle = new Bundle();
fragmentHome = new FragmentHome();
bundle.putSerializable("list", list);
fragmentTransaction.replace(
    R.id.frame, fragmentHome).commitAllowingStateLoss();
fragmentHome.setArguments(bundle);

//Fragment//
list=(ArrayList<SystemInfoVO>)getArguments().get("list");
```



