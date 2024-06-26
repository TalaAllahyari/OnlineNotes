package ir.Allahyari.MyNotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import ir.Allahyari.MyNotes.databinding.ActivityCreateBinding

class Create : AppCompatActivity() {
    lateinit var binding: ActivityCreateBinding
    lateinit var auth: FirebaseAuth
    lateinit var firebase: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebase = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        binding.create.setOnClickListener{
            val title = binding.title.text.toString()
            val body = binding.body.text.toString()
            if (title.isEmpty() || body.isEmpty()){
                Toast.makeText(this, "هر دو فیلد الزامی هستند", Toast.LENGTH_SHORT).show()
            }
            else{
                if (user != null) {
                    val documentReference : DocumentReference = firebase.collection("notes")
                        .document(user.uid)
                        .collection("myNotes")
                        .document()
                    val note = mutableMapOf<String, Any>()
                    note["title"]=title
                    note["body"]=body
                    documentReference.set(note).addOnSuccessListener {
                        Toast.makeText(this, "یادداشت شما با موفقیت ایجاد شد", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, AllNotes::class.java))
                    }.addOnFailureListener{
                        Toast.makeText(this, "ذخیره یادداشت شما با شکست مواجه شد", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}

