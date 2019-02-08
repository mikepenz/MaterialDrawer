package com.mikepenz.materialdrawer.icons;

import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import com.mikepenz.iconics.Iconics;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.typeface.ITypeface;
import com.mikepenz.materialdrawer.R;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by mikepenz on 01.11.14.
 */
public class MaterialDrawerFont implements ITypeface {
    private static final String TTF_FILE = "materialdrawerfont-font-v5.0.0.ttf";

    private static Typeface typeface = null;

    private static HashMap<String, Character> mChars;

    @Override
    public IIcon getIcon(String key) {
        return Icon.valueOf(key);
    }

    @Override
    public HashMap<String, Character> getCharacters() {
        if (mChars == null) {
            HashMap<String, Character> aChars = new HashMap<String, Character>();
            for (Icon v : Icon.values()) {
                aChars.put(v.name(), v.character);
            }
            mChars = aChars;
        }

        return mChars;
    }

    @Override
    public String getMappingPrefix() {
        return "mdf";
    }

    @Override
    public String getFontName() {
        return "MaterialDrawerFont";
    }

    @Override
    public String getVersion() {
        return "5.0.0";
    }

    @Override
    public int getIconCount() {
        return mChars.size();
    }

    @Override
    public Collection<String> getIcons() {
        Collection<String> icons = new LinkedList<String>();

        for (Icon value : Icon.values()) {
            icons.add(value.name());
        }

        return icons;
    }


    @Override
    public String getAuthor() {
        return "";
    }

    @Override
    public String getUrl() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getLicense() {
        return "";
    }

    @Override
    public String getLicenseUrl() {
        return "";
    }

    @Override
    public int getFontRes() {
        return R.font.materialdrawerfont_font_v5_0_0;
    }

    @NotNull
    @Override
    public Typeface getRawTypeface() {
        return ResourcesCompat.getFont(Iconics.applicationContext, getFontRes());
    }

    public enum Icon implements IIcon {
        mdf_arrow_drop_down('\ue5c5'),
        mdf_arrow_drop_up('\ue5c7'),
        mdf_expand_less('\ue5ce'),
        mdf_expand_more('\ue5cf'),
        mdf_person('\ue7fd');

        char character;

        Icon(char character) {
            this.character = character;
        }

        public String getFormattedName() {
            return "{" + name() + "}";
        }

        public char getCharacter() {
            return character;
        }

        public String getName() {
            return name();
        }

        // remember the typeface so we can use it later
        private static ITypeface typeface;

        public ITypeface getTypeface() {
            if (typeface == null) {
                typeface = new MaterialDrawerFont();
            }
            return typeface;
        }
    }
}
