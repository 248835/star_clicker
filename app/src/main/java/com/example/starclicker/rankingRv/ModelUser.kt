package com.example.starclicker.rankingRv

class ModelUser {

    private var name: String? = null
    private var score: Int? = null

    fun ModelUser() {}

    fun ModelUser(name: String?, score: Int) {
        this.name = name
        this.score = score
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getScore(): Int? {
        return score
    }

    fun setScore(score: Int?) {
        this.score = score
    }
}