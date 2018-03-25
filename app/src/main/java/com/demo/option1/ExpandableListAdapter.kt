package com.demo.option1

import java.util.ArrayList

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView

import com.demo.R
import com.demo.option1.model.Child
import com.demo.option1.model.Group


class ExpandableListAdapter(private val context: Context, private val groups: ArrayList<Group>) : BaseExpandableListAdapter() {


    override fun getChild(groupPosition: Int, childPosititon: Int): Any {

        val chList = groups[groupPosition].items
        return chList?.get(childPosititon)!!

    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView


        val child = getChild(groupPosition, childPosition) as Child
        if (convertView == null) {
            val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.childs, parent, false)
        }



        Log.d("TAG ", groupPosition.toString() + " " + childPosition.toString())

        val child_name = convertView!!.findViewById<View>(R.id.child_name) as TextView
        if (groupPosition == 2 && childPosition == 2) {

        }


        child_name.text = child.top_name
        return convertView
    }


    override fun getChildrenCount(groupPosition: Int): Int {

        val chList = groups[groupPosition].items
        return chList!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return groups[groupPosition]
    }

    override fun getGroupCount(): Int {
        return groups.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }


    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val group = getGroup(groupPosition) as Group
        if (convertView == null) {
            val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.header, parent, false)
        }

        val header_text = convertView!!.findViewById<View>(R.id.header) as TextView
        header_text.text = group.name


        // If group is expanded then change the text into bold and change the
        // icon
        if (isExpanded) {
            header_text.setTypeface(null, Typeface.BOLD)
            header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_less, 0)
        } else {
            // If group is not expanded then change the text back into normal
            // and change the icon

            header_text.setTypeface(null, Typeface.NORMAL)
            header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more, 0)
        }

        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}