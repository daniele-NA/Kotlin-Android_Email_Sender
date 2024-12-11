package com.view.control_view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.controller.Bridge
import com.controller.Utils
import com.view.R
import com.view.databinding.FragmentHistoryBinding



class HistoryFragment : Fragment(),Utils {  //implementa l'interfaccia utils

    private lateinit var binding: FragmentHistoryBinding  //chiama il suo xml
    private lateinit var parent: Context

    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var listView: ListView



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding=FragmentHistoryBinding.inflate(inflater, container, false)
        parent=requireContext()
        setHasOptionsMenu(true)  //specifica la presenza di un menu
        readyForOperations()
        return binding.root
    }


    private fun readyForOperations(){

        listView = binding.listView

        /*
        si passa prima il layout e dopo la View interessata
         */

        adapter = ArrayAdapter(parent,R.layout.list_item,R.id.textViewItem, Bridge.database.listEmailToString)
        listView.adapter = adapter

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {  //vale solo per questo frammento
        /*
      override solo del Clear che in ogni Fragment esegue una cosa diversa,  LogOut fatto nel MAINACTIVITY
       */
        return when (item.itemId) {
            R.id.clear_item -> {
                Bridge.database.clearAllEmailData()
                adapter.notifyDataSetChanged()
                shortToast(parent, "Storico eliminato")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {   //ogni qual volta che si torna in questo frammento
        super.onResume()
        adapter.notifyDataSetChanged()
    }




}