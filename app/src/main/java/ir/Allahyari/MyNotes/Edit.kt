package ir.Allahyari.MyNotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import ir.Allahyari.MyNotes.databinding.ActivityEditBinding

class Edit : AppCompatActivity() {
    lateinit var binding: ActivityEditBinding
    lateinit var data : Intent
    lateinit var auth: FirebaseAuth
    lateinit var store: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        data= intent
        auth = FirebaseAuth.getInstance()
        store = FirebaseFirestore.getInstance()
        val user = auth.currentUser

        val title : String? = data.getStringExtra("title")
        val body : String? = data.getStringExtra("body")

        binding.title.setText(title)
        binding.body.setText(body)

        binding.back.setOnClickListener{
            startActivity(Intent(this, AllNotes::class.java))
        }

        binding.edit.setOnClickListener{
            val newTitle = binding.title.text.toString()
            val newBody = binding.body.text.toString()
            if (newTitle.isEmpty() || newBody.isEmpty()){
                Toast.makeText(this, "هر دو فیلد الزامی هستند", Toast.LENGTH_SHORT).show()
            }
            else{
                if (user != null) {
                    val documentReference : DocumentReference = store.collection("notes")
                        .document(user.uid)
                        .collection("myNotes")
                        .document(data.getStringExtra("noteId").toString())
                    val note = mutableMapOf<String, Any>()
                    note["title"]=newTitle
                    note["body"]=newBody
                    documentReference.set(note).addOnSuccessListener {
                        Toast.makeText(this, "یادداشت شما با موفقیت ویرایش شد", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, AllNotes::class.java))
                    }.addOnFailureListener{
                        Toast.makeText(this, "ویرایش یادداشت شما با شکست مواجه شد", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
