package com.mikepenz.materialdrawer.icons

import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.ITypeface
import com.mikepenz.materialdrawer.R
import java.util.*

/**
 * Created by mikepenz on 01.11.14.
 */
object MaterialDrawerFont : ITypeface {

    override val fontRes: Int
        get() = R.font.materialdrawerfont_font_v5_0_0

    override val characters: Map<String, Char> by lazy {
        Icon.values().associate { it.name to it.character }
    }

    override val mappingPrefix: String
        get() = "mdf"

    override val fontName: String
        get() = "MaterialDrawerFont"

    override val version: String
        get() = "5.0.0"

    override val iconCount: Int
        get() = characters.size

    override val icons: List<String>
        get() = characters.keys.toCollection(LinkedList())

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

    override fun getIcon(key: String): IIcon = Icon.valueOf(key)

    enum class Icon constructor(override val character: Char) : IIcon {
        mdf_arrow_drop_down('\ue5c5'),
        mdf_arrow_drop_up('\ue5c7'),
        mdf_expand_less('\ue5ce'),
        mdf_expand_more('\ue5cf'),
        mdf_person('\ue7fd');

        override val typeface: ITypeface by lazy { MaterialDrawerFont }
    }
}
