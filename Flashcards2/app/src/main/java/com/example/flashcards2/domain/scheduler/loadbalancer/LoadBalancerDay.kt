package com.example.flashcards2.domain.scheduler.loadbalancer

import com.example.flashcards2.domain.model.Flashcard

class LoadBalancerDay(
    val cards: List<Flashcard>
) {

    fun has_sibling(card_id: Long): Boolean {
        return if(cards.find { it.id == card_id } != null){
            true
        }else {
            false
        }
    }
}