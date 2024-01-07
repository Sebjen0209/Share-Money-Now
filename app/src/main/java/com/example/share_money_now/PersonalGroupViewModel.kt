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

    private val _group = MutableStateFlow<Group?>(null)
    val group: StateFlow<Group?> = _group

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
        val personsRef = databaseReference.child("persons")

        personsRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val isEmailRegistered = snapshot.childrenCount > 0
                callback(isEmailRegistered)
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
                callback(false)
            }
        })
    }

    fun updateGroupInFirebase(group: Group) {
        val groupsReference = databaseReference.child("groups")

        groupsReference.child(group.id).setValue(group)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    fun fetchNameForEmail(email: String, callback: (String?) -> Unit) {
        val usersReference = databaseReference.child("persons")

        usersReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userSnapshot = snapshot.children.first()
                    val user = userSnapshot.getValue(Person::class.java)
                    callback(user?.name)
                } else {
                    callback(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
                callback(null)
            }
        })
    }
}
