package com.kidslearning.app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kidslearning.app.data.Reward
import com.kidslearning.app.data.KidsLearningDatabase
import com.kidslearning.app.data.KidsLearningRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RewardsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: KidsLearningRepository

    private val _rewards = MutableStateFlow<List<Reward>>(emptyList())
    val rewards: StateFlow<List<Reward>> = _rewards.asStateFlow()

    private val _coinBalance = MutableStateFlow(150)
    val coinBalance: StateFlow<Int> = _coinBalance.asStateFlow()

    private val _purchasedRewards = MutableStateFlow<Set<Int>>(emptySet())
    val purchasedRewards: StateFlow<Set<Int>> = _purchasedRewards.asStateFlow()

    private val _purchaseResult = MutableStateFlow<PurchaseResult?>(null)
    val purchaseResult: StateFlow<PurchaseResult?> = _purchaseResult.asStateFlow()

    init {
        val database = KidsLearningDatabase.getDatabase(application)
        repository = KidsLearningRepository(database)

        viewModelScope.launch {
            repository.getAllRewards().collect { rewardList ->
                _rewards.value = rewardList
            }
        }
    }

    fun purchaseReward(reward: Reward) {
        viewModelScope.launch {
            if (_coinBalance.value >= reward.cost) {
                _coinBalance.value -= reward.cost
                _purchasedRewards.value = _purchasedRewards.value + reward.id
                // Note: In a real implementation, this would save the purchase to database
                // repository.savePurchase(reward)
                _purchaseResult.value = PurchaseResult.Success(reward)
            } else {
                _purchaseResult.value = PurchaseResult.InsufficientCoins
            }
        }
    }

    fun clearPurchaseResult() {
        _purchaseResult.value = null
    }
}

sealed class PurchaseResult {
    data class Success(val reward: Reward) : PurchaseResult()
    object InsufficientCoins : PurchaseResult()
}
