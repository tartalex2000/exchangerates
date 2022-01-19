package com.alex.exchangeratesapi.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.alex.exchangeratesapi.R
import com.alex.exchangeratesapi.data.models.Rate
import com.alex.exchangeratesapi.databinding.FragmentHomeBinding
import com.alex.exchangeratesapi.main.MainViewModel
import com.alex.exchangeratesapi.ui.adapter.diffold.OldRatesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

@AndroidEntryPoint
class HomeFragment : Fragment() {
    val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->

        throwable.printStackTrace()
        throw Exception(throwable.message)
    }
companion object {
    private var sortvalue:String?="asc"
    private var sortname:String?="desc"
    private var choosed_by_name:Boolean=true

}


    lateinit var newlist:ArrayList<Rate>
  private  val viewModel: MainViewModel  by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private lateinit var ratesAdapter: OldRatesAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
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
        /*     viewModel.conversion1.observe(viewLifecycleOwner, Observer{


        })*/
        lifecycleScope.launchWhenStarted {

            viewModel.conversion.collect { event ->

                when (event) {
                    is MainViewModel.CurrencyEvent.Success -> {
                        _binding?.error?.visibility = View.INVISIBLE
                        _binding?.rvRates?.visibility = View.VISIBLE
                      //  ratesAdapter.updateList(event.rates)
                    }
                    is MainViewModel.CurrencyEvent.Failiure -> {
                        _binding?.rvRates?.visibility = View.INVISIBLE
                        _binding?.error?.visibility = View.VISIBLE
                        _binding?.error?.text = event.errorText
                    }
                    is MainViewModel.CurrencyEvent.Loading -> {

                    }
                    else -> Unit

                }
            }


        }
/*
        lifecycleScope.launchWhenStarted {
            viewModel.favourtates.collect { event ->
                Log.d("Rates:", event.toString())

            }
        }*/

        lifecycleScope.launchWhenStarted  {
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
                    else if (sortvalue=="asc"&&!choosed_by_name) {
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

       /* viewModel =
            ViewModelProvider(this).get(MainViewModel::class.java)
*/
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        configUi()
        _binding?.spnCurrency?.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
            viewModel.getRates(binding.spnCurrency.selectedItem as String)
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
        if (sortname!=null&& choosed_by_name== true) {

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
            choosed_by_name=true
            (_binding?.sortname?.nextView as TextView).setTextColor(resources.getColor(R.color.design_default_color_error))
            (_binding?.sortvalue?.nextView as TextView).setTextColor(resources.getColor(R.color.black))

            if (sortname=="asc") {
                sortname="desc"
              //  sortvalue=null
                ratesAdapter.updateList(ratesAdapter.new_rates.sortedByDescending { it.name })
            } else {

                sortname="asc"
             //   sortvalue=null
                ratesAdapter.updateList(ratesAdapter.new_rates.sortedBy { it.name })
            }

            _binding?.sortname?.setText(sortname)
            _binding?.sortvalue?.setText(sortvalue)
        }

        _binding?.sortvalue?.setOnClickListener{
            choosed_by_name=false
            (_binding?.sortvalue?.nextView as TextView).setTextColor(resources.getColor(R.color.design_default_color_error))
            (_binding?.sortname?.nextView as TextView).setTextColor(resources.getColor(R.color.black))

            if (sortvalue=="asc") {
                sortvalue="desc"
                //  sortvalue=null
                ratesAdapter.updateList(ratesAdapter.new_rates.sortedByDescending { it.value })
            } else {

                sortvalue="asc"
                //   sortvalue=null
                ratesAdapter.updateList(ratesAdapter.new_rates.sortedBy { it.value })
            }

            _binding?.sortname?.setText(sortname)
            _binding?.sortvalue?.setText(sortvalue)
        }

        viewModel.observerFavourRates()
        viewModel.favoriteModels()
        return root
    }
    private fun configUi() {
        newlist= ArrayList()
   //     ratesAdapter = RatesAdapter({position -> onListItemClick(position)})
        ratesAdapter = OldRatesAdapter(arrayListOf(),{ position -> onListItemClick(position)})
      _binding?.rvRates?.adapter= ratesAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}