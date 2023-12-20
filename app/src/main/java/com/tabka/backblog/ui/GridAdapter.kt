import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tabka.backblog.R
import com.tabka.backblog.ui.home.HomeFragment

data class ImageItem(val imageResId: Int)
class GridAdapter(private val mData: List<ImageItem>,
                  private val customizeView: (ViewHolder) -> Unit) : RecyclerView.Adapter<GridAdapter.ViewHolder>() {
    private val TAG = "Grid Adapter"

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image_view)
    }

    // Create new views for each grid item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_image_view, parent, false)
        return ViewHolder(view)
    }

    // Bind data to each view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = mData[position]
        holder.imageView.setImageResource(item.imageResId)
        customizeView(holder)
    }

    override fun getItemCount() = mData.size
}

