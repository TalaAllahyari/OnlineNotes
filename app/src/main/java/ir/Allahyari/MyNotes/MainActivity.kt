package ir.Allahyari.MyNotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import ir.Allahyari.MyNotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        val user = auth.currentUser
        if (user!=null){
            finish()
            startActivity(Intent(this, AllNotes::class.java))
        }

        binding.login.setOnClickListener{
            val password = binding.password.text.toString()
            val email = binding.email.text.toString()
            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "هر دو فیلد الزامی هستند", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        checkMailVerification();
                    } else {
                        Toast.makeText(this, "حسابی با این مشخصات یافت نشد", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.sigin.setOnClickListener {
            startActivity(Intent(this, signin::class.java))
        }
    }

    fun checkMailVerification() {
        val user = auth.currentUser
        if (user != null && user.isEmailVerified) {
            startActivity(Intent(this, AllNotes::class.java))
        }
        else{
            Toast.makeText(this, "ایمیل خود را تایید کنید", Toast.LENGTH_SHORT).show()
            auth.signOut()
        }
    }


}


