import androidx.lifecycle.ViewModel
import com.example.share_money_now.data_classes.Group
import com.example.share_money_now.data_classes.Person
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PersonalGroupViewModel : ViewModel() {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance("https://share-money-now-default-rtdb.europe-west1.firebasedatabase.app").reference

    // Use StateFlow to represent the group state
    private val _group = MutableStateFlow<Group?>(null)
    val group: StateFlow<Group?> = _group

    // ...

    // Set the group state using MutableStateFlow
    fun setGroup(group: Group?) {
        _group.value = group
    }
    fun fetchGroupDetails(groupId: String, onDataReceived: (Group?) -> Unit) {
        val groupsRef = databaseReference.child("groups").child(groupId)

        groupsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val groupData = snapshot.getValue(Group::class.java)
                onDataReceived(groupData)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                onDataReceived(null)
            }
        })
    }

    fun findGroupByGroupId(firebaseGroupId: String, onDataReceived: (String?) -> Unit) {
        val groupsRef = databaseReference.child("groups")

        val query = groupsRef.orderByChild("id").equalTo(firebaseGroupId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val groupId = childSnapshot.key
                        onDataReceived(groupId)
                        return
                    }
                } else {
                    onDataReceived(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onDataReceived(null)
            }
        })
    }

    fun checkIfEmailExistsInFirebase(email: String, callback: (Boolean) -> Unit) {
        // Assuming you have a "users" node in your Realtime Database
        val personsRef = databaseReference.child("persons")

        // Query the users node to check if the email exists
        personsRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // If there are any children in the result, the email exists
                val isEmailRegistered = snapshot.childrenCount > 0
                callback(isEmailRegistered)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors, such as network issues or permission problems
                error.toException().printStackTrace()
                callback(false) // Assume email does not exist in case of failure
            }
        })
    }

    fun updateGroupInFirebase(group: Group) {
        // Assuming you have a "groups" node in your database
        val groupsReference = databaseReference.child("groups")

        // Update the group data under the corresponding group ID node
        groupsReference.child(group.id).setValue(group)
            .addOnSuccessListener {
                // Handle success if needed
            }
            .addOnFailureListener { e ->
                // Handle failure, such as network errors or permission problems
                e.printStackTrace()
            }
    }

    fun fetchNameForEmail(email: String, callback: (String?) -> Unit) {
        // Assuming you have a "users" node in your database
        val usersReference = databaseReference.child("persons")

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
