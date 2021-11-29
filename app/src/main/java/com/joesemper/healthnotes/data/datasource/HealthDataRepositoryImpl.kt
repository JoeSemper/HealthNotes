package com.joesemper.healthnotes.data.datasource

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.joesemper.healthnotes.data.model.HealthContentState
import com.joesemper.healthnotes.data.model.HealthData
import com.joesemper.healthnotes.data.repository.HealthDataRepository
import com.joesemper.healthnotes.utils.generateNewDataId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HealthDataRepositoryImpl : HealthDataRepository {

    companion object {
        const val DATA_COLLECTION = "data"
    }


    private val db = Firebase.firestore

    @ExperimentalCoroutinesApi
    override fun getAllHealthData() = channelFlow {
        getHealthDataCollection()
            .get()
            .addOnSuccessListener(getHealthDataStateListener(this))

        awaitClose { }
    }

    override suspend fun addNewData(data: HealthData) {
        getHealthDataCollection().document(generateNewDataId()).set(data)
    }

    @ExperimentalCoroutinesApi
    private suspend fun getHealthDataStateListener(scope: ProducerScope<HealthContentState>): OnSuccessListener<in QuerySnapshot> =
        OnSuccessListener<QuerySnapshot> { task ->
            scope.launch {
                getDataStateFromDoc(task.documents).collect {
                    scope.trySend(it)
                }
            }
        }

    @ExperimentalCoroutinesApi
    private fun getDataStateFromDoc(docs: List<DocumentSnapshot>) = callbackFlow {
        docs.forEach { doc ->
            doc.reference.collection(DATA_COLLECTION)
                .addSnapshotListener { snapshots, error ->
                    if (snapshots != null) {

                        val result = HealthContentState()

                        for (dc in snapshots.documentChanges) {
                            when (dc.type) {
                                DocumentChange.Type.ADDED -> {
                                    val userCatch = dc.document.toObject<HealthData>()
                                    result.added.add(userCatch)
                                }
                                DocumentChange.Type.MODIFIED -> {
                                }
                                DocumentChange.Type.REMOVED -> {
                                    val userCatch = dc.document.toObject<HealthData>()
                                    result.deleted.add(userCatch)
                                }
                            }
                        }

                        trySend(result)
                    }
                }
        }
        awaitClose { }
    }

    private fun getHealthDataCollection(): CollectionReference {
        return db.collection(DATA_COLLECTION)
    }


}