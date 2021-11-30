package com.joesemper.healthnotes.data.datasource

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.joesemper.healthnotes.data.model.HealthData
import com.joesemper.healthnotes.data.repository.HealthDataRepository
import com.joesemper.healthnotes.utils.generateNewDataId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow

class HealthDataRepositoryImpl : HealthDataRepository {

    companion object {
        const val DATA_COLLECTION = "data"
    }

    private val db = Firebase.firestore

    @ExperimentalCoroutinesApi
    override fun getAllHealthData() = channelFlow {
        val listener = getHealthDataCollection().addSnapshotListener { value, error ->
            value?.let {
                val result = it.toObjects(HealthData::class.java)
                trySend(result)
            }
        }

        awaitClose {
            listener.remove()
        }
    }

    override suspend fun addNewData(data: HealthData) {
        getHealthDataCollection().document(generateNewDataId()).set(data)
    }

    private fun getHealthDataCollection(): CollectionReference {
        return db.collection(DATA_COLLECTION)
    }


}