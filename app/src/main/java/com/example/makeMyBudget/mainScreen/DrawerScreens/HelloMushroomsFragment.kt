package com.example.makeMyBudget.mainScreen.DrawerScreens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.makemybudget.R
import com.example.makemybudget.databinding.FragmentHelloMushroomsBinding

class HelloMushroomsFragment : Fragment() {

    lateinit var binding : FragmentHelloMushroomsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHelloMushroomsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hello_mushrooms, container, false)
    }


}