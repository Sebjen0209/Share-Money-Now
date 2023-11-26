import androidx.lifecycle.ViewModel
import com.example.share_money_now.data_classes.Group
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PersonalGroupViewModel : ViewModel() {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance("https://share-money-now-default-rtdb.europe-west1.firebasedatabase.app").reference

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
}
