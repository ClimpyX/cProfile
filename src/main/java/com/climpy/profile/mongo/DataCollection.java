package com.climpy.profile.mongo;

import com.climpy.profile.ProfilePlugin;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class DataCollection {
    private final ProfilePlugin profilePlugin;
    private String registeredName;

    public DataCollection(ProfilePlugin profilePlugin, String registeredName) {
        this.profilePlugin = profilePlugin;
        this.registeredName = registeredName;
    }

}
