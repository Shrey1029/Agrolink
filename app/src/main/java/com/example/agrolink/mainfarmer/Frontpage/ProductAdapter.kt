import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agrolink.R
import com.example.agrolink.databinding.CardviewholderBinding
import com.squareup.picasso.Picasso

// Define Product data class within the same file for clarity (if not imported separately)
data class Product(
    val productName: String,
    val stock: String,
    val price: String,
    val imageUrl: String,
    val description: String,
    val productID: String // Document ID from Firestore
)

class ProductAdapter(
    private val productList: List<Product>,  // List of Product items
    private val onProductClick: (Product) -> Unit // Lambda function to handle product click events
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // ViewHolder class to hold and bind product views
    class ProductViewHolder(val binding: CardviewholderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = CardviewholderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position] // Get the current product item
        holder.binding.productname.text = product.productName
        holder.binding.quantity.text = product.stock
        holder.binding.price.text = product.price
        holder.binding.description.text = product.description

        // Load image with Picasso, handle if the URL is empty
        if (product.imageUrl.isNotEmpty()) {
            Picasso.get()
                .load(product.imageUrl)
                .placeholder(R.drawable.placeholder) // Placeholder while loading
                .into(holder.binding.imageView)
        } else {
            holder.binding.imageView.setImageResource(R.drawable.placeholder) // Set default image
        }

        // Set click listener on the product card
        holder.itemView.setOnClickListener {
            onProductClick(product) // Pass the product to the click handler
        }
    }

    override fun getItemCount() = productList.size
}
