package com.mikepenz.materialdrawer.icons

import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.ITypeface
import com.mikepenz.materialdrawer.R
import java.util.*

/**
 * Created by mikepenz on 01.11.14.
 */
class MaterialDrawerFont : ITypeface {
    override val characters: HashMap<String, Char>
        get() {
            if (mChars == null) {
                val aChars = HashMap<String, Char>()
                Icon.values().associateTo(aChars) { it.name to it.character }
                mChars = aChars
            }

            return mChars as HashMap<String, Char>
        }

    override val mappingPrefix: String
        get() = "mdf"

    override val fontName: String
        get() = "MaterialDrawerFont"

    override val version: String
        get() = "5.0.0"

    override val iconCount: Int
        get() = characters.size

    override val icons: Collection<String>
        get() = Icon.values().map { it.name }.toCollection(LinkedList())

    override val author: String
        get() = ""

    override val url: String
        get() = ""

    override val description: String
        get() = ""

    override val license: String
        get() = ""

    override val licenseUrl: String
        get() = ""

    override val fontRes: Int
        get() = R.font.materialdrawerfont_font_v5_0_0

    override fun getIcon(key: String): IIcon {
        return Icon.valueOf(key)
    }

    enum class Icon constructor(override val character: Char) : IIcon {
        mdf_arrow_drop_down('\ue5c5'),
        mdf_arrow_drop_up('\ue5c7'),
        mdf_expand_less('\ue5ce'),
        mdf_expand_more('\ue5cf'),
        mdf_person('\ue7fd');

        override val typeface: ITypeface
            get() = savedTypeface

        companion object {
            // remember the typeface so we can use it later
            private val savedTypeface: ITypeface by lazy { MaterialDrawerFont() }
        }
    }

    companion object {
        private var mChars: HashMap<String, Char>? = null
    }
}
