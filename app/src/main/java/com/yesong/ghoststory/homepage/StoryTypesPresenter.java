package com.yesong.ghoststory.homepage;

import android.content.Context;
import android.content.Intent;

import com.yesong.ghoststory.R;
import com.yesong.ghoststory.bean.StoryType;
import com.yesong.ghoststory.storylist.StoryList;

import java.util.ArrayList;
import java.util.List;

public class StoryTypesPresenter implements StoryTypesContract.Presenter{
    private Context context;
    private StoryTypesContract.View view;
    private List<StoryType> storyTypesList = new ArrayList<>();

    @Override
    public void start() {
        String[] typeNames = context.getResources().getStringArray(R.array.type_names);
        String[] typeId = {"dp", "cp" , "xy" , "yy" , "jl" , "mj" , "ly" , "yc" , "neihanguigushi"};
        int[] typesImageId = {R.drawable.ic_dp,R.drawable.ic_cp,R.drawable.ic_xy,R.drawable.ic_yy,
                R.drawable.ic_jl,R.drawable.ic_mj,R.drawable.ic_ly,R.drawable.ic_yc,R.drawable.ic_nh};
        for (int i = 0; i<9; i++) {
            StoryType storyType = new StoryType();
            storyType.setId(typeId[i]);
            storyType.setImg(String.valueOf(typesImageId[i]));
            storyType.setTitle(typeNames[i]);
            storyTypesList.add(storyType);
        }
        view.setTypes(storyTypesList);
    }

    @Override
    public void chooseType(int position) {
        StoryType storyType = storyTypesList.get(position);
        Intent intent = new Intent(context, StoryList.class);
        intent.putExtra("typeId", storyType.getId());
        intent.putExtra("title", storyType.getTitle());
        context.startActivity(intent);
    }

    StoryTypesPresenter(Context context, StoryTypesContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
    }
}
