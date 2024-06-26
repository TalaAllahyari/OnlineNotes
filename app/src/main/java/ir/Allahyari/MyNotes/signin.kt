package ir.Allahyari.MyNotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import ir.Allahyari.MyNotes.databinding.ActivitySigninBinding


class signin : AppCompatActivity() {
    lateinit var binding: ActivitySigninBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.login.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.signin.setOnClickListener{
            val password = binding.password.text.toString()
            val email = binding.email.text.toString()
            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "هر دو فیلد الزامی هستند", Toast.LENGTH_SHORT).show()
            }
            else if (password.length < 8){
                binding.password.setError("پسورد شما باید بیشتر از 8 کاراکتر باشد")
            }
            else{
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "ثبت نام با موفقیت انجام شد", Toast.LENGTH_SHORT).show()
                            sendEmailVerification()
                        } else {
                            Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
    fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "ایمیل تاییدیه به ایمیل شما ارسال شد", Toast.LENGTH_LONG).show()
                    auth.signOut()
                    finish()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, "خطا در ارسال ایمیل تاییدیه: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
