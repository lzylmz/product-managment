//Mustafa Yilmaz


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Product { //create a new product node class that includes products' name and price
    private String name;
    private double price;
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
class ProductHeap {
    private List<Product> heap;  //create heap list to hold products
    private Map<String, Integer> nameToIndex;  // create a hash map to hold products name and heap index for products

    public ProductHeap() {
        heap = new ArrayList<>();
        nameToIndex = new HashMap<>();
    }

    private void swap(int i, int j) { // swap two products in the heap
        Product temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
        nameToIndex.put(heap.get(i).getName(), i);
        nameToIndex.put(heap.get(j).getName(), j);
    }


    private void heapifyUp(int index) {// Time Complexity is O(log N)
        //if the last added products have a lower prices than the parents,
        // swap child products to parent products
        int parent = (index - 1) / 2; // get parent index
        while (index > 0 && heap.get(index).getPrice() < heap.get(parent).getPrice()) {
            swap(index, parent);
            index = parent;
            parent = (index - 1) / 2;
        }
    }

    private void heapifyDown(int index) {// Time Complexity is O(log N)
        int smallest = index;
        int left = 2 * index + 1;
        int right = 2 * index + 2;


        // find is the smallest price in right child or left child
        if (left < heap.size() && heap.get(left).getPrice() < heap.get(smallest).getPrice()) {
            smallest = left;
        }

        if (right < heap.size() && heap.get(right).getPrice() < heap.get(smallest).getPrice()) {
            smallest = right;
        }

        // Swap where parent products with  smallest products
        if (smallest != index) {
            swap(index, smallest);
            heapifyDown(smallest); //recursive function to reorder all heap
        }
    }

    public void add(Product product) {// Time Complexity is O(log N), because of heapifyUp function

        if (nameToIndex.containsKey(product.getName())) {
            System.out.println("Error, product with the same name already exists.");
            return;
        }
        // add products at last of the heap
        heap.add(product);
        nameToIndex.put(product.getName(), heap.size() - 1);
        heapifyUp(heap.size() - 1);  // reorder the heap

        System.out.println(product.getName() + " with price " + String.format("%.2f",product.getPrice()) + " added." );
    }

    public void removeMin() {
        if (heap.isEmpty()) {
            System.out.println("Error, no item added yet.");
            return;
        }

        List<Product> minProducts = new ArrayList<>(); // hold all products have min prices
        double minPrice = heap.get(0).getPrice(); // keep min price

        while (!heap.isEmpty() && heap.get(0).getPrice() == minPrice) {  // find all products have  same prices
            // take min products and remove this in heap and map
            System.out.println(heap.get(0).getName() + " is removed since it has the min price");
            Product minProduct = heap.get(0);
            minProducts.add(minProduct);
            nameToIndex.remove(minProduct.getName());

            if (heap.size() > 1) {
                heap.set(0, heap.remove(heap.size() - 1));
                nameToIndex.put(heap.get(0).getName(), 0);
                heapifyDown(0);

            } else {
                heap.clear();
            }
        }

        }

    public void decreasePrice(String productName, double amount) {
        if (!nameToIndex.containsKey(productName)) {
            System.out.println("Error, no item added yet.");
            return;
        }

        int index = nameToIndex.get(productName); //get the product's heap's index in maps
        Product product = heap.get(index);          // get the product in heap
        product.setPrice(product.getPrice() - amount); // change product's price

        if (amount > 0) {// reorder the heap
            heapifyUp(index);
        } else {
            heapifyDown(index);
        }
        System.out.println(product.getName() +"'s price is decreased by " + (int)amount + " (making it " +String.format("%.2f",product.getPrice())+")");

    }

    public void listMin() {
        if (heap.isEmpty()) {
            System.out.println("Error, no item added yet.");
            return;
        }
        Product minProduct = heap.get(0);  //Get the product at the list at the first index
        System.out.println(minProduct.getName() + " with price " + String.format("%.2f",minProduct.getPrice()) + " listed (without removing).");
    }
}

public class ProductManagement {
    public static void main(String[] args) {
        ProductHeap productHeap = new ProductHeap();

        try (BufferedReader br = new BufferedReader(new FileReader("process.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(" ");

                if (tokens[0].equals("ListMin")) {
                    productHeap.listMin();
                } else if (tokens[0].equals("RemoveMin")) {
                    productHeap.removeMin();
                } else if (tokens[0].equals("DecreasePrice")) {
                    String productName = tokens[1];
                    double amount = Double.parseDouble(tokens[2]);
                    productHeap.decreasePrice(productName, amount);
                } else if (tokens[0].equals("Add")) {
                    String productName = tokens[1];
                    double price = Double.parseDouble(tokens[2]);
                    productHeap.add(new Product(productName, price));

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}