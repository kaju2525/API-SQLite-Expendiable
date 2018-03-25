package com.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.demo.option1.sqlite_database.Contact
import com.demo.option1.sqlite_database.MyDB
import org.json.JSONObject

class Test: AppCompatActivity() {

    val url = "https://raw.githubusercontent.com/karunkumar25/Api_Test/master/test2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initJsonOperation()
    }

    private fun initJsonOperation() {


        AndroidNetworking.get(url).build().getAsJSONObject(object :JSONObjectRequestListener{
            override fun onResponse(response: JSONObject?) {
                Log.d("TAGS","Data"+ response.toString())

                val key=response!!.keys()
                while (key.hasNext()){
                    val QuestionName=key.next()
                   // Log.d("TAGS"," QuestionName key  =>  "+ QuestionName)
                    val ansData=ArrayList<String>()
                    val ans=response.getJSONArray(QuestionName)
                     for(i in 0 until ans.length()){
                         val obj=ans.getJSONObject(i)
                         val ansName=obj.getString("top_name")
                         ansData.add(ansName)
                     }


                    Log.d("TAGS"," QuestionName key  =>   $QuestionName  =  ${ansData[0]} =  ${ansData[1]} =  ${ansData[2]} =  ${ansData[3]}")
                   // MyDB(this@Test).AddData(Contact(QuestionName,ansData[0],ansData[1],ansData[2],ansData[3]))

                }




            }

            override fun onError(anError: ANError?) {

                Log.d("TAGS","anError"+ anError.toString())



            }

        })




    }

}