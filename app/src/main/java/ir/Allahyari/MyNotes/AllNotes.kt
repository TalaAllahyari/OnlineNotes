package ir.Allahyari.MyNotes

import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import ir.Allahyari.MyNotes.databinding.ActivityAllNotesBinding
import kotlin.random.Random

class AllNotes : AppCompatActivity() {
    lateinit var binding: ActivityAllNotesBinding
    lateinit var auth: FirebaseAuth
    lateinit var store: FirebaseFirestore
    lateinit var recyclerView: RecyclerView
    lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    lateinit var Adapter: FirestoreRecyclerAdapter<MyNotesModel, NoteViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAllNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        store = FirebaseFirestore.getInstance()
        val user = auth.currentUser

        binding.create.setOnClickListener{
            startActivity(Intent(this, Create::class.java))
        }
        val query: Query = if (user != null) {
            store.collection("notes")
                .document(user.uid).collection("myNotes")
        } else{
            store.collection("notes")
        }
        val allnote: FirestoreRecyclerOptions<MyNotesModel> =
            FirestoreRecyclerOptions.Builder<MyNotesModel>().setQuery(query,MyNotesModel::class.java).build()
        Adapter = object : FirestoreRecyclerAdapter<MyNotesModel, NoteViewHolder>(allnote) {
            override fun onBindViewHolder(noteViewHolder: NoteViewHolder, i: Int, firebasemodel: MyNotesModel) {
                noteViewHolder.note.setBackgroundColor(noteViewHolder.itemView.resources.getColor(R.color.bg1,null))
                noteViewHolder.title.setText(firebasemodel.title)
                noteViewHolder.body.setText(firebasemodel.body)
                val docId:String = Adapter.snapshots.getSnapshot(i).id
                noteViewHolder.itemView.setOnClickListener {
                    val intent = Intent(noteViewHolder.itemView.context, SingleNote::class.java)
                    intent.putExtra("title",firebasemodel.title)
                    intent.putExtra("body",firebasemodel.body)
                    intent.putExtra("noteId",docId)
                    noteViewHolder.itemView.context.startActivity(intent)
                }
            }
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
                val view: View =
                    LayoutInflater.from(parent.context).inflate(R.layout.note,parent,false)
                return NoteViewHolder(view)
            }
        }
        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        val layoutParams = recyclerView.layoutParams as RelativeLayout.LayoutParams
        recyclerView.layoutParams = layoutParams

        staggeredGridLayoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.adapter= Adapter
    }
    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val body: TextView = itemView.findViewById(R.id.body1)
        val note: LinearLayout = itemView.findViewById(R.id.note)
        val title: TextView = itemView.findViewById(R.id.title1)
    }
    override fun onStart(){
        super.onStart()
        Adapter.startListening()
    }
}