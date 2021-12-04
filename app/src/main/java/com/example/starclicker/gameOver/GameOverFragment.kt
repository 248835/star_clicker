package com.example.starclicker.gameOver

import android.os.Bundle
import android.view.*
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.starclicker.R
import com.example.starclicker.database.StarClickerDatabase
import com.example.starclicker.databinding.GameOverFragmentBinding
import com.example.starclicker.ViewModelFactory
import com.example.starclicker.database.Score
import com.example.starclicker.rankingRv.AdapterUser
import com.example.starclicker.rankingRv.ModelUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList

class GameOverFragment : Fragment() {
    private lateinit var viewModel: GameOverViewModel
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: GameOverFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.game_over_fragment, container, false)

        val args = GameOverFragmentArgs.fromBundle(requireArguments())

        val datasource = StarClickerDatabase.getInstance(requireActivity().application).databaseDao
        val viewModelFactory = ViewModelFactory(datasource)

        viewModel = ViewModelProvider(this, viewModelFactory)[GameOverViewModel::class.java]

        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.rvRanks.layoutManager = linearLayoutManager

        binding.again.setOnClickListener {
            it.findNavController()
                .navigate(GameOverFragmentDirections.actionGameOverFragmentToGameFragment(args.difficultyLevel))
        }

        val users: MutableList<ModelUser?>
        users = ArrayList()
        val mainRef = FirebaseDatabase.getInstance().getReference("Users")

        binding.tvScore.setText(args.score.toString())

        if(firebaseAuth.currentUser != null) {
            binding.tvName.visibility = View.VISIBLE
            binding.tvBestScore.visibility = View.VISIBLE
            binding.btnFirebaseSignIn.visibility = View.GONE
            binding.tvSignIn.visibility = View.GONE
            val ref = mainRef.child(firebaseAuth.currentUser?.uid!!)
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.tvName.setText(snapshot.child("name").value.toString()+",")
                    val remoteBestScore = (snapshot.child("score").value as Long).toInt()
                    binding.tvBestScore.visibility = View.VISIBLE
                    binding.tvBestScore.text = "Twój najlepszy wynik to: "+remoteBestScore.toString()
                    if(args.score>remoteBestScore){
                        binding.tvBestScore.text = "Twój najlepszy wynik to: "+args.score.toString()
                        ref.child("score").setValue(args.score).addOnCompleteListener {
                            setRecyclerView(mainRef, users, binding)
                        }
                    } else {
                        setRecyclerView(mainRef, users, binding)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        } else {
            binding.tvName.visibility = View.GONE
            binding.tvBestScore.visibility = View.GONE
            binding.btnFirebaseSignIn.visibility = View.VISIBLE
            binding.tvSignIn.visibility = View.VISIBLE
            setRecyclerView(mainRef, users, binding)
        }

        binding.btnFirebaseSignIn.setOnClickListener{viewModel.showSignInDialog(requireContext(), args.score)}

        //viewModel.insertScore(Score(points = args.score))
        ////viewModel.checkDatabase(this)

        return binding.root
    }

    fun setRecyclerView(mainRef: DatabaseReference, users: MutableList<ModelUser?>, binding: GameOverFragmentBinding){
        mainRef.orderByChild("score").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //users.clear()
                for (ds in snapshot.children) {
                    users.add(ds.getValue(ModelUser::class.java))
                }
                binding.rvRanks.adapter = AdapterUser(context!!, users)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}