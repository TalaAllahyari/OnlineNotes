package ir.Allahyari.MyNotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import ir.Allahyari.MyNotes.databinding.ActivitySingleNoteBinding

class SingleNote : AppCompatActivity() {
    lateinit var binding: ActivitySingleNoteBinding
    lateinit var store: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySingleNoteBinding.inflate(layoutInflater)
        store = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        setContentView(binding.root)
        val data= intent
        binding.title.text = data.getStringExtra("title")
        binding.body.text = data.getStringExtra("body")
        val docId=data.getStringExtra("noteId").toString()
        binding.edit.setOnClickListener{
            val intent = Intent(it.context, Edit::class.java)
            intent.putExtra("title",data.getStringExtra("title"))
            intent.putExtra("body",data.getStringExtra("body"))
            intent.putExtra("noteId",data.getStringExtra("noteId"))
            it.context.startActivity(intent)
        }
        binding.delete.setOnClickListener{
            if (user != null) {
                val documentReference: DocumentReference =
                    store.collection("notes").document(user.uid)
                        .collection("myNotes").document(docId)
                documentReference.delete().addOnSuccessListener {
                    Toast.makeText(this, "یادداشت با موفقیت حذف شد", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, AllNotes::class.java))
                }.addOnFailureListener {
                    Toast.makeText(this, "عملیات با شکست مواجه شد", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}





