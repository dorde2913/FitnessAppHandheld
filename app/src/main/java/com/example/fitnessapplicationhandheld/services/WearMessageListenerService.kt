package com.example.fitnessapplicationhandheld.services

import android.net.Uri
import android.util.Log
import com.example.fitnessapplicationhandheld.repositories.WorkoutRepository
import com.example.fitnessapplicationhandheld.services.util.BPMUpdate
import com.example.fitnessapplicationhandheld.services.util.CaloriesUpdate
import com.example.fitnessapplicationhandheld.services.util.DistanceUpdate
import com.example.fitnessapplicationhandheld.services.util.LocationUpdate
import com.example.fitnessapplicationhandheld.services.util.handlers
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json
import javax.inject.Inject


@AndroidEntryPoint
class WearMessageListenerService : WearableListenerService(){

    @Inject
    lateinit var repository: WorkoutRepository

    private val dataClient by lazy { Wearable.getDataClient(this)}


    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED) {
                println("DATA CHANGED")

                val dataItem = event.dataItem
                println(dataItem.uri.path)
                if (dataItem.uri.path == null) return

                val segments = dataItem.uri.path?.split("/") ?: return
                val dataMap = DataMapItem.fromDataItem(dataItem).dataMap
                val path = segments[1]
                if (!handlers.containsKey(path)) return

                val handler = handlers[path]!!
                if (handler.handleUpdate(dataMap,segments, repository)!=0)
                    Log.d("Wear Listener","Error handling wear update")



                deleteDataItem(dataItem.uri.path!!)
            }
        }
    }


    fun deleteDataItem(path: String){
        val uri = Uri.Builder()
            .scheme("wear")
            .path(path)
            .build()

        dataClient.deleteDataItems(uri).addOnSuccessListener { dataItem->
            Log.d("DataClient", "DataItem deleted: $dataItem")
        }
            .addOnFailureListener { exception->
                Log.d("DataClient","Failed to delete: $exception")
            }
    }


}


