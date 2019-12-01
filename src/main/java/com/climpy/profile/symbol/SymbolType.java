package com.climpy.profile.symbol;

import com.climpy.profile.rank.RankType;
import lombok.Getter;

@Getter
public enum SymbolType {
    SYMBOL_16("Symbol_16", "§d#CLIMPY ", RankType.CVIPPLUSPLUS),
    SYMBOL_15("Symbol_15", "§e§lUwU ", RankType.CVIPPLUSPLUS),
    SYMBOL_14("Symbol_14", "§e§lCRY ", RankType.CVIPPLUSPLUS),
    SYMBOL_13("Symbol_13", "§e§lMASTER ", RankType.CVIP),
    SYMBOL_12("Symbol_12", "§e§lPRO ", RankType.CVIP),
    SYMBOL_11("Symbol_11", "§e§l❝E❞ ", RankType.CVIP),
    SYMBOL_10("Symbol_10","§a§lE ", RankType.CVIP),
    SYMBOL_9("Symbol_9", "§a§l❦ ", RankType.MVIPPLUS),
    SYMBOL_8("Symbol_8", "§a§l✦ ", RankType.MVIPPLUS),
    SYMBOL_7("Symbol_7", "§a§l✎ ", RankType.MVIP),
    SYMBOL_6("Symbol_6", "§d§l☘ ", RankType.MVIP),
    SYMBOL_5("Symbol_5", "§d§l♚ ", RankType.CVIP),
    SYMBOL_4("Symbol_4", "§d§l❥ ", RankType.CVIP),
    SYMBOL_3("Symbol_3", "§f§l♫ ", RankType.VIPPLUS),
    SYMBOL_2("Symbol_2", "§f§l✿ ", RankType.VIPPLUS),
    SYMBOL_1("Symbol_1", "§f§l★ ", RankType.VIPPLUS),
    SYMBOL_0("Symbol_0", "", RankType.VIP);
    private String displayName, prefix;
    private RankType rankType;

    SymbolType(String displayName, String prefix, RankType rankType) {
        this.displayName = displayName;
        this.prefix = prefix;
        this.rankType = rankType;
    }

    public static SymbolType getSymbolOrDefault(String internalName) {
        SymbolType symbolType;
        try {
            symbolType = valueOf(internalName.toUpperCase());
        } catch (Exception ex) {
            symbolType = SYMBOL_1;
        }

        return symbolType;
    }

    public static SymbolType getSymbolWithPrefix(String symbolPrefix) {
        for (SymbolType symbolType : values()) {
            if (symbolType.getPrefix().equals(symbolPrefix)) {
                return symbolType;
            }
        }

        return SYMBOL_0;
    }

    public static SymbolType getDefaultSymbolByRank(RankType rankType) {
        if (rankType == RankType.VIP)
            return SYMBOL_1;
        if (rankType == RankType.VIPPLUS)
            return SYMBOL_4;
        if (rankType == RankType.CVIP)
            return SYMBOL_7;
        if (rankType == RankType.CVIPPLUSPLUS) {
            return SYMBOL_11;
        }
        return SYMBOL_0;
    }
    }
