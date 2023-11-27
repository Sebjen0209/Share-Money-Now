package com.example.share_money_now

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.share_money_now.data_classes.Group
import com.example.share_money_now.data_classes.Person
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CreateGroupViewModel : ViewModel() {
    private val databaseReference = FirebaseDatabase.getInstance("https://share-money-now-default-rtdb.europe-west1.firebasedatabase.app")



    private val _groups = MutableLiveData<List<Group>>()
    val items: LiveData<List<Group>> get() = _groups

    /*
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

     */

    fun getItemsByMember(personEmail: String) {
        val query = databaseReference.getReference("groups")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val groupList = mutableListOf<Group>()

                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(Group::class.java)
                    item?.let {
                        if (personEmail in it.members.mapNotNull { member -> member?.email }) {
                            groupList.add(it)
                        }
                    }
                }

                _groups.value = groupList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
    }

    fun fetchNameForEmail(email: String, callback: (String?) -> Unit) {
        // Assuming you have a "users" node in your database
        val usersReference = databaseReference.reference.child("persons")

        // Query the users node to get the user with the specified email
        usersReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Get the first user (assuming email is unique)
                    val userSnapshot = snapshot.children.first()
                    val user = userSnapshot.getValue(Person::class.java)
                    callback(user?.name)
                } else {
                    // User not found
                    callback(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors, such as network issues or permission problems
                error.toException().printStackTrace()
                callback(null)
            }
        })
    }
}