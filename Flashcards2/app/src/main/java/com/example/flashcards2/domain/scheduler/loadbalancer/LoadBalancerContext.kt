package com.example.flashcards2.domain.scheduler.loadbalancer

class LoadBalancerContext(
    val load_balancer: LoadBalancer,
    val note_id: Int?,
    val deckconfig_id: Long,
    val fuzz_seed: Long?
) {

    fun find_interval(interval: Float, minimum: Int, maximum: Int): Int?{
        return load_balancer.find_interval(
            interval,
            minimum,
            maximum,
            deckconfig_id,
            fuzz_seed,
            )
    }
}