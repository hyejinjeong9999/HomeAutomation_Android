package viewPage;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ContentViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentList;
    private ArrayList<String> title;

    public ContentViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
//        fragmentList.add(new FragmentHome());
//        fragmentList.add(new FragmentWindow());


        title = new ArrayList<>();
        title.add("HOME");
        title.add("WIN");
        title.add("냉장고");
        title.add("현관문");
        title.add("조명");
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }

    /**
     * Posigion 에 위치한 Fragment 를 반환하는 Method
     * ArrayList에 담아둔 Fragment 를 position index 를 통해 호출
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    /**
     * page의 개수를 반환 => Fragment 배열의 크기가 page개수가 된다.
     */
    @Override
    public int getCount() {
        return fragmentList.size();
    }

    /**
     * add Method를 통해 Fragment 배열에 추가할 Fragemnt 를 담는다.
     * @param fragment
     */
    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
    }
}
