import com.example.share_money_now.data_classes.Group
import com.example.share_money_now.data_classes.Person
import com.google.firebase.database.*

class FirebaseManager {
    private val databaseReference: DatabaseReference

    // Initialize Firebase with a specific database URL
    init {
        //FirebaseDatabase.getInstance("https://share-money-now-default-rtdb.europe-west1.firebasedatabase.app")
        databaseReference = FirebaseDatabase.getInstance("https://share-money-now-default-rtdb.europe-west1.firebasedatabase.app").reference
    }

    fun createGroup(group: Group) {
        val groupReference = databaseReference.child("groups").push()
        groupReference.setValue(group)
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
}
