package utils.https;

import com.example.project.model.CartItem;
import com.example.project.model.Order;
import com.example.project.model.Product;
import com.example.project.model.Store;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import utils.https.types.request.PreSignedRequest;
import utils.https.types.request.ProductRequest;
import utils.https.types.request.SignupRequest;
import utils.https.types.response.SignupResponse;

public interface ProjectAPI {
    // Users
    @GET("/users/products")
    Call<ApiResponse<List<Product>>> products();

    @GET("/users/products/{productId}")
    Call<ApiResponse<Product>> getProduct(@Path("productId") int productId);

    @GET("/users/cart")
    Call<ApiResponse<List<CartItem>>> getProfile();

    @POST("/users/cart/add/{productId}")
    Call<ApiResponse<CartItem>> addProductToCart(@Path("productId") int productId, @Query("quantity") int quantity);

    @DELETE("/users/cart/remove/{productId}")
    Call<ApiResponse<Void>> removeProductFromCart(@Path("productId") int productId);

    @PUT("/users/cart/modify/{productId}")
    Call<ApiResponse<CartItem>> modifyProductInCart(@Path("productId") int productId, @Query("quantity") int quantity);

    @POST("/users/cart/clear")
    Call<ApiResponse<List<CartItem>>> clearCart();

    @GET("/users/orders")
    Call<ApiResponse<List<Order>>> getOrders();

    @GET("/users/orders/{orderId}")
    Call<ApiResponse<Order>> getOrder(@Path("orderId") int orderId);

    @POST("/users/order/create")
    Call<ApiResponse<Void>> createOrder(@Body List<Integer> productIds);

    @POST("/users/order/payment-callback")
    Call<ApiResponse<Void>> paymentCallback(@Query("orderId") int orderId, @Query("status") String status);

    //Auths
    @POST("/auths/signup")
    Call<ApiResponse<SignupResponse>> signup(@Body SignupRequest request);

    //Stores
    @GET("/stores/info")
    Call<ApiResponse<Store>> getStoreInfo();

    @POST("/stores/presigned-url")
    Call<ApiResponse<String>> getPresignedUrl(@Body PreSignedRequest request);

    @GET("/stores/products")
    Call<ApiResponse<List<Product>>> getProducts();

    @POST("/stores/products")
    Call<ApiResponse<Product>> addProduct(@Body ProductRequest request);

    @PUT("/stores/products/{productId}")
    Call<ApiResponse<Product>> updateProduct(@Path("productId") int productId, @Body ProductRequest request);

    @DELETE("/stores/products/{productId}")
    Call<ApiResponse<Void>> deleteProduct(@Path("productId") int productId);

    @GET("/stores/orders")
    Call<ApiResponse<List<Order>>> getStoreOrders();

    @GET("/stores/orders/{orderId}")
    Call<ApiResponse<Order>> getStoreOrder(@Path("orderId") int orderId);
}
