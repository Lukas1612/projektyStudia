package com.example.readyreadingkotlin.described_passage

import com.example.readyreadingkotlin.described_passage.DescribedPassage

interface PassageLoaderListener {

    fun update(passageList: MutableList<DescribedPassage>)
}