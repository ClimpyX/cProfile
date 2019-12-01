package com.climpy.profile.mongo;

import com.climpy.profile.ProfilePlugin;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class DataCollection {
    private final ProfilePlugin bedrockPlugin;
    private String registeredName;

    public DataCollection(ProfilePlugin bedrockPlugin, String registeredName) {
        this.bedrockPlugin = bedrockPlugin;
        this.registeredName = registeredName;
    }

}
