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
import android.widget.TextView
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
import com.example.hangin.place.PlaceAdapter.RecyclerViewClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), RecyclerViewClickListener {

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var editTextSearch: EditText
    private lateinit var imageViewClose: ImageView
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var layoutEmpty: ConstraintLayout
    private lateinit var textViewTryAgain: TextView

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
        layoutEmpty = view.findViewById(R.id.layout_empty)
        textViewTryAgain = view.findViewById(R.id.textView_emptyBody)

        showSearchAnimator = AnimationUtils.loadAnimation(context, R.anim.show_swipe_left)
        hideSearchAnimator = AnimationUtils.loadAnimation(context, R.anim.hide_swipe_right)

        places = ArrayList()

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

        /** Try to load the recycler items again **/
        textViewTryAgain.setOnClickListener { getPlaces() }

        /** Filtration the recycler elements to the searched text **/
        editTextSearch.addTextChangedListener { filterRecyclerViewBySearch(it.toString()) }

        /** Swipe Container for the recycler view **/
        swipeContainer.setOnRefreshListener {
            if (recyclerView.visibility == View.VISIBLE) {
                placeAdapter.clear()
                getPlaces()
            } else getPlaces()
        }
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary)
    }

    private fun filterRecyclerViewBySearch(searchText: String) {
        if (!TextUtils.isEmpty(searchText)) {
            loadRecyclerView(placeAdapter.filter(searchText))
        } else loadRecyclerView(places)
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
                loadRecyclerView(places)
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
        progressBar.visibility = View.VISIBLE

        val service = APIClient.retrofit.create(APIInterface::class.java)
        val call = service.getPlaces()

        call.enqueue(object : Callback<ArrayList<Place>> {
            override fun onFailure(call: Call<ArrayList<Place>>, t: Throwable) {
                showEmptyView()
                call.cancel()
            }

            override fun onResponse(
                call: Call<ArrayList<Place>>,
                response: Response<ArrayList<Place>>
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.size > 0) {
                        restoreRecyclerViewVisibility()
                        places = response.body()!!
                        loadRecyclerView(places)
                        progressBar.visibility = View.GONE
                    } else showEmptyView()
                    call.cancel()
                } else showEmptyView()
            }
        })
    }

    private fun showEmptyView() {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.GONE
        layoutEmpty.visibility = View.VISIBLE
        swipeContainer.isRefreshing = false
    }


    private fun loadRecyclerView(places: ArrayList<Place>) {
        placeAdapter = PlaceAdapter(places)
        recyclerView.adapter = placeAdapter
        swipeContainer.isRefreshing = false
    }

    private fun restoreRecyclerViewVisibility() {
        if (recyclerView.visibility == View.GONE) {
            recyclerView.visibility = View.VISIBLE
            layoutEmpty.visibility = View.GONE
        }
    }

//    private fun openPlaceDetailActivity(position: Int){
//        Toast.makeText(this.context, "item $position is clicked", Toast.LENGTH_LONG).show()
//        val place = places[position]
//        val detailIntent = Intent(this.context, DetailActivity::class.java)
////        detailIntent.putExtra("place", place as Serializable)
//        startActivity(detailIntent)
//    }

    override fun recyclerViewListClicked(v: View, position: Int) {}

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
