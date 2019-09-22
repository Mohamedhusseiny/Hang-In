package com.example.hangin


import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.hangin.network.APIClient
import com.example.hangin.network.APIInterface
import com.example.hangin.place.Place
import com.example.hangin.place.PlaceAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var editTextSearch: EditText
    private lateinit var imageViewClose: ImageView
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var constraintLayout: ConstraintLayout

    private lateinit var showSearchAnimator: Animation
    private lateinit var hideSearchAnimator: Animation

    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var places: ArrayList<Place>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.place_progressBar)
        editTextSearch = view.findViewById(R.id.editText_search)
        imageViewClose = view.findViewById(R.id.imageView_close)
        swipeContainer = view.findViewById(R.id.swipe_refresh_layout)
        constraintLayout = view.findViewById(R.id.constraintLayout)

        showSearchAnimator = AnimationUtils.loadAnimation(context, R.anim.show_swipe_left)
        hideSearchAnimator = AnimationUtils.loadAnimation(context, R.anim.hide_swipe_right)
        places = ArrayList()
        // Inflate the layout for this fragment
        return view
    }

    override fun onStart() {
        super.onStart()
        getPlaces()
    }

    override fun onResume() {
        super.onResume()
        /** Disable search context **/
        imageViewClose.setOnClickListener {
            hideSearchContext()
        }

        /** Filtration the recycler elements to the searched text **/
        editTextSearch.addTextChangedListener {
            filterRecyclerViewBySearch(it.toString())
        }

        /** Swipe Container for the recycler view **/
        swipeContainer.setOnRefreshListener {
            placeAdapter.clear()
            getPlaces()
        }
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary)
    }

    private fun filterRecyclerViewBySearch(searchText: String) {
        if (!TextUtils.isEmpty(searchText)) {
            loadRecyclerView(placeAdapter.filter(searchText))
        } else getPlaces()
    }

    private fun hideSearchContext() {
        hideSearchAnimator.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {
                constraintLayout.visibility = View.GONE
                (activity as AppCompatActivity).supportActionBar!!.show()
            }

            override fun onAnimationStart(p0: Animation?) {
                hideSoftKeyboard(editTextSearch)
                getPlaces()
            }
        })
        constraintLayout.startAnimation(hideSearchAnimator)
    }

    private fun showSearchContext() {
        if (constraintLayout.visibility != View.VISIBLE) {
            (activity as AppCompatActivity).supportActionBar!!.hide()
            constraintLayout.visibility = View.VISIBLE
            constraintLayout.startAnimation(showSearchAnimator)
            requestEditTextFocus()
        }
    }

    private fun requestEditTextFocus() {
        editTextSearch.text.clear()
        editTextSearch.requestFocus()
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextSearch, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideSoftKeyboard(view: View) {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun getPlaces() {
        val service = APIClient.retrofit.create(APIInterface::class.java)
        val call = service.getPlaces()

        call.enqueue(object : Callback<ArrayList<Place>> {
            override fun onFailure(call: Call<ArrayList<Place>>, t: Throwable) {
                // TODO: set empty view
                call.cancel()
                //Toast.makeText(this@PlaceActivity, "500", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ArrayList<Place>>,
                response: Response<ArrayList<Place>>
            ) {
                if (response.code() == 200) {
                    places = response.body()!!
                    loadRecyclerView(places)
                    progressBar.visibility = View.GONE
                    call.cancel()
                } //else Toast.makeText(this@PlaceActivity, "400", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun loadRecyclerView(places: java.util.ArrayList<Place>) {
        placeAdapter = PlaceAdapter(places)
        recyclerView.adapter = placeAdapter
        swipeContainer.isRefreshing = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> showSearchContext()
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        constraintLayout.visibility = View.GONE
    }
}
