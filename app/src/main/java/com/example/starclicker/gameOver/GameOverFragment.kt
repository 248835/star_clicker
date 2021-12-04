package com.example.starclicker.gameOver

import android.os.Bundle
import android.view.*
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
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

        binding.again.setOnClickListener {
            it.findNavController()
                .navigate(GameOverFragmentDirections.actionGameOverFragmentToGameFragment(args.difficultyLevel))
        }

        binding.tvScore.setText(args.score.toString())

        val users: MutableList<ModelUser?>
        users = ArrayList()
        val mainRef = FirebaseDatabase.getInstance().getReference("Users")

        if(firebaseAuth.currentUser != null) {
            val ref = mainRef.child(firebaseAuth.currentUser?.uid!!)
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val remoteBestScore = (snapshot.child("score").value as Long).toInt()
                    binding.tvBestScore.visibility = View.VISIBLE
                    binding.tvBestScore.text = "Twój najlepszy wynik to: "+remoteBestScore.toString()
                    if(args.score>remoteBestScore){
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
            setRecyclerView(mainRef, users, binding)
        }

        binding.btnFirebaseSignIn.setOnClickListener{viewModel.showSignInDialog(requireContext(), args.score)}

        return binding.root
    }

    fun setRecyclerView(mainRef: DatabaseReference, users: MutableList<ModelUser?>, binding: GameOverFragmentBinding){
        mainRef.addListenerForSingleValueEvent(object : ValueEventListener {
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