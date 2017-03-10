package com.afomic.javalords.util;

import com.afomic.javalords.models.Developer;

import java.util.ArrayList;

/**
 * Created by afomic on 10-Mar-17.
 */

public class SearchUtil {
    private ArrayList<Developer> developers;
    private static SearchUtil util;
    private SearchUtil(ArrayList<Developer> developers){
        this.developers=developers;
    }
    public static SearchUtil getInstance(ArrayList<Developer> developers){
        if(util==null){
            util=new SearchUtil(developers);
        }
        return util;
    }
    public ArrayList<Developer>  search(String query){
        ArrayList<Developer> result=new ArrayList<>();
        for(Developer developer:developers){
            if(contains(developer,query)){
                result.add(developer);
            }
        }
        return result;
    }
    private boolean contains(Developer developer,String query){
        String username=developer.getUsername();
        return username.contains(query);
    }
}
