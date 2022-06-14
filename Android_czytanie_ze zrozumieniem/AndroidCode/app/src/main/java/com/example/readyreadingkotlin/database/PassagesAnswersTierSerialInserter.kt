package com.example.readyreadingkotlin.database

class PassagesAnswersTierSerialInserter(override var iterator: Iterator<Int>?,
                                        var howTheAnswerChanged: HashMap<Int, Int>?,
                                        var describedPassageId: Int?,
                                        var name: String,
                                        var password: String
                                        )
    : AbstractSerialDataInserter<Int>(iterator) {

    override fun insertNextSingleData() {

            val key = iterator!!.next()
            val value = howTheAnswerChanged!![key]

            DbHelper().addNewPassagesAnswersTier(
                    value!!,
                    describedPassageId!!,
                    key,
                    name,
                    password,
                    this
            )

    }
}