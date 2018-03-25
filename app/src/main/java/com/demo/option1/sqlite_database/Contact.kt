package com.demo.option1.sqlite_database

class Contact() {

      var Question:String?=null
      var ANS1:String?=null
      var ANS2:String?=null
      var ANS3:String?=null
      var ANS4:String?=null

     constructor(Question: String?, ANS1: String?, ANS2: String?, ANS3: String?, ANS4: String?) : this() {
         this.Question = Question
         this.ANS1 = ANS1
         this.ANS2 = ANS2
         this.ANS3 = ANS3
         this.ANS4 = ANS4

     }

 }

