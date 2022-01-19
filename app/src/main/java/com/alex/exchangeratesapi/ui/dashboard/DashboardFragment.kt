package com.alex.exchangeratesapi.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alex.exchangeratesapi.R
import com.alex.exchangeratesapi.data.models.Rate
import com.alex.exchangeratesapi.databinding.FragmentDashboardBinding
import com.alex.exchangeratesapi.main.MainViewModel
import com.alex.exchangeratesapi.ui.adapter.RatesAdapter
import com.alex.exchangeratesapi.ui.adapter.diffold.OldRatesAdapter
import com.alex.exchangeratesapi.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    companion object {
        private var sortvalue:String?="asc"
        private var sortname:String?="desc"
        private var choosed_by_name:Boolean=true
    }

    private var _binding: FragmentDashboardBinding? = null
    private  val viewModel: MainViewModel  by viewModels()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var ratesAdapter: OldRatesAdapter
    private lateinit var rateslist:String
    lateinit var newlist:ArrayList<Rate>
    private fun onListItemClick(item: String) {
        val i:Rate=Rate(item,12.1,true)

        if (viewModel.favourtates.value.map{it.name}.contains(i.name)) {
            //     item.isFavourite = false
            viewModel.delete(i)
        } else {
            i.isFavourite = true
            viewModel.add(i)
        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycleScope.launchWhenStarted {

            viewModel.favourtates.collect { rates ->
                rateslist=rates.map { it.name }.joinToString(",")

            }
        }

        viewLifecycleOwner.lifecycleScope.launch  {

            viewModel.favoriteModels().collect { event ->
                Log.d("Merge:", event.toString())
                newlist = ArrayList()

                newlist.addAll(event)

                if (sortname == "desc"&& choosed_by_name) {
                    val ll = newlist.sortedByDescending { it.name }
                    ratesAdapter.updateList(ll)
                } else if (sortname == "asc"&& choosed_by_name) {
                    val ll = newlist.sortedBy { it.name }
                    ratesAdapter.updateList(ll)
                } else if (sortvalue == "desc"&& !choosed_by_name) {
                    val ll = newlist.sortedByDescending { it.value }
                    ratesAdapter.updateList(ll)
                }
                else if (sortvalue =="asc"&&!choosed_by_name) {
                    val ll = newlist.sortedBy { it.value }
                    ratesAdapter.updateList(ll)
                } else {
                    ratesAdapter.updateList(newlist)
                }



            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _binding?.spnCurrency?.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.getRates(binding.spnCurrency.selectedItem as String, rateslist)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        _binding?.sortname?.setFactory{
            val textView = TextView(context)
            textView.gravity =  Gravity.CENTER


            textView.textSize = 21f
            textView.isSingleLine = true
            textView.setTextColor(Color.BLACK)
            textView
        }
        _binding?.sortvalue?.setFactory{
            val textView = TextView(context)
            textView.gravity =  Gravity.CENTER


            textView.textSize = 21f
            textView.isSingleLine = true
            textView.setTextColor(Color.BLACK)
            textView
        }
        /*      val inAnimation: Animation = AnimationUtils.loadAnimation(
                  context,
                  android.R.anim.fade_in
              )
              val outAnimation: Animation = AnimationUtils.loadAnimation(
                  context,
                  android.R.anim.fade_out
              )
              _binding?.sortname?.inAnimation = inAnimation
              _binding?.sortname?.outAnimation = outAnimation*/
//        album_artists=view.findViewById(R.id.album_artists)
        if (sortname !=null&& choosed_by_name == true) {

            (_binding?.sortname?.nextView as TextView).setTextColor(resources.getColor(R.color.design_default_color_error))

            if (sortname == "asc")  {
                _binding?.sortname?.setText("asc")
            }
            else{
                if (sortname == "desc") _binding?.sortname?.setText("desc")

            }
            _binding?.sortvalue?.setText(sortvalue)
        } else {

            (_binding?.sortvalue?.nextView as TextView).setTextColor(resources.getColor(R.color.design_default_color_error))

            if (sortvalue == "asc")  {
                _binding?.sortvalue?.setText("asc")
            }
            else{
                if (sortvalue == "desc") _binding?.sortvalue?.setText("desc")

            }
            _binding?.sortname?.setText(sortname)
        }
        _binding?.sortname?.setOnClickListener{
            choosed_by_name =true
            (_binding?.sortname?.nextView as TextView).setTextColor(resources.getColor(R.color.design_default_color_error))
            (_binding?.sortvalue?.nextView as TextView).setTextColor(resources.getColor(R.color.black))

            if (sortname =="asc") {
                sortname ="desc"
                //  sortvalue=null
                ratesAdapter.updateList(ratesAdapter.new_rates.sortedByDescending { it.name })
            } else {

                sortname ="asc"
                //   sortvalue=null
                ratesAdapter.updateList(ratesAdapter.new_rates.sortedBy { it.name })
            }

            _binding?.sortname?.setText(sortname)
            _binding?.sortvalue?.setText(sortvalue)
        }

        _binding?.sortvalue?.setOnClickListener{
            choosed_by_name =false
            (_binding?.sortvalue?.nextView as TextView).setTextColor(resources.getColor(R.color.design_default_color_error))
            (_binding?.sortname?.nextView as TextView).setTextColor(resources.getColor(R.color.black))

            if (sortvalue =="asc") {
                sortvalue ="desc"
                //  sortvalue=null
                ratesAdapter.updateList(ratesAdapter.new_rates.sortedByDescending { it.value })
            } else {

                sortvalue ="asc"
                //   sortvalue=null
                ratesAdapter.updateList(ratesAdapter.new_rates.sortedBy { it.value })
            }

            _binding?.sortname?.setText(sortname)
            _binding?.sortvalue?.setText(sortvalue)
        }

        configUi()
        viewModel.observerFavourRates()

        return root
    }
    private fun configUi() {
        ratesAdapter = OldRatesAdapter( arrayListOf(),{position -> onListItemClick(position)})
        _binding?.rvRates?.adapter= ratesAdapter

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}