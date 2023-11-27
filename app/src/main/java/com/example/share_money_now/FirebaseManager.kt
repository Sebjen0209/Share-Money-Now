
import com.example.share_money_now.data_classes.Group
import com.example.share_money_now.data_classes.PaymentList
import com.example.share_money_now.data_classes.Person
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseManager {
    private val databaseReference: DatabaseReference

    // Initialize Firebase with a specific database URL
    init {
        //FirebaseDatabase.getInstance("https://share-money-now-default-rtdb.europe-west1.firebasedatabase.app")
        databaseReference = FirebaseDatabase.getInstance("https://share-money-now-default-rtdb.europe-west1.firebasedatabase.app").reference
    }

    fun createGroup(group: Group) {
        val groupReference = databaseReference.child("groups").push()

        val groupId = groupReference.key

        val paymentReference = databaseReference.child("paymentLists").child(groupId?: "")

        val samplePaymentList = PaymentList(
            groupId = groupId ?: ""
        )

        groupReference.setValue(group)
        paymentReference.setValue(samplePaymentList)

    }

    fun addPersonToGroup(groupId: String, person: Person) {
        val groupReference = databaseReference.child("groups").child(groupId)
        groupReference.child("members").push().setValue(person)
    }

    fun removePersonFromGroup(groupId: String, personId: String) {
        val groupReference = databaseReference.child("groups").child(groupId).child("members").child(personId)
        groupReference.removeValue()
    }

    fun addPersonOnSignUp(person: Person){
        val personReference = databaseReference.child("persons").push()
        personReference.setValue(person)
    }

    fun fetchGroupDetails(groupId: String, onDataReceived: (Group?) -> Unit) {
        val groupsRef = databaseReference.child("groups").orderByChild("id").equalTo(groupId)

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
}
