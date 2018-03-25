package com.demo.option1.model

import java.util.ArrayList

class Group() {
    var name: String? = null
    var items: ArrayList<Child>? = null

    constructor(name: String?,ANS1:ArrayList<Child>?) : this() {
        this.name = name
        this.items = ANS1

    }
}