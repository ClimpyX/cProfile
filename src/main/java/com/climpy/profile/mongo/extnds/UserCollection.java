package com.climpy.profile.mongo.extnds;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.mongo.DataCollection;

public class UserCollection extends DataCollection {

    public UserCollection() {
        super(ProfilePlugin.getInstance(), "user");
    }

}
