package com.demo.option1

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ExpandableListView
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.ExpandableListView.OnGroupClickListener
import android.widget.ExpandableListView.OnGroupExpandListener
import android.widget.Toast


import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.demo.R
import com.demo.option1.model.Child
import com.demo.option1.model.Group
import com.demo.option1.sqlite_database.Contact
import com.demo.option1.sqlite_database.MyDB

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var list:ArrayList<Group>
    lateinit var ch_list:ArrayList<Child>

    companion object {

        internal val TAG = "MainActivity"
        // final static String url="https://raw.githubusercontent.com/karunkumar25/Api_Test/master/test.json";
        internal val url = "https://raw.githubusercontent.com/karunkumar25/Api_Test/master/test2"

        private var expandableListView: ExpandableListView? = null
        private var adapter: ExpandableListAdapter? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        expandableListView = findViewById<View>(R.id.simple_expandable_listview) as ExpandableListView

        // Setting group indicator null for custom indicator
        expandableListView!!.setGroupIndicator(null)

        MyDB(this)
        jsonParse()
        setListener()

    }

    private fun jsonParse() {
        val pDialog = ProgressDialog(this)
        pDialog.setMessage("Loading...")
        pDialog.show()


        AndroidNetworking.get(url).build().getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject) {

                Log.d("TAGS","Data"+ response.toString())

                val key= response.keys()
                while (key.hasNext()){
                    val QuestionName=key.next()
                    // Log.d("TAGS"," QuestionName key  =>  "+ QuestionName)
                    val ansData=ArrayList<String>()
                    val ans=response.getJSONArray(QuestionName)
                    ansData.clear()
                    for(i in 0 until ans.length()){
                        val obj=ans.getJSONObject(i)
                        val ansName=obj.getString("top_name")
                        ansData.add(ansName)
                    }


                    Log.d("TAGS"," QuestionName key  =>   $QuestionName  =  ${ansData[0]} =  ${ansData[1]} =  ${ansData[2]} =  ${ansData[3]}")
                   MyDB(this@MainActivity).AddData(Contact(QuestionName,ansData[0],ansData[1],ansData[2],ansData[3]))

                }


                //  Toast.makeText(baseContext, insert, Toast.LENGTH_SHORT).show()

                showData()
                /*   val list = ArrayList<Group>()
                   var ch_list: ArrayList<Child>

                   try {
                       val key = response.keys()
                       while (key.hasNext()) {
                           val k = key.next()
                           val gru = Group()
                           gru.name = k
                           ch_list = ArrayList()

                           val ja = response.getJSONArray(k)

                           for (i in 0 until ja.length()) {
                               val jo = ja.getJSONObject(i)
                               val ch = Child()
                               ch.top_id = jo.getString("top_id")
                               ch.top_name = jo.getString("top_name")
                               ch_list.add(ch)

                           } // for loop end
                           gru.items = ch_list
                           list.add(gru)
                       }*/



               /*     adapter = ExpandableListAdapter(this@MainActivity, list)
                    expandableListView!!.setAdapter(adapter)*/
                    pDialog.dismiss()

               // } catch (e: JSONException) {
                 //   e.printStackTrace()
               // }

            }

            override fun onError(anError: ANError) {
                pDialog.dismiss()
                showData()
            }
        })
    }


    private fun showData() {

        val contacts = MyDB(this).getAllContacts()

         list = ArrayList<Group>()
         ch_list=ArrayList<Child>()


        for (cn in contacts) {
            ch_list.add(Child("",cn.ANS1))
            ch_list.add(Child("",cn.ANS2))
            ch_list.add(Child("",cn.ANS3))
            ch_list.add(Child("",cn.ANS4))
            list.add(Group(cn.Question,ch_list))
        }
        adapter = ExpandableListAdapter(this@MainActivity, list)
        expandableListView!!.setAdapter(adapter)

    }


    // Setting different listeners to expandablelistview
    internal fun setListener() {

        // This listener will show toast on group click
        expandableListView!!.setOnGroupClickListener { listview, view, group_pos, id ->
            //   Toast.makeText(MainActivity.this, "You clicked : " + adapter.getGroup(group_pos), Toast.LENGTH_SHORT).show();
            false
        }

        // This listener will expand one group at one time
        // You can remove this listener for expanding all groups
        expandableListView!!.setOnGroupExpandListener(object : OnGroupExpandListener {

            // Default position
            internal var previousGroup = -1

            override fun onGroupExpand(groupPosition: Int) {
                if (groupPosition != previousGroup) expandableListView!!.collapseGroup(previousGroup)
                previousGroup = groupPosition
            }

        })

        // This listener will show toast on child click
        expandableListView!!.setOnChildClickListener { listview, view, groupPos, childPos, id ->
            //  Toast.makeText(MainActivity.this, "You clicked : " + adapter.getChild(groupPos, childPos), Toast.LENGTH_SHORT).show();
            false
        }
    }





    /* Runtime permisstion check Storegae*/

    val isStoragePermissionGranted: Boolean
        get() {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission is granted")

                    return true
                } else {

                    Log.v(TAG, "Permission is revoked")
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 234)
                    return false
                }
            } else {
                Log.v(TAG, "Permission is granted")
                return true
            }
        }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (requestCode == 234) {

            var i = 0
            val len = permissions.size
            while (i < len) {
                val permission = permissions[i]
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    Log.e(TAG, "user rejected the permission: " + permissions[0] + "was " + grantResults[0])
                    // user rejected the permission

                    var showRationale = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permission)
                    }
                    if (!showRationale) {
                        Log.e(TAG, "user also CHECKED \"never ask again\" " + permissions[0] + "was " + grantResults[0])

                        /*   showMessageOKCancel("Without WRITE_EXTERNAL_STORAGE permission the app is unable to auto-generate folder when storeing files to your device. Are you sure you want to deny this permission?",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // click ok button
                                        SplashActivity.this.finish();
                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //click Cancel button
                                        isStoragePermissionGranted();
                                    }
                                });*/

                    } else if (android.Manifest.permission.WRITE_EXTERNAL_STORAGE == permission) {

                        showMessageOKCancel("This permission is necessary for this app.Are you sure you want to deny this permission?",
                                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() },
                                DialogInterface.OnClickListener { dialog, which -> isStoragePermissionGranted })
                    }
                } else {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.e(TAG, "Permission: " + permissions[0] + "was " + grantResults[0])
                        //resume tasks needing this permission


                    }
                }
                i++
            }
        }

    }

    private fun showMessageOKCancel(message: String, onClickListener: DialogInterface.OnClickListener, onClickCancel: DialogInterface.OnClickListener) {
        AlertDialog.Builder(applicationContext, AlertDialog.THEME_HOLO_LIGHT)
                .setMessage(message)
                .setPositiveButton("OK", onClickListener)
                .setCancelable(false)
                .setNegativeButton("Cancel", onClickCancel).create().show()

    }

    /*  End Permisstion */
}