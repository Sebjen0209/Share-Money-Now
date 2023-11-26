package com.example.share_money_now

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.share_money_now.data_classes.Group
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CreateGroupViewModel : ViewModel() {
    private val databaseReference = FirebaseDatabase.getInstance("https://share-money-now-default-rtdb.europe-west1.firebasedatabase.app")



    private val _groups = MutableLiveData<List<Group>>()
    val items: LiveData<List<Group>> get() = _groups

    fun getItemsById(groupOwnerId: String, personEmail: String) {
        val query = databaseReference.getReference("groups").orderByChild("ownerId").equalTo(groupOwnerId)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val groupList = mutableListOf<Group>()

                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(Group::class.java)
                    item?.let { groupList.add(it) }
                }

                _groups.value = groupList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
    }
}